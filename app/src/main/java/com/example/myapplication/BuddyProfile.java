package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

public class BuddyProfile extends AppCompatActivity {

        FirebaseAuth auth;
        ImageView button;
        FirebaseUser user;
        TextView profileUsernameTextView;
        private Button btnShowDialog;

        String buddyUid;

        DatabaseReference databaseTasksReference, databaseCollectibleReference;

        private DataSnapshot dataSnapshot;
        ImageView collectibleChange1, collectibleChange2, collectibleChange3, collectibleChange4;
        ImageView collectibleChange5, collectibleChange6, collectibleChange7, collectibleChange8;
        ImageView profileIcon;
        Dialog customizationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy_profile);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // RECENT ACTIVITY GRADIENT
        TextView recentActivity = findViewById(R.id.title_recentActivity);
        int blue = Color.rgb(50, 61, 115);
        int purple = Color.rgb(94, 132, 243);
        Shader shader1 = new LinearGradient(0f, 0f, 0f, recentActivity.getTextSize(), blue, purple, Shader.TileMode.CLAMP);
        recentActivity.getPaint().setShader(shader1);

        profileUsernameTextView = findViewById(R.id.profile_username);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                    .child(user.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Users userProfile = dataSnapshot.getValue(Users.class);
                        if (userProfile != null) {
                            buddyUid = userProfile.getBuddyUid();

                            if (buddyUid != null && !buddyUid.isEmpty()) {
                                DatabaseReference buddyRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                                        .child(buddyUid);

                                buddyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Users buddyProfile = dataSnapshot.getValue(Users.class);
                                            if (buddyProfile != null && buddyProfile.getFirstName() != null) {
                                                profileUsernameTextView.setText(buddyProfile.getFirstName() + " " + buddyProfile.getLastName());
                                            } else {
                                                profileUsernameTextView.setText("Default Buddy Name");
                                            }
                                        } else {
                                            profileUsernameTextView.setText("No buddy information available");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle errors
                                    }
                                });
                            } else {
                                profileUsernameTextView.setText("No buddy information available");
                            }


                            // START OF OTHER REFERENCES
                            databaseTasksReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                                    .child(buddyUid)
                                    .child("Tasks");
                            // Assuming these are your task counter TextViews, replace them with your actual TextViews
                            TextView dailyToBeAccomplishedCounter = findViewById(R.id.daily_to_be_accomplished_count);
                            TextView dailyPendingCounter = findViewById(R.id.daily_pending_count);
                            TextView dailyAccomplishedCounter = findViewById(R.id.daily_accomplished_count);

                            TextView majorToBeAccomplishedCounter = findViewById(R.id.major_to_be_accomplished_count);
                            TextView majorPendingCounter = findViewById(R.id.major_pending_count);
                            TextView majorAccomplishedCounter = findViewById(R.id.major_accomplished_count);

                            // ...

                            // Inside your databaseTasksReference listener
                            databaseTasksReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    // Initialize counters
                                    int dailyToBeAccomplished = 0;
                                    int dailyPending = 0;
                                    int dailyAccomplished = 0;

                                    int majorToBeAccomplished = 0;
                                    int majorPending = 0;
                                    int majorAccomplished = 0;

                                    // Loop through tasks under the user's UID
                                    for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                                        // Assuming there are fields named "type" and "status" in your task model
                                        String type = taskSnapshot.child("type").getValue(String.class);
                                        String status = taskSnapshot.child("status").getValue(String.class);

                                        // Update counters based on type and status
                                        if ("Daily".equals(type)) {
                                            if ("To be Accomplished".equals(status)) {
                                                dailyToBeAccomplished++;
                                            } else if ("Pending".equals(status)) {
                                                dailyPending++;
                                            } else if ("Accomplished".equals(status)) {
                                                dailyAccomplished++;
                                            }
                                        } else if ("Major".equals(type)) {
                                            if ("To be Accomplished".equals(status)) {
                                                majorToBeAccomplished++;
                                            } else if ("Pending".equals(status)) {
                                                majorPending++;
                                            } else if ("Accomplished".equals(status)) {
                                                majorAccomplished++;
                                            }
                                        }
                                    }

                                    // Display the results in the respective TextViews
                                    displayTaskCounts(dailyToBeAccomplished, dailyPending, dailyAccomplished,
                                            majorToBeAccomplished, majorPending, majorAccomplished);

                                    // Update your TextViews with the actual values
                                    dailyToBeAccomplishedCounter.setText("Daily Tasks: " + dailyToBeAccomplished);
                                    dailyPendingCounter.setText("Pending: " + dailyPending);
                                    dailyAccomplishedCounter.setText("Accomplished: " + dailyAccomplished);

                                    majorToBeAccomplishedCounter.setText("Major Tasks: " + majorToBeAccomplished);
                                    majorPendingCounter.setText("Pending: " + majorPending);
                                    majorAccomplishedCounter.setText("Accomplished: " + majorAccomplished);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle errors
                                }
                            });

                            // COLLECTIBLE REFERENCE

                            // Badges Reference
                            databaseCollectibleReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                                    .child(buddyUid)
                                    .child("SoftRewards");

                            // Add a listener to retrieve data from Firebase Database
                            databaseCollectibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    DataSnapshot dataSnapshot = snapshot;
                                    for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                                        String collectibleName = badgeSnapshot.getKey(); // Badge name is the key
                                        String collectibleStatus = badgeSnapshot.child("reward_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

                                        collectibleChange1 = findViewById(R.id.displayed_collectible_1);
                                        collectibleChange2 = findViewById(R.id.displayed_collectible_2);
                                        collectibleChange3 = findViewById(R.id.displayed_collectible_3);
                                        collectibleChange4 = findViewById(R.id.displayed_collectible_4);

                                        collectibleChange5 = findViewById(R.id.displayed_collectible_5);
                                        collectibleChange6 = findViewById(R.id.displayed_collectible_6);
                                        collectibleChange7 = findViewById(R.id.displayed_collectible_7);
                                        collectibleChange8 = findViewById(R.id.displayed_collectible_8);

                                        profileIcon = findViewById(R.id.user_icon);


                                        // Check if badge status is not "locked" before adding it to the list
                                        if (!"locked".equals(collectibleStatus)) {
                                            int badgeDrawableId = getDrawableResourceId(collectibleName);

                                            if ("displayed1".equals(collectibleStatus)) {
                                                collectibleChange1.setImageResource(badgeDrawableId);
                                            }
                                            else if ("displayed2".equals(collectibleStatus)) {
                                                collectibleChange2.setImageResource(badgeDrawableId);
                                            }
                                            else if ("displayed3".equals(collectibleStatus)) {
                                                collectibleChange3.setImageResource(badgeDrawableId);
                                            }
                                            else if ("displayed4".equals(collectibleStatus)) {
                                                collectibleChange4.setImageResource(badgeDrawableId);
                                            }
                                            else if ("displayed5".equals(collectibleStatus)) {
                                                collectibleChange5.setImageResource(badgeDrawableId);
                                            }
                                            else if ("displayed6".equals(collectibleStatus)) {
                                                collectibleChange6.setImageResource(badgeDrawableId);
                                            }
                                            else if ("displayed7".equals(collectibleStatus)) {
                                                collectibleChange7.setImageResource(badgeDrawableId);
                                            }
                                            else if ("displayed8".equals(collectibleStatus)) {
                                                collectibleChange8.setImageResource(badgeDrawableId);
                                            } else if ("displayedProfile".equals(collectibleStatus)) {
                                                profileIcon.setImageResource(badgeDrawableId);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle errors
                                }
                            });

                        }
                    }
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
    private void displayTaskCounts(int dailyToBeAccomplished, int dailyPending, int dailyAccomplished,
                                   int majorToBeAccomplished, int majorPending, int majorAccomplished) {
        // Assuming you have TextViews to display the counts
        TextView dailyToBeAccomplishedTextView = findViewById(R.id.daily_to_be_accomplished_count);
        TextView dailyPendingTextView = findViewById(R.id.daily_pending_count);
        TextView dailyAccomplishedTextView = findViewById(R.id.daily_accomplished_count);

        TextView majorToBeAccomplishedTextView = findViewById(R.id.major_to_be_accomplished_count);
        TextView majorPendingTextView = findViewById(R.id.major_pending_count);
        TextView majorAccomplishedTextView = findViewById(R.id.major_accomplished_count);

        // Set the counts in the respective TextViews
        dailyToBeAccomplishedTextView.setText(dailyToBeAccomplished + " To be Accomplished");
        dailyPendingTextView.setText(dailyPending + " Pending Tasks");
        dailyAccomplishedTextView.setText(dailyAccomplished + " Accomplished Tasks");

        majorToBeAccomplishedTextView.setText(majorToBeAccomplished + " To be Accomplished");
        majorPendingTextView.setText(majorPending + " Pending Tasks");
        majorAccomplishedTextView.setText(majorAccomplished + " Accomplished Tasks");
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