package com.example.ami.androidnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String API_URL_STRING = "https://content.guardianapis.com/search";
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
                // Check if a suitable app is installed
                if (linkIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(linkIntent);
                }
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

        // Proceed if there's a connection
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
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences.
        // The second parameter is the default value for this preference.
        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(API_URL_STRING);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("q", "android");
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-fields", "trailText,byline,thumbnail");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("type", "article");
        uriBuilder.appendQueryParameter("section", section);

        // Return the completed uri
        return new ArticleLoader(this, uriBuilder.toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * @param item is the selected menu item
     * @return menu item action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Settings action
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}