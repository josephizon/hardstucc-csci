package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class RewardsSoftRecycleView extends RecyclerView.ViewHolder {

    ImageView rewardSoftImage;

    TextView rewardSoftPrice;

    Button rewardSoftStatusButton;

    public RewardsSoftRecycleView(@NonNull View itemView) {
        super(itemView);

        rewardSoftImage = itemView.findViewById(R.id.reward_soft_image);
        rewardSoftPrice = itemView.findViewById(R.id.reward_soft_price);
        rewardSoftStatusButton = itemView.findViewById(R.id.reward_soft_status_button);
    }
}
