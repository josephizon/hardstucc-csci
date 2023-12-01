package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BattlePassRecycleAdapter extends RecyclerView.Adapter<BattlePassRecycleView> {

    Context context;
    List<BattlePassRecycleItems> items;

    public BattlePassRecycleAdapter(Context context, List<BattlePassRecycleItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BattlePassRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BattlePassRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_battle_pass2_recycle_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BattlePassRecycleView holder, int position) {
        holder.itemName.setText(items.get(position).getItemName());
        holder.itemProgress.setProgress(items.get(position).getItemProgress());
        holder.itemImage.setImageResource(items.get(position).getItemImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
