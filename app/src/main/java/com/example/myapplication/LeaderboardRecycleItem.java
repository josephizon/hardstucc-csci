package com.example.myapplication;

public class LeaderboardRecycleItem {

    String leader_rank, leader_name, leader_level;

    int leader_icon, leader_collectible_1, leader_collectible_2, leader_collectible_3;

    int leader_badge_1, leader_badge_2, leader_badge_3;

    public LeaderboardRecycleItem(String leader_rank, String leader_name, String leader_level, int leader_icon, int leader_collectible_1, int leader_collectible_2, int leader_collectible_3, int leader_badge_1, int leader_badge_2, int leader_badge_3) {
        this.leader_rank = leader_rank;
        this.leader_name = leader_name;
        this.leader_level = leader_level;
        this.leader_icon = leader_icon;
        this.leader_collectible_1 = leader_collectible_1;
        this.leader_collectible_2 = leader_collectible_2;
        this.leader_collectible_3 = leader_collectible_3;
        this.leader_badge_1 = leader_badge_1;
        this.leader_badge_2 = leader_badge_2;
        this.leader_badge_3 = leader_badge_3;
    }

    public String getLeader_rank() {
        return leader_rank;
    }

    public void setLeader_rank(String leader_rank) {
        this.leader_rank = leader_rank;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public String getLeader_level() {
        return leader_level;
    }

    public void setLeader_level(String leader_level) {
        this.leader_level = leader_level;
    }

    public int getLeader_icon() {
        return leader_icon;
    }

    public void setLeader_icon(int leader_icon) {
        this.leader_icon = leader_icon;
    }

    public int getLeader_collectible_1() {
        return leader_collectible_1;
    }

    public void setLeader_collectible_1(int leader_collectible_1) {
        this.leader_collectible_1 = leader_collectible_1;
    }

    public int getLeader_collectible_2() {
        return leader_collectible_2;
    }

    public void setLeader_collectible_2(int leader_collectible_2) {
        this.leader_collectible_2 = leader_collectible_2;
    }

    public int getLeader_collectible_3() {
        return leader_collectible_3;
    }

    public void setLeader_collectible_3(int leader_collectible_3) {
        this.leader_collectible_3 = leader_collectible_3;
    }

    public int getLeader_badge_1() {
        return leader_badge_1;
    }

    public void setLeader_badge_1(int leader_badge_1) {
        this.leader_badge_1 = leader_badge_1;
    }

    public int getLeader_badge_2() {
        return leader_badge_2;
    }

    public void setLeader_badge_2(int leader_badge_2) {
        this.leader_badge_2 = leader_badge_2;
    }

    public int getLeader_badge_3() {
        return leader_badge_3;
    }

    public void setLeader_badge_3(int leader_badge_3) {
        this.leader_badge_3 = leader_badge_3;
    }
}
