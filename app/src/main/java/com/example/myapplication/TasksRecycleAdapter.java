package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class TasksRecycleAdapter extends RecyclerView.Adapter<TasksRecycleView> {

    private Context context;
    private List<TasksRecycleItems> items;
    private boolean isBuddyPage;
    private FirebaseUser User;
    private DatabaseReference databaseReference;
    private AlertDialog.Builder confirmation;

    public TasksRecycleAdapter(Context context, List<TasksRecycleItems> items, boolean isBuddyPage, FirebaseUser User) {
        this.context = context;
        this.items = items;
        this.isBuddyPage = isBuddyPage;
        this.User = User;
    }

    @NonNull
    @Override
    public TasksRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TasksRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_tasks_recycle_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksRecycleView holder, int position) {
        TasksRecycleItems currentItem = items.get(position);

        holder.taskTitle.setText(currentItem.getTaskName());
        holder.taskDescription.setText(currentItem.getTaskDescription());
        holder.taskDeadline.setText(currentItem.getTaskDeadline());
        holder.taskStatus.setText(currentItem.getTaskStatus());

        // Update button text and clickability based on task status
        updateButton(holder, currentItem, position);

        // If buddy then Delete button is hidden. If main user then have delete button
        deleteButton(holder, currentItem, position);

        Log.d("TasksRecycleAdapter", "whole button clicked for taskId: " + currentItem.getTaskName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void updateButton(TasksRecycleView holder, TasksRecycleItems currentItem, int position) {
        String status = currentItem.getTaskStatus();

        if (isBuddyPage) {
            // Buddy Page Logic
            if ("To be Accomplished".equals(status)) {
                holder.btnMarkAsDone.setText("Not Accomplished");
                holder.btnMarkAsDone.setClickable(false);
                holder.btnMarkAsDone.setEnabled(false);
            } else if ("Pending".equals(status)) {
                holder.btnMarkAsDone.setText("Verify Accomplishment");
                holder.btnMarkAsDone.setClickable(true);
                holder.btnMarkAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TasksRecycleAdapter", "whole button clicked for taskId: " + currentItem.getTaskId());

                        updateBuddyStatusInFirebase(User, currentItem.getTaskId(), "Accomplished");
                        updateBuddyExpInFirebase(User, currentItem.getExp());
                    }
                });
            } else if ("Accomplished".equals(status)) {
                holder.btnMarkAsDone.setText("Task Complete");
                holder.btnMarkAsDone.setClickable(false);
                holder.btnMarkAsDone.setEnabled(false);
            }
        } else {
            if ("To be Accomplished".equals(status)) {
                holder.btnMarkAsDone.setText("Mark as Done");
                holder.btnMarkAsDone.setClickable(true);
                holder.btnMarkAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateStatusInFirebase(User, currentItem.getTaskId(), "Pending");
                    }
                });
            } else if ("Pending".equals(status)) {
                holder.btnMarkAsDone.setText("Waiting for Verification");
                holder.btnMarkAsDone.setClickable(false);
                holder.btnMarkAsDone.setEnabled(false);
            } else if ("Accomplished".equals(status)) {
                holder.btnMarkAsDone.setText("Task Complete");
                holder.btnMarkAsDone.setClickable(false);
                holder.btnMarkAsDone.setEnabled(false);
            }
        }
    }

    private void updateStatusInFirebase(FirebaseUser user, String taskId, String newStatus) {
        // Assuming your tasks are stored under "Registered Users" and each user has a "Tasks" node
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
                .child("Tasks")
                .child(taskId);

        // Update the "status" field with the new status
        databaseReference.child("status").setValue(newStatus);
    }
    private void updateBuddyExpInFirebase(FirebaseUser user, int taskExp) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        // Retrieve the current user's information
        usersReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users currentUser = dataSnapshot.getValue(Users.class);

                    if (currentUser != null && currentUser.getBuddyUid() != null) {
                        // Retrieve the buddy's UID from the current user's information
                        String buddyUid = currentUser.getBuddyUid();

                        // Check if taskExp is 50 and capd is less than 4
                        if (taskExp == 50) {
                            DatabaseReference buddyDailyCapRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                                    .child(buddyUid)
                                    .child("dailycap");

                            buddyDailyCapRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot capSnapshot) {
                                    if (capSnapshot.exists()) {
                                        Integer currentCap = capSnapshot.getValue(Integer.class);
                                        if (currentCap == null) currentCap = 0; // If for some reason it's null, default to 0

                                        // Check if capd is less than 4
                                        if (currentCap < 4) {
                                            // Increment capd by 1
                                            int newCap = currentCap + 1;
                                            buddyDailyCapRef.setValue(newCap);

                                            // Proceed with giving 50 exp
                                            proceedWithExpUpdate(buddyUid, taskExp);
                                        } else {
                                            // Don't give 50 exp
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle possible errors here
                                }
                            });
                        } else {
                            // Proceed with exp update for other cases
                            proceedWithExpUpdate(buddyUid, taskExp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible cancellations here
            }
        });
    }

    private void proceedWithExpUpdate(String buddyUid, int taskExp) {
        // Update the buddy's exp based on the retrieved buddy UID
        DatabaseReference buddyExpRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(buddyUid)
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
                    while (newExp >= 1000) {
                        // Subtract 1000 from exp and increase the level by 1
                        int multiplier = calculateLevel(newExp);


                        // Retrieve the current level of the buddy
                        DatabaseReference buddyLevelRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                                .child(buddyUid)
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
                                    for(int i = 1; i <= multiplier; i++)
                                    {
                                        updateLevelStatus(buddyUid, currentLevel + i);
                                    }
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
    private void updateBuddyStatusInFirebase(FirebaseUser user, String taskId, String newStatus) {
        // Assuming your users are stored under "Registered Users" and each user has a "buddyUid" field
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        // Retrieve the buddy's ID using the current user's ID
        usersReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users currentUser = dataSnapshot.getValue(Users.class);

                    if (currentUser != null) {
                        String buddyId = currentUser.getBuddyUid();

                        // Update the buddy's task status
                        DatabaseReference tasksReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                                .child(buddyId)
                                .child("Tasks")
                                .child(taskId);

                        tasksReference.child("status").setValue(newStatus);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }

    // Delete Task
    private void deleteButton(TasksRecycleView holder, TasksRecycleItems currentItem, int position) {
        String status = currentItem.getTaskStatus();

        if (isBuddyPage || currentItem.getDeletable() == false) {
            // Buddy Page Logic
            holder.btnDeleteTask.setVisibility(View.GONE);
        } else {
            // Create an if Statement to prevent users from deleting important tasks
            AlertDialog.Builder confirmation = new AlertDialog.Builder(context);
            holder.btnDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmation.setTitle("Are you sure you want to delete?");
                    confirmation.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteTaskInFirebase(User, currentItem.getTaskId());
                                }
                            });
                    confirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    confirmation.show();
                }
            });
        }
    }

    private void deleteTaskInFirebase(FirebaseUser user, String taskId) {
        // Assuming your tasks are stored under "Registered Users" and each user has a "Tasks" node
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
                .child("Tasks")
                .child(taskId);

        // Update the "status" field with the new status
        databaseReference.removeValue();
    }
}

