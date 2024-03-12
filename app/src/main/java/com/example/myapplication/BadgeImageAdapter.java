package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class BadgeImageAdapter extends ArrayAdapter<Integer> {
    private final Context context;
    private final List<Integer> images;
    private final List<String> names;

    public BadgeImageAdapter(Context context, List<Integer> images, List<String> names) {
        super(context, 0, images);
        this.context = context;
        this.images = images;
        this.names = names;
    }

    /*@NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.badge_image_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.badgeImage = convertView.findViewById(R.id.badge_image);
            viewHolder.badgeName = convertView.findViewById(R.id.badge_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.badgeImage.setImageResource(images.get(position));
        viewHolder.badgeName.setText(names.get(position));

        return convertView;
    }*/

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.badge_image_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.badgeImage = convertView.findViewById(R.id.badge_image);
            viewHolder.badgeName = convertView.findViewById(R.id.badge_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set badge image
        viewHolder.badgeImage.setImageResource(images.get(position));

        // Set badge name based on badge's name
        String originalName = names.get(position);
        String displayName = getDisplayName(originalName);
        viewHolder.badgeName.setText(displayName);

        return convertView;
    }

    // Method to get the display name for a badge based on its original name
    private String getDisplayName(String originalName) {
        // Use a switch-case statement to handle different badge names
        switch (originalName) {
            case "badges_level_2":
                return "Peace Be With You 2";
            case "badges_level_4":
                return "A Kiss 4 U";
            case "badges_level_7":
                return "Seven comes before gr-Eight B)";
            case "badges_level_9":
                return "Keep it up, you’re on fire!";
            case "badges_level_12":
                return "12? You’re on a roll!";
            case "badges_level_14":
                return "Isang pindot nalang...";
            case "badges_level_15":
                return "Level 15 Trophy!!!";
            case "badges_buddy_click":
                return "I care about my buddy's profile :~D";

            case "rewards_collectible_1":
                return "Chipi Chipi Chapa Chapa";
            case "rewards_collectible_2":
                return "huhH HUhHH huH";
            case "rewards_collectible_3":
                return "Smudge the Cat";
            case "rewards_collectible_4":
                return "No Thoughts, Head Empty";
            case "rewards_collectible_5":
                return "Chinese Beaver";
            case "rewards_collectible_6":
                return "Long Majestic Borzoi Dog";
            case "rewards_collectible_7":
                return "bRATz";
            case "rewards_collectible_8":
                return "Wadadadadang";
            case "rewards_collectible_9":
                return "Nah, I'd Win Real";

            case "rewards_profile_icon_1":
                return "Profile Icon 1";
            case "rewards_profile_icon_2":
                return "Profile Icon 2";
            case "rewards_profile_icon_3":
                return "Profile Icon 3";
            case "rewards_profile_icon_4":
                return "Profile Icon 4";
            case "rewards_profile_icon_5":
                return "Profile Icon 5";
            case "rewards_profile_icon_6":
                return "Profile Icon 6";
            case "rewards_profile_icon_7":
                return "Profile Icon 7";
            case "rewards_profile_icon_8":
                return "Profile Icon 8";


            default:
                return originalName; // Return the original name if no modification is needed
        }
    }

    static class ViewHolder {
        ImageView badgeImage;
        TextView badgeName;
    }
}
