package com.example.myapplication;

import android.media.Image;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BattlePassRecycleItems {

    String itemName;
    int itemProgress;
    int itemImage;

    public BattlePassRecycleItems(String itemName, int itemProgress, int itemImage) {
        this.itemName = itemName;
        this.itemProgress = itemProgress;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemProgress() {
        return itemProgress;
    }

    public void setItemProgress(int itemProgress) {
        this.itemProgress = itemProgress;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }
}
