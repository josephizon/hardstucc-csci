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
import java.util.Calendar;
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

    boolean visibleTask;

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
                        String[] currentDeadline = task.getDeadline().split("/",-1);
                        /*
                        Check if Major Task we create should be displayed
                        This is a hard coded thing, doesn't affect any other tasks
                        Task is Hidden if: getDeletable == false
                        Deadline of task is within week
                        */
                        Calendar currentDate = Calendar.getInstance();
                        int currentMonth = currentDate.get(Calendar.MONTH)+1;
                        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

                        if (!task.getDeletable()) {
                            // Week 2 March 17 to 23
                            if ( currentMonth == 3 && 17 <= currentDay && currentDay <= 23 &&
                                    Integer.parseInt(currentDeadline[0]) == 3 &&
                                    17 <= Integer.parseInt(currentDeadline[1]) &&
                                    Integer.parseInt(currentDeadline[1]) <= 23) {   // Week 2
                                visibleTask = true;
                            }
                            // Week 3 March 24 to 30
                            else if ( currentMonth == 3 && 24 <= currentDay && currentDay <= 30 &&
                                    Integer.parseInt(currentDeadline[0]) == 3 &&
                                    24 <= Integer.parseInt(currentDeadline[1]) &&
                                    Integer.parseInt(currentDeadline[1]) <= 30) {
                                visibleTask = true;
                            }
                            // Week 3 March 24 to 30
                            else if ( currentMonth == 3 && 24 <= currentDay && currentDay <= 30 &&
                                    Integer.parseInt(currentDeadline[0]) == 4 &&
                                    1 <= Integer.parseInt(currentDeadline[1]) &&
                                    Integer.parseInt(currentDeadline[1]) <= 6) {
                                visibleTask = true;
                            }
                            /*// Week 4 March 31
                            if ( currentMonth == 3 && currentDay ==  31 &&
                                    Integer.parseInt(currentDeadline[0]) == 3 &&
                                    31 == Integer.parseInt(currentDeadline[1])) {
                                visibleTask = true;
                            }*/
                            // Week 4 April 1 to 6
                            else if ( currentMonth == 4 && 1 <= currentDay && currentDay <= 6 &&
                                    Integer.parseInt(currentDeadline[0]) == 4 &&
                                    1 <= Integer.parseInt(currentDeadline[1]) &&
                                    Integer.parseInt(currentDeadline[1]) <= 6) {
                                visibleTask = true;
                            }
                            else {
                                visibleTask = false;
                            }
                        }
                        else {
                            visibleTask = true;
                        }

                        if (task != null && "major".equalsIgnoreCase(task.getType()) && visibleTask) {
                            TasksRecycleItems recycleItem = new TasksRecycleItems(
                                    task.getName(),
                                    task.getDescription(),
                                    task.getFormattedDeadline(),
                                    task.getStatus(),
                                    task.getTaskId(),
                                    task.getExp(),
                                    task.getDeletable()
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