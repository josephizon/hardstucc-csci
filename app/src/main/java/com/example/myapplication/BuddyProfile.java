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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class BuddyProfile extends AppCompatActivity {

        FirebaseAuth auth;
        Button button;
        FirebaseUser user;
        TextView profileUsernameTextView;

    private Button btnShowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy_profile);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(255, 190, 92);
        int endColor = Color.rgb(255, 206, 49);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // RECENT ACTIVITY GRADIENT
        TextView recentActivity = findViewById(R.id.title_recentActivity);
        int blue = Color.rgb(50, 61, 115);
        int purple = Color.rgb(94, 132, 243);
        Shader shader1 = new LinearGradient(0f, 0f, 0f, recentActivity.getTextSize(), blue, purple, Shader.TileMode.CLAMP);
        recentActivity.getPaint().setShader(shader1);

        // UPDATE PROFILE CODE
        btnShowDialog = findViewById(R.id.btn_update_profile);

        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

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
                            String buddyUid = userProfile.getBuddyUid();

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

    public void openMainActivity2(View view) {
        startActivity(new Intent(this, MainActivity2.class));
    }

    public void openMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openTasks(View view) {
        startActivity(new Intent(this, Tasks.class));
    }

    public void openProfile(View view) {
        startActivity(new Intent(this, Profile.class));
    }

    public void openBattlePass(View view) {
        startActivity(new Intent(this, BattlePass.class));
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.popup_profile_customization);
        dialog.show();
    }

    // BUDDY NAVIGATION
    public void openBuddyMainActivity2(View view) {
        startActivity(new Intent(this, BuddyMainActivity2.class));
    }

    public void openBuddyMainActivity(View view) {
        startActivity(new Intent(this, BuddyMainActivity.class));
    }

    public void openBuddyTasks(View view) {
        startActivity(new Intent(this, BuddyTasks.class));
    }

    public void openBuddyBattlePass(View view) {
        startActivity(new Intent(this, BuddyBattlePass.class));
    }

    public void openBuddyProfile(View view) {
        startActivity(new Intent(this, BuddyProfile.class));
    }

    public void openBuddyStore(View view) {
        startActivity(new Intent(this, BuddyStore.class));
    }

    public void openBuddyBadges(View view) {
        startActivity(new Intent(this, BuddyBadges.class));
    }
}