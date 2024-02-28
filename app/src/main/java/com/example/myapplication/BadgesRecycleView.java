package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BadgesRecycleView extends RecyclerView.ViewHolder {

    ImageView badgeImage;
    TextView badgeStatus;

    public BadgesRecycleView(@NonNull View itemView) {
        super(itemView);

        badgeImage = itemView.findViewById(R.id.badge_image);
        badgeStatus = itemView.findViewById(R.id.badge_status);

    }
}
