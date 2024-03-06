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

public class BuddyTasksMajor extends AppCompatActivity {

    FirebaseAuth auth;
    Button createTaskButton;
    ImageView button;
    EditText editTaskName, editTaskDescription, editTaskDeadline, editTaskType;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference buddydatabaseReference;
    RecyclerView recyclerView;
    List<TasksRecycleItems> taskItems;
    TasksRecycleAdapter tasksRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy_tasks_major);

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
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                    .child(user.getUid());

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("buddyUid")) {
                        String buddyUid = dataSnapshot.child("buddyUid").getValue(String.class);

                        if (buddyUid != null) {
                            // Now you have the dynamic buddyUid, use it to fetch tasks
                            databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                                    .child(buddyUid)
                                    .child("Tasks");

                            // Fetch tasks from the database
                            fetchTasksFromDatabase();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(BuddyTasksMajor.this, "Failed to fetch buddyUid: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        createTaskButton = findViewById(R.id.create_major_task);


        recyclerView = findViewById(R.id.major_task_display);
        taskItems = new ArrayList<>();

        // Initialize RecyclerView adapter
        tasksRecycleAdapter = new TasksRecycleAdapter(getApplicationContext(), taskItems, true, user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tasksRecycleAdapter);

        // Fetch tasks from the database
        fetchTasksFromDatabase();

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

    public void openMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openMainActivity2(View view) {
        startActivity(new Intent(this, MainActivity2.class));
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
                                    task.getTaskId(),
                                    task.getExp()
                            );
                            taskItems.add(recycleItem);
                        }
                    }

                    // Notify the adapter that the data set has changed
                    tasksRecycleAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(BuddyTasksMajor.this, "Failed to fetch tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void openBuddyTasksDaily(View view) {
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

}