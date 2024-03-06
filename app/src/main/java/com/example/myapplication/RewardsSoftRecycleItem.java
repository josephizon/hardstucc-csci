package com.example.myapplication;

public class RewardsSoftRecycleItem {

    String rewardSoftName;
    String rewardSoftPrice;
    String rewardSoftStatus;
    String rewardSoftType;
    int rewardSoftImage;


    public RewardsSoftRecycleItem(String rewardSoftName, String rewardSoftPrice, String rewardSoftStatus, int rewardSoftImage, String rewardSoftType) {
        this.rewardSoftName = rewardSoftName;
        this.rewardSoftPrice = rewardSoftPrice;
        this.rewardSoftImage = rewardSoftImage;
        this.rewardSoftStatus = rewardSoftStatus;
        this.rewardSoftType = rewardSoftType;
    }

    public String getRewardSoftName() {
        return rewardSoftName;
    }

    public void setRewardSoftName(String rewardSoftName) {
        this.rewardSoftName = rewardSoftName;
    }

    public String getRewardSoftPrice() {
        return rewardSoftPrice;
    }

    public void setRewardSoftPrice(String rewardSoftPrice) {
        this.rewardSoftPrice = rewardSoftPrice;
    }

    public int getRewardSoftImage() {
        return rewardSoftImage;
    }

    public void setRewardSoftImage(int rewardSoftImage) {
        this.rewardSoftImage = rewardSoftImage;
    }

    public String getRewardSoftStatus() {
        return rewardSoftStatus;
    }

    public void setRewardSoftStatus(String rewardSoftStatus) {
        this.rewardSoftStatus = rewardSoftStatus;
    }

    public String getRewardSoftType() {
        return rewardSoftType;
    }

    public void setRewardSoftType(String rewardSoftType) {
        this.rewardSoftType = rewardSoftType;
    }
}


