/*
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

    public void setItems(List<RewardsSoftRecycleItem> items) {
        this.items = items;
        notifyDataSetChanged(); // Notify adapter about dataset changes
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
        holder.rewardSoftStatus.setText(items.get(position).getRewardSoftStatus());
        holder.rewardSoftName.setText(items.get(position).getRewardSoftName());


        
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
*/

package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class RewardsSoftRecycleAdapter extends RecyclerView.Adapter<RewardsSoftRecycleView> {

    Context context;
    List<RewardsSoftRecycleItem> items;

    DatabaseReference databaseUserDataReference;
    FirebaseAuth auth;

    FirebaseUser user;



    public RewardsSoftRecycleAdapter(Context context, List<RewardsSoftRecycleItem> items) {
        this.context = context;
        this.items = items;
    }

    public void setItems(List<RewardsSoftRecycleItem> items) {
        this.items = items;
        notifyDataSetChanged(); // Notify adapter about dataset changes
    }

    @NonNull
    @Override
    public RewardsSoftRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RewardsSoftRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_rewards_soft_recycle_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsSoftRecycleView holder, int position) {
        RewardsSoftRecycleItem currentItem = items.get(position);
        holder.rewardSoftImage.setImageResource(currentItem.getRewardSoftImage());
        holder.rewardSoftPrice.setText(currentItem.getRewardSoftPrice());
        holder.rewardSoftStatus.setText(currentItem.getRewardSoftStatus());
        holder.rewardSoftName.setText(currentItem.getRewardSoftName());

        // Add OnClickListener to handle item clicks
        holder.rewardSoftStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Adapter", "Item clicked: " + currentItem.getRewardSoftName());
                openPurchasePopup(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Method to open a purchase popup
    private void openPurchasePopup(RewardsSoftRecycleItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Purchase Item");
        builder.setMessage("Do you want to purchase " + item.getRewardSoftName() + " for " + item.getRewardSoftPrice() + "?");
        builder.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the reward status in Firebase database
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                databaseUserDataReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                        .child(user.getUid())
                        .child("SoftRewards")
                        .child(item.getRewardSoftName());

                databaseUserDataReference.child("reward_status").setValue("owned")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Database updated successfully
                                Toast.makeText(context, "Item purchased successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}

