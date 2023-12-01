package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BattlePassRecycleView extends RecyclerView.ViewHolder{
    TextView itemName;
    ImageView itemProgress;
    ImageView itemImage;

    public BattlePassRecycleView(@NonNull View itemView){
        super(itemView);
        itemName = itemView.findViewById(R.id.reward_title);
        itemProgress = itemView.findViewById(R.id.reward_progress);
        itemImage = itemView.findViewById(R.id.reward_image);
    }

}
