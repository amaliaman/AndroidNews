package com.example.ami.androidnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String API_URL_STRING = "https://content.guardianapis.com/search?" +
            "q=android&api-key=test&show-fields=trailText,byline,thumbnail&" +
            "order-by=newest&format=json&type=article";
    private static final int ARTICLE_LOADER_ID = 1;

    private ArticleArrayAdapter mAdapter;
    TextView emptyView;
    ProgressBar indeterminateBar;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView articlesListView = findViewById(R.id.articles_list);
        emptyView = findViewById(R.id.empty);
        indeterminateBar = findViewById(R.id.indeterminateBar);

        // Set empty view
        articlesListView.setEmptyView(emptyView);

        // Set array adapter
        mAdapter = new ArticleArrayAdapter(this, new ArrayList<Article>());
        articlesListView.setAdapter(mAdapter);

        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                URL url = mAdapter.getItem(position).getArticleUrl();
                Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
                startActivity(linkIntent);
            }
        });

        // Connect to the web to get data
        connectToApi();

        // Set swipe refresh
        swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.secondaryColor);
    }

    private void connectToApi() {
        // Check connectivity before querying
        ConnectivityManager cm =
                (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;

        // Procees if there's a connection
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            indeterminateBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_connection);
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
        indeterminateBar.setVisibility(View.GONE);
        emptyView.setText(R.string.empty_string);

        // Clear the adapter of previous articles data
        mAdapter.clear();

        // Stop swipe refresh
        swipeLayout.setRefreshing(false);

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

    @Override
    public void onRefresh() {
        connectToApi();
    }
}