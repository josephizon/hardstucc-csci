package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuddyBattlePass extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView button;
    FirebaseUser user;
    DatabaseReference userRef, expRef, levelRef;
    String buddyUid;
    RecyclerView recyclerView;
    List<BattlePassRecycleItems> battlepassItems;
    BuddyBattlePassRecycleAdapter battlePassRecycleAdapter;
    TextView levelTextView, expTextView;

    ProgressBar xpBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy_battle_pass);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // LOGOUT BUTTON CODE
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        else {
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
                    }
                    recyclerView = findViewById(R.id.battlepass_recycler);
                    battlepassItems = new ArrayList<>();

                    battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 1));
                    battlepassItems.add(new BattlePassRecycleItems("[Badge] Peace Be With You 2", R.drawable.battlepass_locked_icon, R.drawable.badges_level_2, 2));
                    battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 3));
                    battlepassItems.add(new BattlePassRecycleItems("Hard Reward from Buddy \n100 Coins \n[Badge] A Kiss 4 U", R.drawable.battlepass_locked_icon, R.drawable.badges_level_4, 4));
                    battlepassItems.add(new BattlePassRecycleItems("Hard Reward from Ma'am Jess \n200 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_hard_reward_icon, 5));
                    battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 6));
                    battlepassItems.add(new BattlePassRecycleItems("[Badge] Seven comes before gr-Eight B)", R.drawable.battlepass_locked_icon, R.drawable.badges_level_7, 7));
                    battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 8));
                    battlepassItems.add(new BattlePassRecycleItems("Hard Reward from Buddy \n100 Coins \n[Badge] Keep it up, you’re on fire!", R.drawable.battlepass_locked_icon, R.drawable.badges_level_9, 9));
                    battlepassItems.add(new BattlePassRecycleItems("Hard Reward from Ma'am Jess \n300 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_hard_reward_icon, 10));
                    battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 11));
                    battlepassItems.add(new BattlePassRecycleItems("[Badge] 12? You’re on a roll!", R.drawable.battlepass_locked_icon, R.drawable.badges_level_12, 12));
                    battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 13));
                    battlepassItems.add(new BattlePassRecycleItems("Hard Reward from Buddy \n100 Coins \n[Badge] Isang Pindot Nalang", R.drawable.battlepass_locked_icon, R.drawable.badges_level_14, 14));
                    battlepassItems.add(new BattlePassRecycleItems("Hard Reward from Ma'am Jess \n600 Coins \n[Badge] Level 15 Trophy", R.drawable.battlepass_locked_icon, R.drawable.badges_level_15, 15));

                    battlePassRecycleAdapter = new BuddyBattlePassRecycleAdapter((getApplicationContext()), battlepassItems, user);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(battlePassRecycleAdapter);
                    levelTextView = findViewById(R.id.levelTextView);
                    expTextView = findViewById(R.id.expTextView);
                    // Create in database the xp value for each users
                    // The progress bar can only go up to 100, so when getting the user's xp we should do the math to set it in percentages/ below 100
                    xpBar = findViewById(R.id.battlepass_progressbar);

                    xpBar.setMax(1000);

                    expRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                            .child(buddyUid)
                            .child("exp");

                    expRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int userExp = dataSnapshot.getValue(Integer.class);
                                xpBar.setProgress(userExp);

                                // Update UI with real-time exp
                                expTextView.setText("Exp: " + userExp + "/1000");

                                // Fetch user level from the database
                                levelRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                                        .child(buddyUid)
                                        .child("bpLevel");

                                levelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot levelSnapshot) {
                                        if (levelSnapshot.exists()) {
                                            int userLevel = levelSnapshot.getValue(Integer.class);
                                            levelTextView.setText("Level: " + userLevel);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle onCancelled
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled
                        }
                    });
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });



        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void openMainActivity2(View view) {
        startActivity(new Intent(this, MainActivity2.class));
    }

    public void openMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void openTasksMajor(View view) {
        startActivity(new Intent(this, TasksMajor.class));
    }

    public void openProfile(View view) {
        startActivity(new Intent(this, Profile.class));
    }

    public void openBattlePass(View view) {
        startActivity(new Intent(this, BattlePass.class));
    }

    // BUDDY NAVIGATION
    public void openBuddyMainActivity2(View view) {
        startActivity(new Intent(this, BuddyMainActivity2.class));
    }

    public void openBuddyMainActivity(View view) {
        startActivity(new Intent(this, BuddyMainActivity.class));
    }

    public void openBuddyTasksDaily(View view) {
        startActivity(new Intent(this, BuddyTasksDaily.class));
    }

    public void openBuddyBattlePass(View view) {
        startActivity(new Intent(this, BuddyBattlePass.class));
    }

    public void openBuddyProfile(View view) {
        startActivity(new Intent(this, BuddyProfile.class));
    }


}