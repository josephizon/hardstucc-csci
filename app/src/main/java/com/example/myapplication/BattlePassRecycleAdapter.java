package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        return new BattlePassRecycleView(LayoutInflater.from(context).inflate(R.layout.activity_battle_pass2_recycle_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BattlePassRecycleView holder, int position) {
        holder.itemName.setText(items.get(position).getItemName());
        holder.itemProgress.setImageResource(items.get(position).getItemProgress());
        holder.itemImage.setImageResource(items.get(position).getItemImage());
        holder.itemLevel.setText(String.valueOf(items.get(position).getItemLevel()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /*
    private void getlevel(FirebaseUser user, int taskExp) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        // Retrieve the current user's information
        usersReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users currentUser = dataSnapshot.getValue(Users.class);

                    // Retrieve the current level of the buddy
                    DatabaseReference buddyLevelRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                            .child(user.getUid())
                            .child("bpLevel");

                    buddyLevelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot levelSnapshot) {
                            if (levelSnapshot.exists()) {
                                Integer currentLevel = levelSnapshot.getValue(Integer.class);

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
                // Handle possible cancellations here
            }
        });
    }*/
}
