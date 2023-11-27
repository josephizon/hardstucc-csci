package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksRecycleAdapter extends RecyclerView.Adapter<TasksRecycleView>{

    Context context;
    List<TasksRecycleItems> items;

    public TasksRecycleAdapter(Context context, List<TasksRecycleItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public TasksRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TasksRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_tasks_recycle_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksRecycleView holder, int position) {
        holder.taskTitle.setText(items.get(position).getTaskName());
        holder.taskDescription.setText(items.get(position).getTaskDescription());
        holder.taskDeadline.setText(items.get(position).getTaskDeadline());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
