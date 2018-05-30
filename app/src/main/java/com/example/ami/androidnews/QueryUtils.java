package com.example.ami.androidnews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving data from the web.
 */
final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * A private constructor
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the web API and return a {@link List<Article>} to represent a list of articles.
     */
    public static List<Article> fetchArticlesData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Article} object
//        List<Article> articles = extractArticles(jsonResponse);

        // Return the List<Article>
        return extractArticles(jsonResponse);
    }

    private static List<Article> extractArticles(String jsonResponse) {

        // Create an empty ArrayList that we can start adding articles to
        List<Article> articles = new ArrayList<>();
        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponse);
            // Get 'response' object
            JSONObject jsonResponseObject = jsonRootObject.getJSONObject("response");
            // Get 'results' array
            JSONArray jsonResultsArray = jsonResponseObject.optJSONArray("results");
            for (int i = 0; i < jsonResultsArray.length(); i++) {
                // Get current result object in array
                JSONObject jsonResultObject = jsonResultsArray.getJSONObject(i);

                // Get properties from 'result'
                String title = jsonResultObject.optString("webTitle");
                String articleUrl = jsonResultObject.optString("webUrl");
                String date = jsonResultObject.optString("webPublicationDate");

                // Get 'fields' object
                JSONObject jsonFieldsObject = jsonResultObject.getJSONObject("fields");

                // Get properties from 'fields'
                String trail = jsonFieldsObject.optString("trailText");
                String byLine = jsonFieldsObject.optString("byline");
                String thumbnailUrl = jsonFieldsObject.optString("thumbnail");

                // Add Article to list
                articles.add(new Article(title, trail, byLine, createUrl(articleUrl),
                        createUrl(thumbnailUrl), new Date()));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }

        // Return the list of articles
        return articles;
    }
}
