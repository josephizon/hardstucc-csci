package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Profile extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView button;
    FirebaseUser user;
    TextView profileUsernameTextView;

    private Button btnShowDialog;

    DatabaseReference databaseCollectibleReference, databaseBadges;

    private DataSnapshot dataSnapshot;
    ImageView collectibleChange1, collectibleChange2, collectibleChange3, collectibleChange4;
    ImageView collectibleChange5, collectibleChange6, collectibleChange7, collectibleChange8;
    ImageView profileIcon;
    private String previouslyDisplayedCollectible1, previouslyDisplayedCollectible2, previouslyDisplayedCollectible3, previouslyDisplayedCollectible4;
    private String previouslyDisplayedCollectible5, previouslyDisplayedCollectible6, previouslyDisplayedCollectible7, previouslyDisplayedCollectible8;
    private String previouslyDisplayedProfile;

    Dialog customizationDialog;

    private ImageView popupProfileIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // RECENT ACTIVITY GRADIENT
        TextView recentActivity = findViewById(R.id.title_recentActivity);
        int orange = Color.rgb(255, 190, 92);
        int yellow = Color.rgb(255, 206, 49);
        Shader shader1 = new LinearGradient(0f, 0f, 0f, recentActivity.getTextSize(), orange, yellow, Shader.TileMode.CLAMP);
        recentActivity.getPaint().setShader(shader1);

        // UPDATE PROFILE CODE
        btnShowDialog = findViewById(R.id.btn_update_profile);

        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        // PROFILE USERNAME TEXTVIEW
        profileUsernameTextView = findViewById(R.id.profile_username);

        // LOGOUT BUTTON CODE
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // Retrieve user information from Firebase Database and set in the TextView
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                    .child(user.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Users userProfile = dataSnapshot.getValue(Users.class);
                        if (userProfile != null) {
                            // Concatenate first name and last name and set it in the TextView
                            String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
                            profileUsernameTextView.setText(fullName);
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

        // Set up Firebase Database reference
        databaseCollectibleReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
                .child("SoftRewards");

        // Add a listener to retrieve data from Firebase Database
        databaseCollectibleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dataSnapshot = snapshot;
                // List<BadgesRecycleItem> items = new ArrayList<>();
                for (DataSnapshot collectibleSnapshot : dataSnapshot.getChildren()) {
                    String collectibleName = collectibleSnapshot.getKey(); // Badge name is the key
                    String collectibleStatus = collectibleSnapshot.child("reward_status").getValue(String.class);

                    collectibleChange1 = findViewById(R.id.displayed_collectible_1);
                    collectibleChange2 = findViewById(R.id.displayed_collectible_2);
                    collectibleChange3 = findViewById(R.id.displayed_collectible_3);
                    collectibleChange4 = findViewById(R.id.displayed_collectible_4);
                    collectibleChange5 = findViewById(R.id.displayed_collectible_5);
                    collectibleChange6 = findViewById(R.id.displayed_collectible_6);
                    collectibleChange7 = findViewById(R.id.displayed_collectible_7);
                    collectibleChange8 = findViewById(R.id.displayed_collectible_8);

                    profileIcon = findViewById(R.id.user_icon);

                    // Click listener for badgeChange1
                    collectibleChange1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle badgeChange1 click
                            resetAndChangeCollectible(previouslyDisplayedCollectible1, "displayed1");
                        }
                    });

                    // Click listener for badgeChange2
                    collectibleChange2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle badgeChange2 click
                            resetAndChangeCollectible(previouslyDisplayedCollectible2, "displayed2");
                        }
                    });

                    // Click listener for badgeChange3
                    collectibleChange3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle badgeChange3 click
                            resetAndChangeCollectible(previouslyDisplayedCollectible3, "displayed3");
                        }
                    });

                    // Click listener for badgeChange4
                    collectibleChange4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetAndChangeCollectible(previouslyDisplayedCollectible4, "displayed4");
                        }
                    });

                    // Click listener for badgeChange5
                    collectibleChange5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetAndChangeCollectible(previouslyDisplayedCollectible5, "displayed5");
                        }
                    });

                    // Click listener for badgeChange6
                    collectibleChange6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetAndChangeCollectible(previouslyDisplayedCollectible6, "displayed6");
                        }
                    });

                    // Click listener for badgeChange7
                    collectibleChange7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetAndChangeCollectible(previouslyDisplayedCollectible7, "displayed7");
                        }
                    });

                    // Click listener for badgeChange8
                    collectibleChange8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetAndChangeCollectible(previouslyDisplayedCollectible8, "displayed8");
                        }
                    });

                    /*profileIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetAndChangeIcon(previouslyDisplayedProfile, "displayedProfile");
                        }
                    });*/



                    // Check if badge status is not "locked" before adding it to the list

                    int badgeDrawableId = getDrawableResourceId(collectibleName);
                        // items.add(new BadgesRecycleItem(badgeStatus, badgeDrawableId, badgeName));

                    if ("displayed1".equals(collectibleStatus)) {
                        collectibleChange1.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible1 = collectibleSnapshot.getKey();
                    }

                    else if ("displayed2".equals(collectibleStatus)) {
                        collectibleChange2.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible2 = collectibleSnapshot.getKey();
                    }

                    else if ("displayed3".equals(collectibleStatus)) {
                        collectibleChange3.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible3 = collectibleSnapshot.getKey();
                    }

                    else if ("displayed4".equals(collectibleStatus)) {
                        collectibleChange4.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible4 = collectibleSnapshot.getKey();
                    }

                    else if ("displayed5".equals(collectibleStatus)) {
                        collectibleChange5.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible5 = collectibleSnapshot.getKey();
                    }

                    else if ("displayed6".equals(collectibleStatus)) {
                        collectibleChange6.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible6 = collectibleSnapshot.getKey();
                    }

                    else if ("displayed7".equals(collectibleStatus)) {
                        collectibleChange7.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible7 = collectibleSnapshot.getKey();
                    }

                    else if ("displayed8".equals(collectibleStatus)) {
                        collectibleChange8.setImageResource(badgeDrawableId);
                        previouslyDisplayedCollectible8 = collectibleSnapshot.getKey();
                    }

                    else if ("displayedProfile".equals(collectibleStatus)) {
                        profileIcon.setImageResource(badgeDrawableId);
                        previouslyDisplayedProfile = collectibleSnapshot.getKey();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
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

    private void showDialog() {
        customizationDialog = new Dialog(this, R.style.DialogStyle);
        customizationDialog.setContentView(R.layout.popup_profile_customization);
        Button btnSave = customizationDialog.findViewById(R.id.btn_register);

        // Find the ImageView for profile icon in the popup layout
        popupProfileIcon = customizationDialog.findViewById(R.id.user_icon);

        // Set onClickListener for profile icon in customization dialog
        popupProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAndChangeIcon(previouslyDisplayedProfile, "displayedProfile");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assuming you have EditText fields for first name, last name, and middle name
                EditText etFirstName = customizationDialog.findViewById(R.id.edit_first_name);
                EditText etLastName = customizationDialog.findViewById(R.id.edit_last_name);
                EditText etMiddleName = customizationDialog.findViewById(R.id.edit_middle_name);

                String newFirstName = etFirstName.getText().toString().trim();
                String newLastName = etLastName.getText().toString().trim();
                String newMiddleName = etMiddleName.getText().toString().trim();

                // Validate the input if needed

                // Save the changes to Firebase
                saveProfileChanges(customizationDialog, newFirstName, newLastName, newMiddleName);
            }
        });
        // CLOSE BUTTON FOR POP UP CUSTOMIZATION
        ImageView btnClose = customizationDialog.findViewById(R.id.popup_exit_icon);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customizationDialog.dismiss();
            }
        });

        customizationDialog.show();
    }

    private void saveProfileChanges(Dialog dialog, String newFirstName, String newLastName, String newMiddleName) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid());

        userRef.child("firstName").setValue(newFirstName);
        userRef.child("lastName").setValue(newLastName);
        userRef.child("middleName").setValue(newMiddleName);

        // Update the TextView with the new full name
        String newFullName = newFirstName + " " + newLastName;
        profileUsernameTextView.setText(newFullName);

        // Close the dialog after saving
        dialog.dismiss();
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


    // resetAndChange method that uses a custom Badge Image Adapter for layout
    private void resetAndChangeCollectible(String previouslyDisplayedCollectible, final String newCollectibleStatus) {
        // Reset previous displayed badge
        if (previouslyDisplayedCollectible != null && !previouslyDisplayedCollectible.isEmpty()) {
            databaseCollectibleReference.child(previouslyDisplayedCollectible).child("reward_status").setValue("owned");
        }

        // Create a list of the iconNames (badges list for selection)
        List<String> iconNames = new ArrayList<>();
        final List<String> collectibleKeys = new ArrayList<>(); // To store the corresponding badge keys
        final List<Integer> collectibleImages = new ArrayList<>(); // To store the corresponding badge images
        for (DataSnapshot collectibleSnapshot : dataSnapshot.getChildren()) {
            String collectibleName = collectibleSnapshot.getKey(); // Badge name is the key
            String collectibleStatus = collectibleSnapshot.child("reward_status").getValue(String.class); // Badge status is retrieved from "badge_status" child
            String collectibleType = collectibleSnapshot.child("reward_type").getValue(String.class);

            if (!"available".equals(collectibleStatus) && "collectible".equals(collectibleType)) {
                iconNames.add(collectibleName);
                collectibleKeys.add(collectibleSnapshot.getKey()); // Store the corresponding badge key
                int collectibleDrawableId = getDrawableResourceId(collectibleName);
                collectibleImages.add(collectibleDrawableId); // Store the corresponding badge image


            }


        }

        // Convert the list of modified names to an array
        final CharSequence[] iconsArray = iconNames.toArray(new CharSequence[0]);

        // Create an AlertDialog to display the list of icons
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Purchased Collectibles");
                /*.setItems(iconsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < badgeKeys.size()) {
                            // Update the badge_status to the newBadgeStatus for the selected item
                            String selectedBadgeKey = badgeKeys.get(which);
                            databaseReference.child(selectedBadgeKey).child("badge_status").setValue(newBadgeStatus);
                        }
                    }
                });*/

        // Add images to the dialog using BadgeImageAdapter
        builder.setAdapter(new BadgeImageAdapter(this, collectibleImages, collectibleKeys), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle click on badge image
                String selectedCollectibleKey = collectibleKeys.get(which);
                databaseCollectibleReference.child(selectedCollectibleKey).child("reward_status").setValue(newCollectibleStatus);
                reloadActivity();
            }
        });

        // Add a button to clear displayed badges
        builder.setNegativeButton("Clear Displayed Collectibles", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clear badges from display
                for (String key : collectibleKeys) {
                    databaseCollectibleReference.child(key).child("reward_status").setValue("owned");
                    reloadActivity();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void resetAndChangeIcon(String previouslyDisplayedProfile, final String newCollectibleStatus) {
        // Reset previous displayed badge
        if (previouslyDisplayedProfile != null && !previouslyDisplayedProfile.isEmpty()) {
            databaseCollectibleReference.child(previouslyDisplayedProfile).child("reward_status").setValue("owned");
        }

        // Create a list of the iconNames (badges list for selection)
        List<String> iconNames = new ArrayList<>();
        final List<String> collectibleKeys = new ArrayList<>(); // To store the corresponding badge keys
        final List<Integer> collectibleImages = new ArrayList<>(); // To store the corresponding badge images
        for (DataSnapshot collectibleSnapshot : dataSnapshot.getChildren()) {
            String collectibleName = collectibleSnapshot.getKey(); // Badge name is the key
            String collectibleStatus = collectibleSnapshot.child("reward_status").getValue(String.class); // Badge status is retrieved from "badge_status" child
            String collectibleType = collectibleSnapshot.child("reward_type").getValue(String.class);

            if (!"available".equals(collectibleStatus) && "icon".equals(collectibleType)) {
                iconNames.add(collectibleName);
                collectibleKeys.add(collectibleSnapshot.getKey()); // Store the corresponding badge key
                int collectibleDrawableId = getDrawableResourceId(collectibleName);
                collectibleImages.add(collectibleDrawableId); // Store the corresponding badge image
            }
        }

        // Convert the list of modified names to an array
        final CharSequence[] iconsArray = iconNames.toArray(new CharSequence[0]);

        // Create an AlertDialog to display the list of icons
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Purchased Profile Icons");

        // Add images to the dialog using BadgeImageAdapter
        builder.setAdapter(new BadgeImageAdapter(this, collectibleImages, collectibleKeys), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle click on badge image
                String selectedCollectibleKey = collectibleKeys.get(which);
                databaseCollectibleReference.child(selectedCollectibleKey).child("reward_status").setValue(newCollectibleStatus);
                reloadActivity();


            }
        });

        // Add a button to clear displayed badges
        builder.setNegativeButton("Clear Displayed Collectibles", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clear badges from display
                for (String key : collectibleKeys) {
                    databaseCollectibleReference.child(key).child("reward_status").setValue("owned");
                    reloadActivity();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    // Method to reload the activity
    private void reloadActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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

        databaseBadges = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(user.getUid())
                .child("Badges")
                .child("badges_buddy_click")
                .child("badge_status"); // Adjust path as necessary

        databaseBadges.setValue("unlocked");

        startActivity(new Intent(this, BuddyProfile.class));
    }

}