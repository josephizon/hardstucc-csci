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

        viewHolder.badgeImage.setImageResource(images.get(position));
        viewHolder.badgeName.setText(names.get(position));

        return convertView;
    }

    static class ViewHolder {
        ImageView badgeImage;
        TextView badgeName;
    }
}
