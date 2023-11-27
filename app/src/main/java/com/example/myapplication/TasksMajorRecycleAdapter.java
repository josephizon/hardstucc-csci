package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksMajorRecycleAdapter extends RecyclerView.Adapter<TasksMajorRecycleView>{

    Context context;
    List<TasksMajorRecycleItems> items;

    public TasksMajorRecycleAdapter(Context context, List<TasksMajorRecycleItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public TasksMajorRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TasksMajorRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_tasks_major_recycle_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksMajorRecycleView holder, int position) {
        holder.taskTitle.setText(items.get(position).getTaskName());
        holder.taskDescription.setText(items.get(position).getTaskDescription());
        holder.taskDeadline.setText(items.get(position).getTaskDeadline());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
