package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BattlePass extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView button;
    TextView textView;
    RecyclerView recyclerView;
    List<BattlePassRecycleItems> battlepassItems;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    BattlePassRecycleAdapter battlePassRecycleAdapter;
    ProgressBar xpBar;

    // Declare TextView references
    private TextView levelTextView;
    private TextView expTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_pass2);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // BATTLEPASS TITLE GRADIENT
        TextView badges = findViewById(R.id.title_battlepass);
        int orange = Color.rgb(255, 190, 92);
        int yellow = Color.rgb(255, 206, 49);
        Shader shader1 = new LinearGradient(0f, 0f, 0f, badges.getTextSize(), orange, yellow, Shader.TileMode.CLAMP);
        badges.getPaint().setShader(shader1);

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
            //textView.setText(user.getEmail());
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

        recyclerView = findViewById(R.id.battlepass_recycler);
        battlepassItems =new ArrayList<>();

        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 1));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 2));
        battlepassItems.add(new BattlePassRecycleItems("Badge", R.drawable.battlepass_locked_icon, R.drawable.rewards_profile_icon_1, 3));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 4));
        battlepassItems.add(new BattlePassRecycleItems("(Hard Reward), 150 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_hard_reward_icon, 5));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 6));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 7));
        battlepassItems.add(new BattlePassRecycleItems("Badge", R.drawable.battlepass_locked_icon, R.drawable.rewards_profile_icon_1, 8));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 9));
        battlepassItems.add(new BattlePassRecycleItems("(Hard Reward), 150 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_hard_reward_icon, 10));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 11));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 12));
        battlepassItems.add(new BattlePassRecycleItems("Badge", R.drawable.battlepass_locked_icon, R.drawable.rewards_profile_icon_1, 13));
        battlepassItems.add(new BattlePassRecycleItems("100 Coins", R.drawable.battlepass_locked_icon, R.drawable.battlepass_coins_icon, 14));
        battlepassItems.add(new BattlePassRecycleItems("(Hard Reward), 500 Coins, \nBadge", R.drawable.battlepass_locked_icon, R.drawable.battlepass_hard_reward_icon, 15));

        battlePassRecycleAdapter = new BattlePassRecycleAdapter((getApplicationContext()), battlepassItems, user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(battlePassRecycleAdapter);
        levelTextView = findViewById(R.id.levelTextView);
        expTextView = findViewById(R.id.expTextView);
        // Create in database the xp value for each users
        // The progress bar can only go up to 100, so when getting the user's xp we should do the math to set it in percentages/ below 100
        xpBar = findViewById(R.id.battlepass_progressbar);

        xpBar.setMax(1000);
        fetchUserExpFromFirebase();
    }
    
    private void fetchUserExpFromFirebase() {
        DatabaseReference expRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
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
                    DatabaseReference levelRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                            .child(user.getUid())
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
    public void openMainActivity2(View view) {
        startActivity(new Intent(this, MainActivity2.class));
    }

    public void openMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openTasksMajor(View view) {
        startActivity(new Intent(this, TasksMajor.class));
    }

    public void openTasksDaily(View view) {
        startActivity(new Intent(this, TasksDaily.class));
    }

    public void openProfile(View view) {
        startActivity(new Intent(this, Profile.class));
    }

    public void openBattlePass(View view) {
        startActivity(new Intent(this, BattlePass.class));
    }

    public void openBadges(View view) {
        startActivity(new Intent(this, Badges.class));
    }

    public void openRewardsSoft(View view) {
        startActivity(new Intent(this, RewardsSoft.class));
    }

    public void openRewardsHard(View view) {
        startActivity(new Intent(this, RewardsHard.class));
    }

    // BUDDY NAVIGATION
    public void openBuddyMainActivity2(View view) {
        startActivity(new Intent(this, BuddyMainActivity2.class));
    }

    public void openBuddyMainActivity(View view) {
        startActivity(new Intent(this, BuddyMainActivity.class));
    }

    public void openBuddyTasks(View view) {
        startActivity(new Intent(this, BuddyTasksDaily.class));
    }

    public void openBuddyBattlePass(View view) {
        startActivity(new Intent(this, BuddyBattlePass.class));
    }

    public void openBuddyProfile(View view) {
        startActivity(new Intent(this, BuddyProfile.class));
    }

}