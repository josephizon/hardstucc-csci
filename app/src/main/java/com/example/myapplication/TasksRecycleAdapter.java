package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class TasksRecycleAdapter extends RecyclerView.Adapter<TasksRecycleView> {

    private Context context;
    private List<TasksRecycleItems> items;
    private boolean isBuddyPage;
    private FirebaseUser User;
    private DatabaseReference databaseReference;

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
                holder.btnMarkAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TasksRecycleAdapter", "whole button clicked for taskId: " + currentItem.getTaskId());

                        updateBuddyStatusInFirebase(User, currentItem.getTaskId(), "Accomplished");
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
}

