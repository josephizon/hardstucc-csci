package com.example.myapplication;

public class BadgesRecycleItem {

    String badgeStatus;
    int badgeImage;

    public BadgesRecycleItem(String badgeStatus, int badgeImage) {
        this.badgeStatus = badgeStatus;
        this.badgeImage = badgeImage;
    }

    public String getBadgeStatus() {
        return badgeStatus;
    }

    public void setBadgeStatus(String badgeStatus) {
        this.badgeStatus = badgeStatus;
    }

    public int getBadgeImage() {
        return badgeImage;
    }

    public void setBadgeImage(int badgeImage) {
        this.badgeImage = badgeImage;
    }
}
