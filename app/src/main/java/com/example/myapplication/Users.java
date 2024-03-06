package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private String email, firstName, middleName, lastName, buddyUid;
    private int coins = 0, exp = 0 , bpLevel = 0;
    private boolean level1status, level2status, level3status, level4status, level5status, level6status, level7status, level8status, level9status, level10status, level11status, level12status, level13status, level14status, level15status;
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
        this.level1status = false;
        this.level2status = false;
        this.level3status = false;
        this.level4status = false;
        this.level5status = false;
        this.level6status = false;
        this.level7status = false;
        this.level8status = false;
        this.level9status = false;
        this.level10status = false;
        this.level11status = false;
        this.level12status = false;
        this.level13status = false;
        this.level14status = false;
        this.level15status = false;

    }

    public boolean isLevel1status() {
        return level1status;
    }

    public void setLevel1status(boolean level1status) {
        this.level1status = level1status;
    }

    public boolean isLevel2status() {
        return level2status;
    }

    public void setLevel2status(boolean level2status) {
        this.level2status = level2status;
    }

    public boolean isLevel3status() {
        return level3status;
    }

    public void setLevel3status(boolean level3status) {
        this.level3status = level3status;
    }

    public boolean isLevel4status() {
        return level4status;
    }

    public void setLevel4status(boolean level4status) {
        this.level4status = level4status;
    }

    public boolean isLevel5status() {
        return level5status;
    }

    public void setLevel5status(boolean level5status) {
        this.level5status = level5status;
    }

    public boolean isLevel6status() {
        return level6status;
    }

    public void setLevel6status(boolean level6status) {
        this.level6status = level6status;
    }

    public boolean isLevel7status() {
        return level7status;
    }

    public void setLevel7status(boolean level7status) {
        this.level7status = level7status;
    }

    public boolean isLevel8status() {
        return level8status;
    }

    public void setLevel8status(boolean level8status) {
        this.level8status = level8status;
    }

    public boolean isLevel9status() {
        return level9status;
    }

    public void setLevel9status(boolean level9status) {
        this.level9status = level9status;
    }

    public boolean isLevel10status() {
        return level10status;
    }

    public void setLevel10status(boolean level10status) {
        this.level10status = level10status;
    }

    public boolean isLevel11status() {
        return level11status;
    }

    public void setLevel11status(boolean level11status) {
        this.level11status = level11status;
    }

    public boolean isLevel12status() {
        return level12status;
    }

    public void setLevel12status(boolean level12status) {
        this.level12status = level12status;
    }

    public boolean isLevel13status() {
        return level13status;
    }

    public void setLevel13status(boolean level13status) {
        this.level13status = level13status;
    }

    public boolean isLevel14status() {
        return level14status;
    }

    public void setLevel14status(boolean level14status) {
        this.level14status = level14status;
    }

    public boolean isLevel15status() {
        return level15status;
    }

    public void setLevel15status(boolean level15status) {
        this.level15status = level15status;
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