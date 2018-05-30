package com.example.ami.androidnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class ArticleLoader extends AsyncTaskLoader<String> {

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        String a = "";
        // Perform the network request, parse the response, and extract a list of earthquakes.
        a = QueryUtils.fetchArticlesData(mUrl);

        return a;
    }
}
