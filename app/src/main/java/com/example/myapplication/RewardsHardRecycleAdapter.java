package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RewardsHardRecycleAdapter extends RecyclerView.Adapter<RewardsHardRecycleView> {

    Context context;
    List<RewardsHardRecycleItem> items;

    public RewardsHardRecycleAdapter(Context context, List<RewardsHardRecycleItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RewardsHardRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RewardsHardRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_rewards_hard_recycle_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsHardRecycleView holder, int position) {
        holder.rewardName.setText(items.get(position).getRewardName());
        holder.rewardDescription.setText(items.get(position).getRewardDescription());
        holder.rewardCreation.setText(items.get(position).getDateCreated());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
