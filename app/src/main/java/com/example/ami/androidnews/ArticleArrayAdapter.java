package com.example.ami.androidnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
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
            trailTextView.setText(Html.fromHtml(currentArticle.getTrail()));

            // Set the current article's section
            TextView sectionTextView = listItemView.findViewById(R.id.section);
            sectionTextView.setText(currentArticle.getSection());

            // Set the current article's by line
            TextView byLineTextView = listItemView.findViewById(R.id.by_line);
            if (currentArticle.getByLine() != null) {
                byLineTextView.setText(currentArticle.getByLine());
                byLineTextView.setVisibility(View.VISIBLE);
            }

            // Set the current article's date
            TextView dateTextView = listItemView.findViewById(R.id.date);
            if (currentArticle.getPublicationDate() != null) {
                dateTextView.setText(getDateString(currentArticle.getPublicationDate()));
                dateTextView.setVisibility(View.VISIBLE);
            }
        }
        return listItemView;
    }

    /**
     * @param date is the given date to format
     * @return a string in specified format from given date
     */
    private String getDateString(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(date);
    }
}
