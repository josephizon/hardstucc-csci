package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdminActivity extends AppCompatActivity {
    Button createTaskButton, editTaskDeadline;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String[] taskType = { "Daily", "Major" };
    List<String> targetList;
    EditText editTaskName, editTaskDescription, editTaskType, expInput;
    String taskDateDeadline;
    AutoCompleteTextView autoCompleteTextView, autoCompleteTargetView, autoCompleteTargetClass;
    ArrayAdapter<String> adapterItem;
    RewardsHardRecycleAdapter recycleAdapter;
    RecyclerView recyclerView;
    List<RewardsHardRecycleItem> items;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        // Initialize lists
        targetList = new ArrayList<>();



        // Fetch user names for the student task creation
        fetchUserNames();
        
        // Display a welcome message
        TextView adminMessage = findViewById(R.id.adminMessage);
        adminMessage.setText("Welcome, Admin!");

        // Initialization and Setup for Target AutoCompleteTextView
        autoCompleteTargetView = findViewById(R.id.auto_complete_target);
        ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(this, R.layout.activity_tasks_dropdown, targetList);
        autoCompleteTargetView.setAdapter(adapterTarget);
        autoCompleteTargetView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(AdminActivity.this, "Target: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        // Button to create class tasks (you can customize the functionality)
        Button createClassTasksButton = findViewById(R.id.createClassTasksButton);
        createClassTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePopUp();
            }
        });

        Button giveExpButton = findViewById(R.id.giveExpButton);
        giveExpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTarget = autoCompleteTargetView.getText().toString();
                expInput = findViewById(R.id.expInput);
                String expString = expInput.getText().toString();
                int expGive = Integer.parseInt(expString);
                if(selectedTarget.equals("ISCS") || selectedTarget.equals("CSCI") || selectedTarget.equals("ADMIN")) {
                    // It's a class, assign the task to everyone in the class
                    giveEXPToClass(selectedTarget,expGive);
                } else {
                    // It's an individual, create a task for this specific person
                    // You might need to fetch the user's ID or specific identifier here instead of using the name directly
                    giveEXPtoIndividual(selectedTarget,expGive);
                }

            }
        });
        Button createClassRewardsButton = findViewById(R.id.createClassRewardsButton);
        createClassRewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardCreationPopUp();
            }
        });
    }

    private void fetchUserNames() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Add ISCS and CSCI to the Target type list
                targetList.add("ISCS");
                targetList.add("CSCI");
                targetList.add("ADMIN");

                //fetch rest of the usernames
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fullName = snapshot.child("firstName").getValue(String.class) + " " +
                            snapshot.child("lastName").getValue(String.class);
                    targetList.add(fullName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private void showCreatePopUp() {
        Dialog popUp = new Dialog(this, R.style.DialogStyle);
        popUp.setContentView(R.layout.admin_tasks_create);

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
                Toast.makeText(AdminActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // Initialization and Setup for Target AutoCompleteTextView
        autoCompleteTargetView = popUp.findViewById(R.id.auto_complete_target);
        ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(this, R.layout.activity_tasks_dropdown, targetList);
        autoCompleteTargetView.setAdapter(adapterTarget);
        autoCompleteTargetView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(AdminActivity.this, "Target: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        editTaskType = popUp.findViewById(R.id.auto_complete_txt);

        Button saveTaskButton = popUp.findViewById(R.id.saveTaskButton);
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTarget = autoCompleteTargetView.getText().toString();
                if(selectedTarget.equals("ISCS") || selectedTarget.equals("CSCI") || selectedTarget.equals("ADMIN")) {
                    // It's a class, assign the task to everyone in the class
                    assignTaskToClass(selectedTarget,
                            editTaskName.getText().toString(),
                            editTaskDescription.getText().toString(),
                            taskDateDeadline,
                            editTaskType.getText().toString());
                } else {
                    // It's an individual, create a task for this specific person
                    // You might need to fetch the user's ID or specific identifier here instead of using the name directly
                    createTaskForIndividual(selectedTarget,
                            editTaskName.getText().toString(),
                            editTaskDescription.getText().toString(),
                            taskDateDeadline,
                            editTaskType.getText().toString());
                }

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
    private void assignTaskToClass(String className, String name, String description, String deadline, String type) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userClass = userSnapshot.child("selectedClass").getValue(String.class);
                    if (userClass.equals(className)) {
                        // User is in the specified class, assign them the task
                        String userId = userSnapshot.getKey(); // The user's ID
                        addTaskToUser(userId, name, description, deadline, type);
                        Toast.makeText(AdminActivity.this, "it worked", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(!userClass.equals(className) && (userClass.equals("CSCI") || userClass.equals("ISCS")))
                        {
                            Toast.makeText(AdminActivity.this, "classname problem", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AdminActivity.this, "userclass problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("assignTaskToClass", "Error: ", databaseError.toException());
            }
        });
    }
    private void createTaskForIndividual(String fullName, String name, String description, String deadline, String type) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                    String lastName = userSnapshot.child("lastName").getValue(String.class);

                    // Construct the full name from the snapshot
                    String userFullName = firstName + " " + lastName;
                    if (fullName.trim().equalsIgnoreCase(userFullName.trim())) {
                        // Found the user, now create a task for them
                        String userId = userSnapshot.getKey(); // The user's ID

                        // Assuming you have a method to add a task to the user's node
                        addTaskToUser(userId, name, description, deadline, type);
                        break; // Exit the loop once the user is found and task is assigned

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("createTaskForIndiv", "Error: ", databaseError.toException());
            }
        });
    }
    private void addTaskToUser(String userId, String taskName, String description, String deadline, String type) {
        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("Tasks");

        // Generate a unique ID for the new task
        String taskId = taskRef.push().getKey();

        int exp = 1500;
        if("Major".equals(type)){
            exp = 1500;
        }
        else{
            exp = 50;
        }
        // Create a task object. Assuming a constructor similar to the previous examples
        Tasks task = new Tasks(taskName, description, deadline, type, "To be Accomplished", taskId, exp, false); // Adjust parameters as necessary

        // Save the task under the user's Tasks node
        taskRef.child(taskId).setValue(task);

        // Optionally, inform the application or user that the task was created successfully
        // For example: Log.d("addTaskToUser", "Task added to user: " + userId);
    }
    private void giveEXPToClass(String className, int exp) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userClass = userSnapshot.child("selectedClass").getValue(String.class);
                    if (userClass.equals(className)) {
                        // User is in the specified class, assign them the task
                        String userId = userSnapshot.getKey(); // The user's ID
                        proceedWithExpUpdate(userId, exp);
                    }
                    else
                    {
                        if(!userClass.equals(className) && (userClass.equals("CSCI") || userClass.equals("ISCS")))
                        {
                            Toast.makeText(AdminActivity.this, "classname problem", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AdminActivity.this, "userclass problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("assignTaskToClass", "Error: ", databaseError.toException());
            }
        });
    }
    private void giveEXPtoIndividual(String fullName, int exp){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                    String lastName = userSnapshot.child("lastName").getValue(String.class);

                    // Construct the full name from the snapshot
                    String userFullName = firstName + " " + lastName;
                    if (fullName.trim().equalsIgnoreCase(userFullName.trim())) {
                        // Found the user, now create a task for them
                        String userId = userSnapshot.getKey(); // The user's ID

                        // Assuming you have a method to add a task to the user's node
                        proceedWithExpUpdate(userId, exp);
                        break; // Exit the loop once the user is found and task is assigned

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("createTaskForIndiv", "Error: ", databaseError.toException());
            }
        });
    }
    private void proceedWithExpUpdate(String userId, int taskExp) {
        // Update the buddy's exp based on the retrieved buddy UID
        DatabaseReference buddyExpRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(userId)
                .child("exp");

        // Retrieve the current exp of the buddy
        buddyExpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot expSnapshot) {
                if (expSnapshot.exists()) {
                    Integer currentExp = expSnapshot.getValue(Integer.class);
                    if (currentExp == null) currentExp = 0; // If for some reason it's null, default to 0
                    int newExp = currentExp + taskExp;

                    // Update the exp in Firebase
                    buddyExpRef.setValue(newExp);

                    // Handle level updates
                    if (newExp >= 1000) {
                        // Subtract 1000 from exp and increase the level by 1
                        int multiplier = calculateLevel(newExp);
                        // Retrieve the current level of the buddy
                        DatabaseReference buddyLevelRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                                .child(userId)
                                .child("bpLevel");

                        buddyLevelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot levelSnapshot) {
                                if (levelSnapshot.exists()) {
                                    Integer currentLevel = levelSnapshot.getValue(Integer.class);
                                    if (currentLevel == null) currentLevel = 0; // If for some reason it's null, default to 0
                                    int newLevel = currentLevel + 1 * multiplier;

                                    // Update the level in Firebase
                                    buddyLevelRef.setValue(newLevel);
                                    updateLevelStatus(userId, newLevel);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle possible errors here
                            }
                        });
                        newExp -= 1000 * multiplier;
                        buddyExpRef.setValue(newExp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors here
            }
        });
    }
    private void updateLevelStatus(String buddyUid, int newLevel) {
        // Update the corresponding level status based on the new level
        DatabaseReference levelStatusRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(buddyUid)
                .child("level" + newLevel + "status");

        // Update the level status to "Claimable" or any other desired status
        levelStatusRef.setValue("Claimable");
    }
    private int calculateLevel(int exp) {
        // Calculate the level based on the accumulated experience points
        int level = (int) Math.floor((double) exp / 1000); // Divide and round down
        return level;
    }

    private void showRewardCreationPopUp() {
        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.admin_rewards_hard_create);

        EditText etRewardName = dialog.findViewById(R.id.reward_name_input);
        EditText etRewardDescription = dialog.findViewById(R.id.reward_description_input);
        EditText etDateCreated = dialog.findViewById(R.id.reward_date_created_input);


        // Initialization and Setup for Target AutoCompleteTextView
        autoCompleteTargetView = dialog.findViewById(R.id.auto_complete_target);
        ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(this, R.layout.activity_tasks_dropdown, targetList);
        autoCompleteTargetView.setAdapter(adapterTarget);
        autoCompleteTargetView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(AdminActivity.this, "Target: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSave = dialog.findViewById(R.id.saveTaskButton);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assuming you have EditText fields for rewardName, rewardDescription, and dateCreated


                String rewardName = etRewardName.getText().toString();
                String rewardDescription = etRewardDescription.getText().toString();
                String dateCreated = etDateCreated.getText().toString();


                // Close the dialog after saving
                dialog.dismiss();
                String selectedTarget = autoCompleteTargetView.getText().toString();
                if(selectedTarget.equals("ISCS") || selectedTarget.equals("CSCI") || selectedTarget.equals("ADMIN")){
                    // It's a class, assign the task to everyone in the class
                    saveRewardtoClass(selectedTarget,rewardName,rewardDescription,dateCreated);

                } else {
                    // It's an individual, create a task for this specific person
                    // You might need to fetch the user's ID or specific identifier here instead of using the name directly
                    saveRewardtoIndividual(selectedTarget,rewardName,rewardDescription,dateCreated);
                }

                // Close the popup if needed
                dialog.dismiss();
            }
        });
        // CLOSE BUTTON FOR POP UP CUSTOMIZATION
        ImageView btnClose = dialog.findViewById(R.id.exitCreateTaskButton);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void saveRewardtoIndividual(String fullName, String rewardName, String rewardDescription, String dateCreated){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                    String lastName = userSnapshot.child("lastName").getValue(String.class);

                    // Construct the full name from the snapshot
                    String userFullName = firstName + " " + lastName;
                    if (fullName.trim().equalsIgnoreCase(userFullName.trim())) {
                        // Found the user, now create a task for them
                        String userId = userSnapshot.getKey(); // The user's ID

                        // Assuming you have a method to add a task to the user's node
                        saveRewardToFirebase(userId, rewardName, rewardDescription, dateCreated);
                        break; // Exit the loop once the user is found and task is assigned

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("createTaskForIndiv", "Error: ", databaseError.toException());
            }
        });
    }
    private void saveRewardtoClass(String className, String rewardName, String rewardDescription, String dateCreated) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userClass = userSnapshot.child("selectedClass").getValue(String.class);
                    if (userClass.equals(className)) {
                        // User is in the specified class, assign them the task
                        String userId = userSnapshot.getKey(); // The user's ID
                        saveRewardToFirebase(userId, rewardName, rewardDescription, dateCreated);
                    }
                    else
                    {
                        if(!userClass.equals(className) && (userClass.equals("CSCI") || userClass.equals("ISCS")))
                        {
                            Toast.makeText(AdminActivity.this, "classname problem", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AdminActivity.this, "userclass problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("assignTaskToClass", "Error: ", databaseError.toException());
            }
        });
    }
    private void saveRewardToFirebase(String userId, String rewardName, String rewardDescription, String dateCreated) {
        if (userId != null) {
            RewardsHardRecycleItem reward = new RewardsHardRecycleItem(rewardName, rewardDescription, dateCreated);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Registered Users").child(userId).child("HardRewards");
            String rewardId = ref.push().getKey(); // Generate a unique ID for the reward
            if (rewardId != null) {
                ref.child(rewardId).setValue(reward).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Reward created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to create Reward", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}