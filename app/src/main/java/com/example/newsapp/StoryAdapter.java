package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StoryAdapter extends ArrayAdapter<NewsObject> {
    StoryAdapter(@NonNull Context context, ArrayList<NewsObject> myNews) {
        super(context, 0, myNews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final NewsObject newsObject = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_story, parent, false);
        }
        TextView story_title = convertView.findViewById(R.id.story_title);
        assert newsObject != null;
        story_title.setText(newsObject.getTitle());
        TextView story_section = convertView.findViewById(R.id.story_section);
        story_section.setText(newsObject.getSection());
        TextView story_date = convertView.findViewById(R.id.story_date);
        story_date.setText(newsObject.getDate());
        RelativeLayout myContainer = convertView.findViewById(R.id.myContainer);
        myContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = newsObject.getLink();

                Uri webPage = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
