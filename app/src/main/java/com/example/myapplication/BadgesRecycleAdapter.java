package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BadgesRecycleAdapter extends RecyclerView.Adapter<BadgesRecycleView> {

    Context context;
    List<BadgesRecycleItem> items;

    public BadgesRecycleAdapter(Context context, List<BadgesRecycleItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BadgesRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BadgesRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_badges_recycle_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BadgesRecycleView holder, int position) {
        holder.badgeImage.setImageResource(items.get(position).getBadgeImage());
        holder.badgeStatus.setText(items.get(position).getBadgeStatus());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
