package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksRecycleView extends RecyclerView.ViewHolder{
    TextView taskTitle, taskDescription, taskDeadline;

    public TasksRecycleView(@NonNull View itemView){
        super(itemView);
        taskTitle = itemView.findViewById(R.id.major_title_1);
        taskDescription = itemView.findViewById(R.id.major_desc_1);
        taskDeadline = itemView.findViewById(R.id.major_deadline_1);
    }

}
