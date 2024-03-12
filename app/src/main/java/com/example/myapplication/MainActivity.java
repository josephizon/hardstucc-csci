package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

// FOR THE GRADIENT
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

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView button, badgeChange1, badgeChange2, badgeChange3, userProfileIcon;
    TextView userNameTextView, userCoinsTextView, userBPLevelTextView;
    FirebaseUser user;
    DatabaseReference databaseReference, databaseBadges, databaseIcons;

    private DataSnapshot dataSnapshot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // LOGOUT BUTTON CODE
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        userNameTextView = findViewById(R.id.user_details);
        userCoinsTextView = findViewById(R.id.user_coins);
        userBPLevelTextView = findViewById(R.id.user_bplevel);
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        else {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Users userProfile = dataSnapshot.getValue(Users.class);
                        if (userProfile != null && userProfile.getFirstName() != null) {
                            userNameTextView.setText(userProfile.getFirstName());
                            userCoinsTextView.setText(userProfile.getCoins() + " pts");
                            userBPLevelTextView.setText("Level " + userProfile.getBpLevel());
                        } else {
                            userNameTextView.setText(user.getEmail());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });


            // Badges Reference
            databaseBadges = FirebaseDatabase.getInstance().getReference("Registered Users")
                    .child(user.getUid())
                    .child("Badges");

            // Add a listener to retrieve data from Firebase Database
            databaseBadges.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    dataSnapshot = snapshot;
                    for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                        String badgeName = badgeSnapshot.getKey(); // Badge name is the key
                        String badgeStatus = badgeSnapshot.child("badge_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

                        badgeChange1 = findViewById(R.id.displayed_badge_1);
                        badgeChange2 = findViewById(R.id.displayed_badge_2);
                        badgeChange3 = findViewById(R.id.displayed_badge_3);


                        // Check if badge status is not "locked" before adding it to the list
                        if (!"locked".equals(badgeStatus)) {
                            int badgeDrawableId = getDrawableResourceId(badgeName);

                            if ("displayed1".equals(badgeStatus)) {
                                badgeChange1.setImageResource(badgeDrawableId);
                            } else if ("displayed2".equals(badgeStatus)) {
                                badgeChange2.setImageResource(badgeDrawableId);
                            } else if ("displayed3".equals(badgeStatus)) {
                                badgeChange3.setImageResource(badgeDrawableId);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                }
            });

            // Icons Reference
            databaseIcons = FirebaseDatabase.getInstance().getReference("Registered Users")
                    .child(user.getUid())
                    .child("SoftRewards");

            // Add a listener to retrieve data from Firebase Database
            databaseIcons.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    dataSnapshot = snapshot;
                    for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                        String iconName = badgeSnapshot.getKey(); // Badge name is the key
                        String iconStatus = badgeSnapshot.child("reward_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

                        userProfileIcon = findViewById(R.id.user_icon);

                        int badgeDrawableId = getDrawableResourceId(iconName);

                        if ("displayedProfile".equals(iconStatus)) {
                            userProfileIcon.setImageResource(badgeDrawableId);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
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

}