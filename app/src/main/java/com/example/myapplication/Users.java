package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private String email, firstName, middleName, lastName, buddyUid, selectedClass;
    private int coins = 0, exp = 0 , bpLevel = 0, dailycap = 0;
    private String level1status, level2status, level3status, level4status, level5status, level6status, level7status, level8status, level9status, level10status, level11status, level12status, level13status, level14status, level15status;
    private Users buddy;  // New field for the buddy
    private List<Tasks> tasks;  // List to store tasks

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        tasks = new ArrayList<>();
    }

    public Users(String email, String firstName, String middleName, String lastName, String selectedClass) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        tasks = new ArrayList<>();
        this.selectedClass = selectedClass;
        this.level1status = "Locked";
        this.level2status = "Locked";
        this.level3status = "Locked";
        this.level4status = "Locked";
        this.level5status = "Locked";
        this.level6status = "Locked";
        this.level7status = "Locked";
        this.level8status = "Locked";
        this.level9status = "Locked";
        this.level10status = "Locked";
        this.level11status = "Locked";
        this.level12status = "Locked";
        this.level13status = "Locked";
        this.level14status = "Locked";
        this.level15status = "Locked";
    }

    public int getDailycap() {
        return dailycap;
    }

    public void setDailycap(int dailycap) {
        this.dailycap = dailycap;
    }

    public String getLevel1status() {
        return level1status;
    }

    public void setLevel1status(String level1status) {
        this.level1status = level1status;
    }

    public String getLevel2status() {
        return level2status;
    }

    public void setLevel2status(String level2status) {
        this.level2status = level2status;
    }

    public String getLevel3status() {
        return level3status;
    }

    public void setLevel3status(String level3status) {
        this.level3status = level3status;
    }

    public String getLevel4status() {
        return level4status;
    }

    public void setLevel4status(String level4status) {
        this.level4status = level4status;
    }

    public String getLevel5status() {
        return level5status;
    }

    public void setLevel5status(String level5status) {
        this.level5status = level5status;
    }

    public String getLevel6status() {
        return level6status;
    }

    public void setLevel6status(String level6status) {
        this.level6status = level6status;
    }

    public String getLevel7status() {
        return level7status;
    }

    public void setLevel7status(String level7status) {
        this.level7status = level7status;
    }

    public String getLevel8status() {
        return level8status;
    }

    public void setLevel8status(String level8status) {
        this.level8status = level8status;
    }

    public String getLevel9status() {
        return level9status;
    }

    public void setLevel9status(String level9status) {
        this.level9status = level9status;
    }

    public String getLevel10status() {
        return level10status;
    }

    public void setLevel10status(String level10status) {
        this.level10status = level10status;
    }

    public String getLevel11status() {
        return level11status;
    }

    public void setLevel11status(String level11status) {
        this.level11status = level11status;
    }

    public String getLevel12status() {
        return level12status;
    }

    public void setLevel12status(String level12status) {
        this.level12status = level12status;
    }

    public String getLevel13status() {
        return level13status;
    }

    public void setLevel13status(String level13status) {
        this.level13status = level13status;
    }

    public String getLevel14status() {
        return level14status;
    }

    public void setLevel14status(String level14status) {
        this.level14status = level14status;
    }

    public String getLevel15status() {
        return level15status;
    }

    public void setLevel15status(String level15status) {
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

    public int getBpLevel() { return bpLevel; }
    public void setBpLevel(int bpLevel) { this.bpLevel = bpLevel; }

    public String getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }
}