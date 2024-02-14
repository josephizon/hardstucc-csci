package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BattlePassRecycleView extends RecyclerView.ViewHolder{
    TextView itemName;
    ImageView itemProgress;
    ImageView itemImage;
    TextView itemLevel;
    Button itemClaimButton;

    public BattlePassRecycleView(@NonNull View itemView){
        super(itemView);
        itemName = itemView.findViewById(R.id.reward_title);
        itemProgress = itemView.findViewById(R.id.reward_progress);
        itemImage = itemView.findViewById(R.id.reward_image);
        itemLevel = itemView.findViewById(R.id.reward_bp_level);
        itemClaimButton = itemView.findViewById(R.id.reward_claim_button);
    }

}
