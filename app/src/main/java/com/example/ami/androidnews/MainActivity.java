package com.example.ami.androidnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String API_URL_STRING = "https://content.guardianapis.com/search?" +
            "q=android&section=technology&api-key=test&show-fields=trailText,byline,thumbnail&" +
            "order-by=newest&format=json&type=article";
    private static final int ARTICLE_LOADER_ID = 1;

    TextView jsonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonView = findViewById(R.id.json);

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
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(MainActivity.this, API_URL_STRING);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        jsonView.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}