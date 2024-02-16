package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RewardsSoftRecycleAdapter extends RecyclerView.Adapter<RewardsSoftRecycleView> {

    Context context;
    List<RewardsSoftRecycleItem> items;

    public RewardsSoftRecycleAdapter(Context context, List<RewardsSoftRecycleItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RewardsSoftRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RewardsSoftRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_rewards_soft_recycle_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsSoftRecycleView holder, int position) {
        holder.rewardSoftImage.setImageResource(items.get(position).getRewardSoftImage());
        holder.rewardSoftPrice.setText(items.get(position).getRewardSoftPrice());
        
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
