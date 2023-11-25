package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TasksMajor extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, createTaskButton, createTaskConfirmationButton;
    EditText editTaskName, editTaskDescription, editTaskDeadline, editTaskType;
    TextView textView;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_major);

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

        createTaskButton = findViewById(R.id.create_major_task);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view){
                showCreatePopUp();
            }
        });
    }

    public void openMainActivity2(View view) {
        startActivity(new Intent(this, MainActivity2.class));
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

    private void showCreatePopUp() {
        Dialog popUp = new Dialog(this );
        popUp.setContentView(R.layout.activity_tasks_create);

        ImageView closeCreateTaskButton = popUp.findViewById(R.id.exitCreateTaskButton);
        editTaskName = popUp.findViewById(R.id.task_name_input);
        editTaskDescription = popUp.findViewById(R.id.task_description_input);
        editTaskDeadline = popUp.findViewById(R.id.task_deadline_input);
        editTaskType = popUp.findViewById(R.id.task_type_input);

        createTaskConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName, taskDescription, taskDeadline, taskType;
                taskName = String.valueOf(editTaskName.getText());
                taskDescription = String.valueOf(editTaskDescription.getText());
                taskDeadline = String.valueOf(editTaskDeadline.getText());
                taskType = String.valueOf(editTaskType.getText());

                if (TextUtils.isEmpty(taskName)) {
                    Toast.makeText(TasksMajor.this, "Enter Task's Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(taskDescription)) {
                    Toast.makeText(TasksMajor.this, "Enter Task's Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(taskDeadline)) {
                    Toast.makeText(TasksMajor.this, "Enter Task's Deadline", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(taskType)) {
                    Toast.makeText(TasksMajor.this, "Enter Task's Type", Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });

        popUp.show();
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