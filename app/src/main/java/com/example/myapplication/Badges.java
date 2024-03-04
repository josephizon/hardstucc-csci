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
import android.graphics.Typeface;
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
    ImageView badgeChange1, badgeChange2, badgeChange3;
    TextView textView;
    FirebaseUser user;

    DatabaseReference databaseReference;

    private String previousDisplayedBadgeKey1, previousDisplayedBadgeKey2, previousDisplayedBadgeKey3;

    private DataSnapshot dataSnapshot;



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


        // Set up Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
                .child("Badges");

        // Add a listener to retrieve data from Firebase Database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dataSnapshot = snapshot;
                List<BadgesRecycleItem> items = new ArrayList<>();
                for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                    String badgeName = badgeSnapshot.getKey(); // Badge name is the key
                    String badgeStatus = badgeSnapshot.child("badge_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

                    badgeChange1 = findViewById(R.id.displayed_badge_1);
                    badgeChange2 = findViewById(R.id.displayed_badge_2);
                    badgeChange3 = findViewById(R.id.displayed_badge_3);


                    // Click listener for badgeChange1
                    badgeChange1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle badgeChange1 click
                            resetAndChangeBadge(previousDisplayedBadgeKey1, "displayed1");
                        }
                    });

                    // Click listener for badgeChange2
                    badgeChange2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle badgeChange2 click
                            resetAndChangeBadge(previousDisplayedBadgeKey2, "displayed2");
                        }
                    });

                    // Click listener for badgeChange3
                    badgeChange3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle badgeChange3 click
                            resetAndChangeBadge(previousDisplayedBadgeKey3, "displayed3");
                        }
                    });

                    // Check if badge status is not "locked" before adding it to the list
                    if (!"locked".equals(badgeStatus)) {
                        int badgeDrawableId = getDrawableResourceId(badgeName);
                        items.add(new BadgesRecycleItem(badgeStatus, badgeDrawableId, badgeName));

                        if ("displayed1".equals(badgeStatus)) {
                            badgeChange1.setImageResource(badgeDrawableId);
                            previousDisplayedBadgeKey1 = badgeSnapshot.getKey(); // Update previous displayed badge for badgeChange1
                        } else if ("displayed2".equals(badgeStatus)) {
                            badgeChange2.setImageResource(badgeDrawableId);
                            previousDisplayedBadgeKey2 = badgeSnapshot.getKey(); // Update previous displayed badge for badgeChange2
                        } else if ("displayed3".equals(badgeStatus)) {
                            badgeChange3.setImageResource(badgeDrawableId);
                            previousDisplayedBadgeKey3 = badgeSnapshot.getKey(); // Update previous displayed badge for badgeChange3
                        }
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

    // OLD CODE
    /*private void resetAndChangeBadge(String previousDisplayedBadgeKey, final String newBadgeStatus) {
        // Reset previous displayed badge
        if (previousDisplayedBadgeKey != null && !previousDisplayedBadgeKey.isEmpty()) {
            databaseReference.child(previousDisplayedBadgeKey).child("badge_status").setValue("unlocked");
        }

        // Create a list of the iconNames (badges list for selection)
        List<String> iconNames = new ArrayList<>();
        final List<String> badgeKeys = new ArrayList<>(); // To store the corresponding badge keys
        for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
            String badgeName = badgeSnapshot.getKey(); // Badge name is the key
            String badgeStatus = badgeSnapshot.child("badge_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

            if (!"locked".equals(badgeStatus)) {
                iconNames.add(badgeName);
                badgeKeys.add(badgeSnapshot.getKey()); // Store the corresponding badge key
            }
        }

        // Convert the list of icon names to an array
        final CharSequence[] iconsArray = iconNames.toArray(new CharSequence[0]);

        // Create an AlertDialog to display the list of icons
        AlertDialog.Builder builder = new AlertDialog.Builder(Badges.this);
        builder.setTitle("Available Icons")
                .setItems(iconsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the badge_status to the newBadgeStatus for the selected item
                        String selectedBadgeKey = badgeKeys.get(which);
                        databaseReference.child(selectedBadgeKey).child("badge_status").setValue(newBadgeStatus);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle click on OK button (optional)
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

    // Helper method to reset the previously displayed badge status to "unlocked" and set the new badge status
    /*private void resetAndChangeBadge(String previousDisplayedBadgeKey, final String newBadgeStatus) {
        // Reset previous displayed badge
        if (previousDisplayedBadgeKey != null && !previousDisplayedBadgeKey.isEmpty()) {
            databaseReference.child(previousDisplayedBadgeKey).child("badge_status").setValue("unlocked");
        }

        // Create a list of the iconNames (badges list for selection)
        List<String> iconNames = new ArrayList<>();
        final List<String> badgeKeys = new ArrayList<>(); // To store the corresponding badge keys
        for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
            String badgeName = badgeSnapshot.getKey(); // Badge name is the key
            String badgeStatus = badgeSnapshot.child("badge_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

            if (!"locked".equals(badgeStatus)) {
                iconNames.add(badgeName);
                badgeKeys.add(badgeSnapshot.getKey()); // Store the corresponding badge key
            }
        }

        // Add the option to unlock all badges
        iconNames.add("Clear badges from display");



        // Convert the list of icon names to an array
        final CharSequence[] iconsArray = iconNames.toArray(new CharSequence[0]);

        // Create an AlertDialog to display the list of icons
        AlertDialog.Builder builder = new AlertDialog.Builder(Badges.this);
        builder.setTitle("Available Icons")
                .setItems(iconsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < iconNames.size() - 1) {
                            // Update the badge_status to the newBadgeStatus for the selected item
                            String selectedBadgeKey = badgeKeys.get(which);
                            databaseReference.child(selectedBadgeKey).child("badge_status").setValue(newBadgeStatus);
                        } else {
                            // Unlock all badges
                            for (String key : badgeKeys) {
                                databaseReference.child(key).child("badge_status").setValue("unlocked");
                            }
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle click on OK button (optional)
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

    // resetAndChange method that uses a custom Badge Image Adapter for layout
    private void resetAndChangeBadge(String previousDisplayedBadgeKey, final String newBadgeStatus) {
        // Reset previous displayed badge
        if (previousDisplayedBadgeKey != null && !previousDisplayedBadgeKey.isEmpty()) {
            databaseReference.child(previousDisplayedBadgeKey).child("badge_status").setValue("unlocked");
        }

        // Create a list of the iconNames (badges list for selection)
        List<String> iconNames = new ArrayList<>();
        final List<String> badgeKeys = new ArrayList<>(); // To store the corresponding badge keys
        final List<Integer> badgeImages = new ArrayList<>(); // To store the corresponding badge images
        for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
            String badgeName = badgeSnapshot.getKey(); // Badge name is the key
            String badgeStatus = badgeSnapshot.child("badge_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

            if (!"locked".equals(badgeStatus)) {
                iconNames.add(badgeName);
                badgeKeys.add(badgeSnapshot.getKey()); // Store the corresponding badge key
                int badgeDrawableId = getDrawableResourceId(badgeName);
                badgeImages.add(badgeDrawableId); // Store the corresponding badge image
            }
        }

        // Convert the list of icon names to an array
        final CharSequence[] iconsArray = iconNames.toArray(new CharSequence[0]);

        // Create an AlertDialog to display the list of icons
        AlertDialog.Builder builder = new AlertDialog.Builder(Badges.this);
        builder.setTitle("UNLOCKED BADGES")
                .setItems(iconsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < badgeKeys.size()) {
                            // Update the badge_status to the newBadgeStatus for the selected item
                            String selectedBadgeKey = badgeKeys.get(which);
                            databaseReference.child(selectedBadgeKey).child("badge_status").setValue(newBadgeStatus);
                        }
                    }
                });

        // Add images to the dialog using BadgeImageAdapter
        builder.setAdapter(new BadgeImageAdapter(this, badgeImages, badgeKeys), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle click on badge image
                String selectedBadgeKey = badgeKeys.get(which);
                databaseReference.child(selectedBadgeKey).child("badge_status").setValue(newBadgeStatus);
            }
        });

        // Add a button to clear displayed badges
        builder.setNegativeButton("Clear Displayed Badges", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clear badges from display
                for (String key : badgeKeys) {
                    databaseReference.child(key).child("badge_status").setValue("unlocked");
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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