package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardRecycleView extends RecyclerView.ViewHolder {

    TextView leader_rank, leader_name, leader_level;

    ImageView leader_icon, leader_collectible_1, leader_collectible_2, leader_collectible_3;

    ImageView leader_badge_1, leader_badge_2, leader_badge_3;
    public LeaderboardRecycleView(@NonNull View itemView) {
        super(itemView);
        leader_rank = itemView.findViewById(R.id.leader_rank);
        leader_name = itemView.findViewById(R.id.leader_name);
        leader_level = itemView.findViewById(R.id.leader_level);

        leader_icon = itemView.findViewById(R.id.leader_profile_icon);
        leader_collectible_1 = itemView.findViewById(R.id.leader_collectible_1);
        leader_collectible_2 = itemView.findViewById(R.id.leader_collectible_2);
        leader_collectible_3 = itemView.findViewById(R.id.leader_collectible_3);

        leader_badge_1 = itemView.findViewById(R.id.leader_badge_1);
        leader_badge_2 = itemView.findViewById(R.id.leader_badge_2);
        leader_badge_3 = itemView.findViewById(R.id.leader_badge_3);
    }
}
