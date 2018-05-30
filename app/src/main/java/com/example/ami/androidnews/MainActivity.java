package com.example.ami.androidnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String API_URL_STRING = "https://content.guardianapis.com/search?" +
            "q=android&section=technology&api-key=test&show-fields=trailText,byline,thumbnail&" +
            "order-by=newest&format=json&type=article";
    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleArrayAdapter mAdapter;

    ListView articlesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set array adapter
        articlesListView = findViewById(R.id.articles_list);
        mAdapter = new ArticleArrayAdapter(this, new ArrayList<Article>());
        articlesListView.setAdapter(mAdapter);

        // Check connectivity before querying
        ConnectivityManager cm =
                (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
        }


    }
    /*
    Implementation of LoaderManager.LoaderCallbacks interface
    */

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(MainActivity.this, API_URL_STRING);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
//        indeterminateBar.setVisibility(View.GONE);
//        emptyView.setText(R.string.empty_string);

        // Clear the adapter of previous articles data
        mAdapter.clear();

        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}