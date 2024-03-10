package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Currency;
import java.util.List;

public class TasksDaily extends AppCompatActivity {

    FirebaseAuth auth;
    Button createTaskButton, editTaskDeadline;
    String taskDateDeadline;
    ImageView button;
    EditText editTaskName, editTaskDescription, editTaskType;
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
        setContentView(R.layout.activity_tasks_daily);

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

        createTaskButton = findViewById(R.id.create_daily_task);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePopUp();
            }
        });

        // Resets Daily Tasks
        xpChange();
        resetDailyTasks();

        // Fix the List of tasks
        recyclerView = findViewById(R.id.daily_task_display);
        taskItems = new ArrayList<>();
        fetchTasksFromDatabase();


        // Initialize RecyclerView adapter
        tasksRecycleAdapter = new TasksRecycleAdapter(recyclerView.getContext(), taskItems, false, user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tasksRecycleAdapter);

        // Set up a click listener for the adapter to handle button clicks
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
        editTaskDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gets the current date and set it as default in the picker
                openCalendar();
            }
        });

        autoCompleteTextView = popUp.findViewById(R.id.auto_complete_txt);
        adapterItem = new ArrayAdapter<String>(this, R.layout.activity_tasks_dropdown, taskType);

        autoCompleteTextView.setAdapter(adapterItem);
        autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(TasksDaily.this, "Item: " + item, Toast.LENGTH_SHORT).show();
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
                        taskDateDeadline,
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

    private void openCalendar(){
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date if needed
                taskDateDeadline = String.valueOf(month+1) + "/" + String.valueOf(dayOfMonth) + "/"  + String.valueOf(year);
                editTaskDeadline.setText(calendarMonth(month+1) + ", " + String.valueOf(dayOfMonth) + " "  + String.valueOf(year));
            }
        }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }

    private String calendarMonth(int month){
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Null";
        }
    }


    private void createTask(String name, String description, String deadline, String type) {
        // Generate a unique ID for the task
        String taskId = databaseReference.push().getKey();
        int exp = 50;
        // Create a new Tasks object
        Tasks task = new Tasks(name, description, deadline, type, "To be Accomplished", taskId, exp, true);

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

                        if (task != null && "daily".equalsIgnoreCase(task.getType())) {
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
                    Toast.makeText(TasksDaily.this, "Failed to fetch tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void resetDailyTasks() {
        if (user != null && databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        Tasks task = taskSnapshot.getValue(Tasks.class);
                        if (task != null && "daily".equalsIgnoreCase(task.getType())) {
                            String[] currentDeadline = task.getDeadline().split("/",-1);
                            // Check first if the daily task is complete or not
                            // True: Reset daily task
                            // False: then Proceed as normal
                            if ( resetDailyTasksBool(Integer.valueOf(currentDeadline[1]),
                                    Integer.valueOf(currentDeadline[0]),
                                    Integer.valueOf(currentDeadline[2]),
                                    task.getStatus())) {
                                // Edit the Database to reset the daily tasks
                                databaseReference.child(task.getTaskId()).child("status").setValue("To be Accomplished");

                                Calendar currentDate = Calendar.getInstance();
                                taskDateDeadline = currentDate.get(currentDate.MONTH)+1
                                        + "/" + currentDate.get(currentDate.DAY_OF_MONTH)
                                        + "/" + currentDate.get(currentDate.YEAR);
                                databaseReference.child(task.getTaskId()).child("deadline").setValue(taskDateDeadline);
                            }
                        }
                    }

                    // Notify the adapter that the data set has changed
                    tasksRecycleAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(TasksDaily.this, "Failed to fetch tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean resetDailyTasksBool(int dayDeadline, int monthDeadline, int yearDeadline, String status) {
        Calendar currentDate = Calendar.getInstance();

        // If Status is not yet complete then ignore the whole function
        if ( "Accomplished".equalsIgnoreCase(status)){
            // If date has not passed yet, then do not reset
            if ( Integer.valueOf(currentDate.get(currentDate.YEAR)) <= yearDeadline ) {
                if (Integer.valueOf(currentDate.get(currentDate.MONTH)+1) <= monthDeadline) {
                    if (Integer.valueOf(currentDate.get(currentDate.DAY_OF_MONTH)) <= dayDeadline) {
                        return false;
                    }
                    else { return true; }
                }
                else { return true; }
            }
            else { return true; }
        }
        else { return false; }
    }

    public void xpChange() {
        if (user != null && databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        Tasks task = taskSnapshot.getValue(Tasks.class);
                        if (task != null && "daily".equalsIgnoreCase(task.getType())) {
                            // set the xp to always be 50, until it reaches cap
                            if ( true ) {
                                int xpAmount = 50;
                                databaseReference.child(task.getTaskId()).child("exp").setValue(xpAmount);
                            }
                            else {
                                int xpAmount = 0;
                                databaseReference.child(task.getTaskId()).child("exp").setValue(xpAmount);
                            }
                        }
                    }
                    // Notify the adapter that the data set has changed
                    tasksRecycleAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(TasksDaily.this, "Failed to fetch tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}