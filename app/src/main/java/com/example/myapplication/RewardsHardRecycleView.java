package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RewardsHardRecycleView extends RecyclerView.ViewHolder {

    TextView rewardName, rewardDescription, rewardCreation;
    public RewardsHardRecycleView(@NonNull View itemView) {
        super(itemView);

        rewardName = itemView.findViewById(R.id.reward_name);
        rewardDescription = itemView.findViewById(R.id.reward_description);
        rewardCreation =  itemView.findViewById(R.id.reward_creation_date);
    }
}
