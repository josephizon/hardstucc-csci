package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



public class RewardsSoft extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView button;
    TextView textView;
    FirebaseUser user;

    DatabaseReference databaseReference;

    DatabaseReference databaseUserDataReference;

    RecyclerView recyclerView;

    RewardsSoftRecycleAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_soft);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // BADGES GRADIENT
        TextView badges = findViewById(R.id.title_rewards_store);
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

        /*// ADDING ITEMS TO RECYCLE VIEW 1

        // Set up RecyclerView with the retrieved data
        RecyclerView recyclerView = findViewById(R.id.rewards_soft_recyclerview);

        // RecyclerView recyclerView = findViewById(R.id.rewards_soft_recyclerview);

        List<RewardsSoftRecycleItem> items = new ArrayList<RewardsSoftRecycleItem>();
        items.add(new RewardsSoftRecycleItem("rewards_profile_icon_1", "200", "available", R.drawable.rewards_profile_icon_1 ));
        items.add(new RewardsSoftRecycleItem("rewards_profile_icon_2", "300", "available", R.drawable.rewards_profile_icon_2 ));
        items.add(new RewardsSoftRecycleItem("rewards_profile_icon_3", "200", "available", R.drawable.rewards_profile_icon_3 ));
        items.add(new RewardsSoftRecycleItem("rewards_profile_icon_4", "300", "available", R.drawable.rewards_profile_icon_4 ));

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        recyclerView.setAdapter(new RewardsSoftRecycleAdapter(getApplicationContext(), items));*/

        // Set up Firebase Database reference (Displaying the buyable stuff)
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
                .child("SoftRewards");

        recyclerView = findViewById(R.id.rewards_soft_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        adapter = new RewardsSoftRecycleAdapter(recyclerView.getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Retrieve data from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<RewardsSoftRecycleItem> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String rewardSoftPrice = "100 pts";
                    String rewardSoftName = snapshot.getKey();
                    String rewardSoftStatus = snapshot.child("reward_status").getValue(String.class);
                    String rewardSoftType = snapshot.child("reward_type").getValue(String.class);
                    String rewardSoftButton = "";

                    // Log.d("RewardsSoft", "Reward Name: " + rewardSoftName + ", Status: " + rewardSoftStatus);

                    if(!"available".equals(rewardSoftStatus)) {
                        rewardSoftButton = "owned";
                    }

                    else {
                        rewardSoftButton = "available";
                    }

                    // Check if badge status is not "unlocked" before adding it to the list
                    if("icon".equals(rewardSoftType)) {
                        int rewardDrawableId = getDrawableResourceId(rewardSoftName);
                        items.add(new RewardsSoftRecycleItem(rewardSoftName, rewardSoftPrice, rewardSoftStatus, rewardDrawableId, rewardSoftType, rewardSoftButton));
                    }
                }
                adapter.setItems(items); // Set items to the adapter after retrieving from Firebase
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });


    }


    private int getDrawableResourceId(String rewardName) {
        try {
            // Get the R.drawable class using reflection
            Class<?> drawableClass = R.drawable.class;

            // Get the Field object representing the rewardName in the R.drawable class
            Field field = drawableClass.getField(rewardName);

            // Get the value (drawable resource ID) of the field
            return field.getInt(null);
        } catch (NoSuchFieldException e) {
            // Handle the case where the rewardName is not found
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // Handle the case where access to the field is denied
            e.printStackTrace();
        }

        // Return 0 if rewardName is not recognized
        return 0;
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

    public void openRewardsCollectibles(View view) {
        startActivity(new Intent(this, RewardsCollectibles.class));
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

