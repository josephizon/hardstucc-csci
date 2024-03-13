package com.example.myapplication;

public class RewardsHardRecycleItem {

    String rewardName;
    String rewardDescription;
    String dateCreated;
    public RewardsHardRecycleItem() {

    }
    public RewardsHardRecycleItem(String rewardName, String rewardDescription, String dateCreated) {
        this.rewardName = rewardName;
        this.rewardDescription = rewardDescription;
        this.dateCreated = dateCreated;
    }



    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
