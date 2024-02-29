package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private String email, firstName, middleName, lastName, buddyUid;
    private int coins = 0, exp = 0 , bpLevel = 0;
    private Users buddy;  // New field for the buddy
    private List<Tasks> tasks;  // List to store tasks

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        tasks = new ArrayList<>();
    }

    public Users(String email, String firstName, String middleName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        tasks = new ArrayList<>();
    }

    // Other existing methods...

    public List<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Tasks task) {
        tasks.add(task);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBuddyUid() {
        return buddyUid;
    }

    public void setBuddyUid(String buddyUid) {
        this.buddyUid = buddyUid;
    }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }
    public void addCoins(int coins) { this.coins += coins;  }

    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }
    public void addExp(int exp) { this.exp += exp; }

    public int getBpLevel() { this.bpLevel = exp/1000; return bpLevel; }
    public void setBpLevel(int bpLevel) { this.bpLevel = bpLevel; }
}