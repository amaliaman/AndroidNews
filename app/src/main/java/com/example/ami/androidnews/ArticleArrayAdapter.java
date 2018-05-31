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

    // View holder for quick access to views
    static class ViewHolder {
        TextView titleTextView;
        TextView trailTextView;
        TextView sectionTextView;
        TextView byLineTextView;
        TextView dateTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            // Set the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = listItemView.findViewById(R.id.title);
            viewHolder.trailTextView = listItemView.findViewById(R.id.trail);
            viewHolder.sectionTextView = listItemView.findViewById(R.id.section);
            viewHolder.byLineTextView = listItemView.findViewById(R.id.by_line);
            viewHolder.dateTextView = listItemView.findViewById(R.id.date);

            // store the holder with the view.
            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }
        // Get the {@link Article} object located at this position in the list
        final Article currentArticle = getItem(position);

        if (currentArticle != null) {
            // Set the current article's fields
            viewHolder.titleTextView.setText(currentArticle.getTitle());
            viewHolder.trailTextView.setText(Html.fromHtml(currentArticle.getTrail()));
            viewHolder.sectionTextView.setText(currentArticle.getSection());
            if (currentArticle.getByLine() != null) {
                viewHolder.byLineTextView.setText(currentArticle.getByLine());
                viewHolder.byLineTextView.setVisibility(View.VISIBLE);
            }
            if (currentArticle.getPublicationDate() != null) {
                viewHolder.dateTextView.setText(getDateString(currentArticle.getPublicationDate()));
                viewHolder.dateTextView.setVisibility(View.VISIBLE);
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
