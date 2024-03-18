package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardRecycleAdapter extends RecyclerView.Adapter<LeaderboardRecycleView> {

    Context context;
    List<LeaderboardRecycleItem> items;

    public LeaderboardRecycleAdapter(Context context, List<LeaderboardRecycleItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public LeaderboardRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeaderboardRecycleView(LayoutInflater.from(context).inflate(R.layout.buddy_leaderboard_recycle_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardRecycleView holder, int position) {
        holder.leader_rank.setText(items.get(position).getLeader_rank());
        holder.leader_name.setText(items.get(position).getLeader_name());
        holder.leader_level.setText(items.get(position).getLeader_level());
        holder.leader_icon.setImageResource(items.get(position).getLeader_icon());

        holder.leader_collectible_1.setImageResource(items.get(position).getLeader_collectible_1());
        holder.leader_collectible_2.setImageResource(items.get(position).getLeader_collectible_2());
        holder.leader_collectible_3.setImageResource(items.get(position).getLeader_collectible_3());

        holder.leader_badge_1.setImageResource(items.get(position).getLeader_badge_1());
        holder.leader_badge_2.setImageResource(items.get(position).getLeader_badge_2());
        holder.leader_badge_3.setImageResource(items.get(position).getLeader_badge_3());



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
