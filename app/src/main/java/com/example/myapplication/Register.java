package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextFirstName, editTextMiddleName, editTextLastName;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    DatabaseReference referenceUsers, userReference;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // reload();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView name = findViewById(R.id.battle);
        int startColor = Color.rgb(50, 61, 115);
        int endColor = Color.rgb(94, 132, 243);
        Shader shader = new LinearGradient(0f, 0f, 0f, name.getTextSize(), startColor, endColor, Shader.TileMode.CLAMP);
        name.getPaint().setShader(shader);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextFirstName = findViewById(R.id.firstName);
        editTextMiddleName = findViewById(R.id.middleName);
        editTextLastName = findViewById(R.id.lastName);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, firstName, middleName, lastName;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                firstName = String.valueOf(editTextFirstName.getText());
                middleName = String.valueOf(editTextMiddleName.getText());
                lastName = String.valueOf(editTextLastName.getText());
                // Checking if email and password are empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    // Log.d(TAG, "createUserWithEmail:success");
                                    // FirebaseUser user = mAuth.getCurrentUser();
                                    // updateUI(user);

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                    Users user = new Users(email, firstName, middleName, lastName);

                                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                                    referenceProfile.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                addBadgeData(firebaseUser.getUid());
                                                addIconData(firebaseUser.getUid());
                                                Toast.makeText(Register.this, "Account created.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(Register.this, "DatabaseFailed.",
                                                        Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(view.GONE);
                                            }
                                        }
                                    });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }
                            }
                        });
            }
        });


    }

    private void addBadgeData(String userId) {
        List<String> badgeNames = new ArrayList<>();
        badgeNames.add("badges_fire_icon");
        badgeNames.add("badges_folder_icon");
        badgeNames.add("badges_ok_icon");
        badgeNames.add("badges_heart_icon");

        badgeNames.add("badges_shades_icon");
        badgeNames.add("badges_sun_icon");
        badgeNames.add("badges_warning_icon");
        badgeNames.add("badges_computer_icon");

        badgeNames.add("badges_hands_icon");
        badgeNames.add("badges_crying_icon");
        badgeNames.add("badges_gracias_icon");
        badgeNames.add("badges_folder_icon");


        // Define the structure of the badge data
        Map<String, Object> badgeData = new HashMap<>();
        badgeData.put("badge_status", "locked"); // Set status for all badges

        // Create a reference to the "Registered Users" node
        DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("Registered Users");
        DatabaseReference userReference = referenceUsers.child(userId).child("Badges");

        // Iterate over each badge name and set the data under the user's node for each badge
        for (String badgeName : badgeNames) {
            userReference.child(badgeName).setValue(badgeData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        /*Toast.makeText(Register.this, "Badge data added for " + badgeName,
                                Toast.LENGTH_SHORT).show();*/
                    }
                    else{
                        /*Toast.makeText(Register.this, "Failed to add badge data for " + badgeName,
                                Toast.LENGTH_SHORT).show();*/
                    }
                }
            });
        }
    }

    private void addIconData(String userId) {
        List<String> rewardNames = new ArrayList<>();
        rewardNames.add("rewards_profile_icon_1");
        rewardNames.add("rewards_profile_icon_2");
        rewardNames.add("rewards_profile_icon_3");
        rewardNames.add("rewards_profile_icon_4");

        // Define the structure of the badge data
        Map<String, Object> badgeData = new HashMap<>();
        badgeData.put("reward_status", "available"); // Set status for all badges
        badgeData.put("reward_type", "icon"); // Set reward type for all badges

        // Create a reference to the "Registered Users" node
        referenceUsers = FirebaseDatabase.getInstance().getReference("Registered Users");
        userReference = referenceUsers.child(userId).child("SoftRewards");

        // Iterate over each badge name and set the data under the user's node for each badge
        for (String rewardName : rewardNames) {
            userReference.child(rewardName).setValue(badgeData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                    /*Toast.makeText(Register.this, "Badge data added for " + rewardName,
                            Toast.LENGTH_SHORT).show();*/
                    }
                    else{
                    /*Toast.makeText(Register.this, "Failed to add badge data for " + rewardName,
                            Toast.LENGTH_SHORT).show();*/
                    }
                }
            });
        }
    }
}