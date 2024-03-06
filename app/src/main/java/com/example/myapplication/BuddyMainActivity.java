package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

public class BuddyMainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView button;
    TextView textView;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy_main);

        // BATTLEPLAN GRADIENT
        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(255, 190, 92);
        int endColor = Color.rgb(255, 206, 49);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        // LOGOUT BUTTON CODE
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // Retrieve buddy information
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Users userProfile = dataSnapshot.getValue(Users.class);
                        String buddyUid = userProfile.getBuddyUid();
                        if (buddyUid != null) {
                            // Retrieve buddy's information
                            databaseReference.child(buddyUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot buddySnapshot) {
                                    if (buddySnapshot.exists()) {
                                        Users buddyProfile = buddySnapshot.getValue(Users.class);
                                        if (buddyProfile != null && buddyProfile.getFirstName() != null) {
                                            textView.setText( buddyProfile.getFirstName() + "!");
                                        } else {
                                            textView.setText( user.getEmail());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle errors
                                }
                            });
                        } else {
                            // No buddy information available
                            textView.setText("BUDDY VIEW\n" + user.getEmail());
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

    public void openBadges(View view) {
        startActivity(new Intent(this, Badges.class));
    }

    public void openStore(View view) {
        startActivity(new Intent(this, RewardsSoft.class));
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