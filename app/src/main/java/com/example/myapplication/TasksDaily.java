package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class TasksDaily extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, createTaskButton, createTaskConfirmationButton;
    EditText editTaskName, editTaskDescription, editTaskDeadline, editTaskType;
    TextView textView;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_daily);

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

        createTaskButton = findViewById(R.id.create_daily_task);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){ showCreatePopUp(); }
        });

        //RecyclerView create
        RecyclerView recyclerView = findViewById(R.id.daily_task_display);

        List<TasksRecycleItems> taskItems = new ArrayList<TasksRecycleItems>();
        taskItems.add(new TasksRecycleItems("Title 1", "Description 1", "June 1, 2020"));
        taskItems.add(new TasksRecycleItems("Title 2", "Description 2", "June 2, 2020"));
        taskItems.add(new TasksRecycleItems("Title 3", "Description 3", "June 3, 2020"));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TasksRecycleAdapter(getApplicationContext(), taskItems));

    }

    public void openMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openMainActivity2(View view) {
        startActivity(new Intent(this, MainActivity2.class));
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

    private void showCreatePopUp() {
        Dialog popUp = new Dialog(this, R.style.DialogStyle);
        popUp.setContentView(R.layout.activity_tasks_create);
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