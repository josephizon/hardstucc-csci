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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class RewardsSoftRecycleAdapter extends RecyclerView.Adapter<RewardsSoftRecycleView> {

    Context context;
    List<RewardsSoftRecycleItem> items;

    DatabaseReference databaseRewardStatus, databaseUserDataReference;
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
        holder.rewardSoftType.setText(currentItem.getRewardSoftType());
        holder.rewardSoftButton.setText(currentItem.getRewardSoftButton());

        // Add OnClickListener to handle item clicks
        holder.rewardSoftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Adapter", "Item clicked: " + currentItem.getRewardSoftName());
                openPurchasePopup(currentItem);
            }
        });

        // Disable the button if the reward status is "owned"
        if (!"available".equals(currentItem.getRewardSoftStatus())) {
            holder.rewardSoftButton.setEnabled(false);
        } else {
            holder.rewardSoftButton.setEnabled(true);
        }
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
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                databaseUserDataReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                        .child(user.getUid());

                // Attach a ValueEventListener to get the current value of "coins"
                databaseUserDataReference.child("coins").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Retrieve the current value of "coins"
                        Object coinsValue = dataSnapshot.getValue();
                        if (coinsValue != null) {
                            int currentCoins = Integer.parseInt(coinsValue.toString());
                            int itemPrice = 0;


                            // Change price of item based on reward type
                            if (item.getRewardSoftType().equals("icon")) {
                                itemPrice = 100;
                            }

                            else if (item.getRewardSoftType().equals("collectible")) {
                                itemPrice = 150;
                            }


                            // Check if there are enough coins to subtract
                            if (currentCoins >= itemPrice) {
                                int newCoins = currentCoins - itemPrice;

                                // Update the value of "coins" in the database
                                databaseUserDataReference.child("coins").setValue(newCoins)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Database updated successfully
                                                Log.d("Coins", "Coins subtracted successfully");

                                                // Now update the reward status in Firebase database
                                                databaseRewardStatus = FirebaseDatabase.getInstance().getReference("Registered Users")
                                                        .child(user.getUid())
                                                        .child("SoftRewards")
                                                        .child(item.getRewardSoftName());

                                                databaseRewardStatus.child("reward_status").setValue("owned")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // Database updated successfully
                                                                Toast.makeText(context, "Item purchased successfully!", Toast.LENGTH_SHORT).show();
                                                                reloadActivity();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failures here
                                                Log.e("Coins", "Failed to subtract coins", e);
                                            }
                                        });
                            } else {
                                // Not enough coins to subtract
                                Toast.makeText(context, "Not enough coins to purchase", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("Coins", "No value found for coins");
                        }

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors here
                        Log.e("Coins", "Failed to read value.", databaseError.toException());
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

    // Method to reload the activity
    private void reloadActivity() {
        ((Activity) context).finish();
        context.startActivity(((Activity) context).getIntent());
    }


}

