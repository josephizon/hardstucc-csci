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
import java.util.List;

public class TasksMajor extends AppCompatActivity {

    FirebaseAuth auth;
    Button createTaskButton, editTaskDeadline;
    ImageView button;
    EditText editTaskName, editTaskDescription, editTaskType;
    String taskDateDeadline;
    boolean visibleTask;
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

        // xp Amount
        xpChange();

        recyclerView = findViewById(R.id.major_task_display);
        taskItems = new ArrayList<>();
        // Fetch tasks from the database
        fetchTasksFromDatabase();
        boolean isBuddyPage = false;

        // Initialize RecyclerView adapter
        tasksRecycleAdapter = new TasksRecycleAdapter(recyclerView.getContext(), taskItems, isBuddyPage, user);
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
        int exp = 1500;
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

    public void xpChange() {
        if (user != null && databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        Tasks task = taskSnapshot.getValue(Tasks.class);
                        if (task != null && "major".equalsIgnoreCase(task.getType())) {
                            String[] taskStringDeadline = task.getDeadline().split("/",-1);
                            Calendar taskCalendarDeadline = Calendar.getInstance();
                            taskCalendarDeadline.set(Integer.parseInt(taskStringDeadline[2]),
                                    Integer.parseInt(taskStringDeadline[0])-1,
                                    Integer.parseInt(taskStringDeadline[1]));

                            // Check what is the major tasks: required, or not
                            // If required: check if 3 days before
                            // If x>3 days before : xp 1500, otherwise 1250
                            // If not required: xp is set to 0
                            if ( !isThreeDaysBeforeDeadline(taskCalendarDeadline)) {
                                int xpAmount = 1250;
                                databaseReference.child(task.getTaskId()).child("exp").setValue(xpAmount);
                            } else if (task.getDeletable()) {
                                int xpAmount = 0;
                                databaseReference.child(task.getTaskId()).child("exp").setValue(xpAmount);
                            }
                            else {
                                int xpAmount = 1500;
                                databaseReference.child(task.getTaskId()).child("exp").setValue(xpAmount);
                            }
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

    private boolean isThreeDaysBeforeDeadline(Calendar deadlineCalendar) {
        // Get the current date
        Calendar currentDateCalendar = Calendar.getInstance();

        // Calculate the date three days before the deadline
        Calendar threeDaysBeforeDeadline = (Calendar) deadlineCalendar.clone();
        threeDaysBeforeDeadline.add(Calendar.DAY_OF_YEAR, -2);

        // Check if the current date is three days before the deadline
        return currentDateCalendar.before(threeDaysBeforeDeadline);
    }
}