package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Badges extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView button;
    TextView textView;
    FirebaseUser user;

    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // BADGES GRADIENT
        TextView badges = findViewById(R.id.title_badges);
        int orange = Color.rgb(255, 190, 92);
        int yellow = Color.rgb(255, 206, 49);
        Shader shader1 = new LinearGradient(0f, 0f, 0f, badges.getTextSize(), orange, yellow, Shader.TileMode.CLAMP);
        badges.getPaint().setShader(shader1);


        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        else {
        }


        // LOGOUT BUTTON CODE
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

        RecyclerView recyclerView = findViewById(R.id.activity_badges_recyclerview_1);

        List<BadgesRecycleItem> items = new ArrayList<BadgesRecycleItem>();
        items.add(new BadgesRecycleItem("unlocked",  R.drawable.badges_fire_icon, "badges_fire_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_folder_icon, "badges_folder_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_ok_icon, "badges_ok_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_heart_icon, "badges_heart_icon"));
        items.add(new BadgesRecycleItem("unlocked",  R.drawable.badges_shades_icon, "badges_shades_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_sun_icon, "badges_sun_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_warning_icon, "badges_warning_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_computer_icon, "badges_computer_icon" ));
        items.add(new BadgesRecycleItem("unlocked",  R.drawable.badges_hands_icon, "badges_hands_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_crying_icon, "badges_crying_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_gracias_icon, "badges_gracias_icon" ));
        items.add(new BadgesRecycleItem("unlocked", R.drawable.badges_thumbsup_icon, "badges_thumbsup_icon" ));


        // ORIGINAL LINEARLAYOUT MANAGER
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(new CustomLayoutManager());
        recyclerView.setAdapter(new BadgesRecycleAdapter(getApplicationContext(), items));*/

        // Set up Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
                .child("Badges");

        // Add a listener to retrieve data from Firebase Database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BadgesRecycleItem> items = new ArrayList<>();
                for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                    String badgeName = badgeSnapshot.getKey(); // Badge name is the key
                    String badgeStatus = badgeSnapshot.child("badge_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

                    // Check if badge status is not "unlocked" before adding it to the list
                    if (!"locked".equals(badgeStatus)) {
                        int badgeDrawableId = getDrawableResourceId(badgeName);
                        items.add(new BadgesRecycleItem(badgeStatus, badgeDrawableId, badgeName));
                    }

                }
                // Set up RecyclerView with the retrieved data
                RecyclerView recyclerView = findViewById(R.id.activity_badges_recyclerview_1);
                recyclerView.setLayoutManager(new CustomLayoutManager());
                recyclerView.setAdapter(new BadgesRecycleAdapter(getApplicationContext(), items));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        // Your existing code
    }



    // ORIGINAL CONVERTING STRING TO DRAWABLE FOR BADGES
    /*private int getDrawableResourceId(String badgeName) {
        // Implement logic to map badge names to drawable resource IDs
        // For example, you can use a switch statement or a HashMap
        switch (badgeName) {
            case "badges_fire_icon":
                return R.drawable.badges_fire_icon;
            case "badges_folder_icon":
                return R.drawable.badges_folder_icon;
            // Add more cases as needed for other badges
            default:
                return 0; // Return a default drawable resource ID if badge name is not recognized
        }
    }*/

    private int getDrawableResourceId(String badgeName) {
        try {
            // Get the R.drawable class using reflection
            Class<?> drawableClass = R.drawable.class;

            // Get the Field object representing the badgeName in the R.drawable class
            Field field = drawableClass.getField(badgeName);

            // Get the value (drawable resource ID) of the field
            return field.getInt(null);
        } catch (NoSuchFieldException e) {
            // Handle the case where the badgeName is not found
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // Handle the case where access to the field is denied
            e.printStackTrace();
        }

        // Return 0 if badgeName is not recognized
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

    public void openBuddyStore(View view) {
        startActivity(new Intent(this, BuddyRewardsSoft.class));
    }

    public void openBuddyBadges(View view) {
        startActivity(new Intent(this, BuddyBadges.class));
    }
}