package com.example.ami.androidnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    ArticleArrayAdapter(@NonNull Context context, @NonNull List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Get the {@link Article} object located at this position in the list
        final Article currentArticle = getItem(position);

        if (currentArticle != null) {
            // Set the current article's title
            TextView titleTextView = listItemView.findViewById(R.id.title);
            titleTextView.setText(currentArticle.getTitle());

            // Set the current article's trail text
            TextView trailTextView = listItemView.findViewById(R.id.trail);
            trailTextView.setText(currentArticle.getTrail());

            // Set the current article's by line
            TextView byLineTextView = listItemView.findViewById(R.id.by_line);
            byLineTextView.setText(currentArticle.getByLine());

            // Set the current article's date
            TextView dateTextView = listItemView.findViewById(R.id.date);
            dateTextView.setText(getDateString(currentArticle.getPublicationDate()));
        }
        return listItemView;
    }

    private String getDateString(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(date);
    }
}
