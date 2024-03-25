package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView button;
    FirebaseUser user;
    DatabaseReference userRef, expRef, badgeRef;
    String buddyUid;
    RecyclerView recyclerView;
    List<LeaderboardRecycleItem> leaderboardItems;
    LeaderboardRecycleAdapter leaderboardRecycleAdapter;
    int leader_icon, leader_collectible_1, leader_collectible_2, leader_collectible_3, leader_badge_1, leader_badge_2, leader_badge_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy_leaderboard);
// Set up Firebase Database reference
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
            recyclerView = findViewById(R.id.leaderboard_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // Set the layout manager
            leaderboardItems = new ArrayList<>();
            leaderboardRecycleAdapter = new LeaderboardRecycleAdapter(getApplicationContext(), leaderboardItems); // Initialize the adapter
            recyclerView.setAdapter(leaderboardRecycleAdapter); // Set the adapter

            expRef = FirebaseDatabase.getInstance().getReference("Registered Users");

            expRef.orderByChild("bpLevel").limitToLast(13).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // List to hold top 10 users
                        List<Users> topUsers = new ArrayList<>();

                        // Iterate through the top 10 users
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Users user = snapshot.getValue(Users.class);
                            if (user != null && !snapshot.child("selectedClass").getValue().equals("ADMIN")){
                                topUsers.add(user);
                            }
                            else {

                            }
                        }

                        // Sort the topUsers list based on bpLevel and exp
                        Collections.sort(topUsers, new Comparator<Users>() {
                            @Override
                            public int compare(Users user1, Users user2) {
                                // Sort in descending order of bpLevel
                                int bpComparison = Integer.compare(user2.getBpLevel(), user1.getBpLevel());
                                if (bpComparison != 0) {
                                    // If bpLevels are different, return the comparison result
                                    return bpComparison;
                                } else {
                                    // If bpLevels are the same, compare exp levels
                                    return Integer.compare(user2.getExp(), user1.getExp()); // Higher exp ranks higher
                                }
                            }
                        });


                        // Populate leaderboardItems with top 10 users
                        leaderboardItems.clear();
                        int rank = 1;
                        for (Users user : topUsers) {
                            String fullName = user.getFirstName() + " " + user.getLastName();
                            AssignDisplayables(fullName);
                            leaderboardItems.add(new LeaderboardRecycleItem("Rank " + rank, fullName, "BP LEVEL: " + String.valueOf(user.getBpLevel()),
                                    leader_icon, leader_collectible_1, leader_collectible_2, leader_collectible_3, leader_badge_1, leader_badge_2, leader_badge_3));
                            rank++;
                        }

                        leaderboardRecycleAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled
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
    private void AssignDisplayables(String fullName) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Registered Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                    String lastName = userSnapshot.child("lastName").getValue(String.class);

                    // Construct the full name from the snapshot
                    String userFullName = firstName + " " + lastName;
                    if (fullName.trim().equalsIgnoreCase(userFullName.trim())) {
                        // Found the user, now create a task for them
                        String userId = userSnapshot.getKey(); // The user's ID
                        badgeRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                                .child(userId)
                                .child("Badges");
                        for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                            String badgeName = badgeSnapshot.getKey(); // Badge name is the key
                            String badgeStatus = badgeSnapshot.child("badge_status").getValue(String.class); // Badge status is retrieved from "badge_status" child

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("createTaskForIndiv", "Error: ", databaseError.toException());
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

    public void openLeaderboard(View view) {
        startActivity(new Intent(this, Leaderboard.class));
    }


}