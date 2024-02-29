package com.example.myapplication;

public class BadgesRecycleItem {

    String badgeStatus;
    int badgeImage;

    String badgeName;

    public BadgesRecycleItem(String badgeStatus, int badgeImage, String badgeName) {
        this.badgeStatus = badgeStatus;
        this.badgeImage = badgeImage;
        this.badgeName = badgeName;
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

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

}
