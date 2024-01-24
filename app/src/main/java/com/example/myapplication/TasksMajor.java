package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TasksMajor extends AppCompatActivity {

    FirebaseAuth auth;
    Button createTaskButton;
    ImageView button;
    EditText editTaskName, editTaskDescription, editTaskDeadline, editTaskType;
    FirebaseUser user;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<TasksRecycleItems> taskItems;
    TasksRecycleAdapter tasksRecycleAdapter;
    String[] taskType = { "Daily", "Major" };
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_major);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            // Handle the case where the user is not logged in
            // You may want to redirect them to the login screen
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                    .child(user.getUid())
                    .child("Tasks");
        }

        createTaskButton = findViewById(R.id.create_major_task);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePopUp();
            }
        });

        recyclerView = findViewById(R.id.major_task_display);
        taskItems = new ArrayList<>();
        // Fetch tasks from the database
        fetchTasksFromDatabase();
        boolean isBuddyPage = false;

        // Initialize RecyclerView adapter
        tasksRecycleAdapter = new TasksRecycleAdapter(getApplicationContext(), taskItems, isBuddyPage, user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tasksRecycleAdapter);

        // LOGOUT BUTTON
        button = findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void openMainActivity2(View view) {
        startActivity(new Intent(this, MainActivity2.class));
    }

    public void openMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openTasksMajor(View view) {
        startActivity(new Intent(this, TasksMajor.class));
    }

    public void openTasksDaily(View view) {
        startActivity(new Intent(this, TasksDaily.class));
    }

    public void openProfile(View view) {
        startActivity(new Intent(this, Profile.class));
    }

    public void openBattlePass(View view) {
        startActivity(new Intent(this, BattlePass.class));
    }

    public void openBadges(View view) {
        startActivity(new Intent(this, Badges.class));
    }

    public void openRewardsSoft(View view) {
        startActivity(new Intent(this, RewardsSoft.class));
    }

    public void openRewardsHard(View view) {
        startActivity(new Intent(this, RewardsHard.class));
    }

    private void showCreatePopUp() {
        Dialog popUp = new Dialog(this, R.style.DialogStyle);
        popUp.setContentView(R.layout.activity_tasks_create);

        editTaskName = popUp.findViewById(R.id.task_name_input);
        editTaskDescription = popUp.findViewById(R.id.task_description_input);
        editTaskDeadline = popUp.findViewById(R.id.task_deadline_input);
        autoCompleteTextView = popUp.findViewById(R.id.auto_complete_txt);
        adapterItem = new ArrayAdapter<String>(this, R.layout.activity_tasks_dropdown, taskType);

        autoCompleteTextView.setAdapter(adapterItem);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(TasksMajor.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        editTaskType = popUp.findViewById(R.id.auto_complete_txt);

        Button saveTaskButton = popUp.findViewById(R.id.saveTaskButton);
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask(
                        editTaskName.getText().toString(),
                        editTaskDescription.getText().toString(),
                        editTaskDeadline.getText().toString(),
                        editTaskType.getText().toString()
                );

                // Close the popup if needed
                popUp.dismiss();
            }
        });

        // CLOSE BUTTON FOR POP UP CUSTOMIZATION
        ImageView btnClose = popUp.findViewById(R.id.exitCreateTaskButton);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });

        popUp.show();
    }

    private void createTask(String name, String description, String deadline, String type) {
        // Generate a unique ID for the task
        String taskId = databaseReference.push().getKey();

        // Create a new Tasks object
        Tasks task = new Tasks(name, description, deadline, type, "To be Accomplished", taskId);

        // Add the task to the database under the user's node
        databaseReference.child(taskId).setValue(task);

        // Provide feedback to the user (optional)
        Toast.makeText(this, "Task created successfully", Toast.LENGTH_SHORT).show();
    }
    private void fetchTasksFromDatabase() {
        if (user != null && databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    taskItems.clear(); // Clear the existing list before adding new tasks

                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        Tasks task = taskSnapshot.getValue(Tasks.class);

                        if (task != null && "major".equalsIgnoreCase(task.getType())) {
                            TasksRecycleItems recycleItem = new TasksRecycleItems(
                                    task.getName(),
                                    task.getDescription(),
                                    task.getDeadline(),
                                    task.getStatus(),
                                    task.getTaskId()
                            );
                            taskItems.add(recycleItem);
                        }
                    }

                    // Notify the adapter that the data set has changed
                    tasksRecycleAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(TasksMajor.this, "Failed to fetch tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // BUDDY NAVIGATION
    public void openBuddyMainActivity2(View view) {
        startActivity(new Intent(this, BuddyMainActivity2.class));
    }

    public void openBuddyMainActivity(View view) {
        startActivity(new Intent(this, BuddyMainActivity.class));
    }

    public void openBuddyTasks(View view) {
        startActivity(new Intent(this, BuddyTasksDaily.class));
    }

    public void openBuddyTasksMajor(View view) {
        startActivity(new Intent(this, BuddyTasksMajor.class));
    }

    public void openBuddyBattlePass(View view) {
        startActivity(new Intent(this, BuddyBattlePass.class));
    }

    public void openBuddyProfile(View view) {
        startActivity(new Intent(this, BuddyProfile.class));
    }

    public void openBuddyStore(View view) {
        startActivity(new Intent(this, BuddyRewardsSoft.class));
    }

    public void openBuddyBadges(View view) {
        startActivity(new Intent(this, BuddyBadges.class));
    }
}