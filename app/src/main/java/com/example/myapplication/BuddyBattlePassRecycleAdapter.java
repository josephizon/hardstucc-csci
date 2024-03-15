package com.example.myapplication;

import android.app.usage.NetworkStats;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.MutableData;

import java.util.ArrayList;
import java.util.List;

public class BuddyBattlePassRecycleAdapter extends RecyclerView.Adapter<BattlePassRecycleView> {
    private FirebaseUser user;
    Context context;
    List<BattlePassRecycleItems> items;

    DatabaseReference userRef, levelStatusRef;

    String buddyUid;


    public BuddyBattlePassRecycleAdapter(Context context, List<BattlePassRecycleItems> items, FirebaseUser user) {
        this.context = context;
        this.items = items;
        this.user = user;
    }

    @NonNull
    @Override
    public BattlePassRecycleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BattlePassRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_battle_pass2_recycle_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BattlePassRecycleView holder, int position) {
        holder.itemName.setText(items.get(position).getItemName());
        holder.itemProgress.setImageResource(items.get(position).getItemProgress());
        holder.itemImage.setImageResource(items.get(position).getItemImage());
        holder.itemLevel.setText(String.valueOf(items.get(position).getItemLevel()));

        // Get the level status from Firebase and update the claim button
        updateClaimButton(holder, items.get(position).getItemLevel());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    private void updateClaimButton(BattlePassRecycleView holder, int itemLevel) {

        userRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users userProfile = dataSnapshot.getValue(Users.class);
                    if (userProfile != null) {
                        buddyUid = userProfile.getBuddyUid();

                    }
                    levelStatusRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                            .child(buddyUid) // Make sure 'user' is properly initialized in your adapter
                            .child("level" + itemLevel + "status");

                    levelStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String levelStatus = dataSnapshot.getValue(String.class);
                                if ("Claimable".equals(levelStatus)) {
                                    holder.itemClaimButton.setText("Claim");
                                    holder.itemClaimButton.setClickable(false);
                                    holder.itemClaimButton.setEnabled(false);

                                } else if ("Claimed".equals(levelStatus)) {
                                    holder.itemClaimButton.setText("Claimed");
                                    holder.itemClaimButton.setClickable(false);
                                    holder.itemClaimButton.setEnabled(false);
                                } else if ("Locked".equals(levelStatus)) {
                                    holder.itemClaimButton.setText("Locked");
                                    holder.itemClaimButton.setClickable(false);
                                    holder.itemClaimButton.setEnabled(false);
                                }
                            } else {
                                // Handle the case where there is no data (e.g., set button to default state)
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle possible errors here
                        }
                    });
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });




    }


}
