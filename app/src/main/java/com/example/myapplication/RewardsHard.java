package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class RewardsHard extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView button;
    TextView textView;
    FirebaseUser user;
    DatabaseReference databaseReference;
    private Button btnShowDialog;
    RewardsHardRecycleAdapter recycleAdapter;
    RecyclerView recyclerView;
    List<RewardsHardRecycleItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_hard);

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

        // UPDATE PROFILE CODE
        btnShowDialog = findViewById(R.id.btn_create_reward);

        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardCreationPopUp();
            }
        });

        // LOGOUT BUTTON CODE
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            // Handle the case where the user is not logged in
            // You may want to redirect them to the login screen
        } else {

            databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                    .child(user.getUid())
                    .child("HardRewards");
        }
        button = findViewById(R.id.logout);


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

        // ADDING ITEMS TO RECYCLE VIEW
        recyclerView = findViewById(R.id.rewards_recyclerview);

        items = new ArrayList<>();
        items.add(new RewardsHardRecycleItem("Test", "Hi", "idk"));
        fetchHardRewardsFromDatabase();

        recycleAdapter = new RewardsHardRecycleAdapter(getApplicationContext(), items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recycleAdapter);

    }

    private void showRewardCreationPopUp() {
        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.activity_rewards_hard_create);

        EditText etRewardName = dialog.findViewById(R.id.reward_name_input);
        EditText etRewardDescription = dialog.findViewById(R.id.reward_description_input);
        EditText etDateCreated = dialog.findViewById(R.id.reward_date_created_input);



        Button btnSave = dialog.findViewById(R.id.saveTaskButton);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assuming you have EditText fields for rewardName, rewardDescription, and dateCreated


                String rewardName = etRewardName.getText().toString();
                String rewardDescription = etRewardDescription.getText().toString();
                String dateCreated = etDateCreated.getText().toString();

                // Validate the input if needed

                // Save the reward to Firebase
                saveRewardToFirebase(rewardName, rewardDescription, dateCreated);

                // Close the dialog after saving
                dialog.dismiss();
            }
        });
        // CLOSE BUTTON FOR POP UP CUSTOMIZATION
        ImageView btnClose = dialog.findViewById(R.id.exitCreateTaskButton);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void saveRewardToFirebase(String rewardName, String rewardDescription, String dateCreated) {
        if (user != null) {
            String uid = user.getUid();
            RewardsHardRecycleItem reward = new RewardsHardRecycleItem(rewardName, rewardDescription, dateCreated);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Registered Users").child(uid).child("HardRewards");
            String rewardId = ref.push().getKey(); // Generate a unique ID for the reward
            if (rewardId != null) {
                ref.child(rewardId).setValue(reward).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Reward created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to create Reward", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    private void fetchHardRewardsFromDatabase() {
        if (user != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    items.clear(); // Clear the existing list before adding new hard rewards

                    for (DataSnapshot rewardSnapshot : dataSnapshot.getChildren()) {
                        RewardsHardRecycleItem reward = rewardSnapshot.getValue(RewardsHardRecycleItem.class);

                        if (reward != null) {
                            // Assuming your RewardsHardRecycleItem class has appropriate fields
                            RewardsHardRecycleItem recycleItem = new RewardsHardRecycleItem(
                                    reward.getRewardName(),
                                    reward.getRewardDescription(),
                                    reward.getDateCreated()
                            );

                            items.add(recycleItem);
                        }
                    }

                    // Notify the adapter that the data set has changed
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("RewardsHard", "Failed to fetch hard rewards: " + databaseError.getMessage());
                    Toast.makeText(RewardsHard.this, "Failed to fetch hard rewards: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // START OF NAVIGATION CODE

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