package com.offiahchiazor.android.javadeveloperslagos;

import android.text.TextUtils;
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
import java.util.List;

/**
 * Helper methods related to requesting and receiving developer data
 * from the GITHUB API.
 *
 * Created by Offiah Chiazor on 04/08/2017.
 */
public class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_MSG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {}

    public static List<Developer> fetchDeveloperData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_MSG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Developer} object
        List<Developer> developer = extractDevelopers(jsonResponse);

        // Return the list of {@link Developer}s
        return developer;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_MSG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return  jsonResponse;
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
                Log.e(LOG_MSG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_MSG, "Problem retrieving the earthquake JSON results.", e);
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
     * Return a list of {@link Developer} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Developer> extractDevelopers(String developerJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(developerJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding developers to
        ArrayList<Developer> javaDevelopers = new ArrayList<>();

        // Try to parse the GITHUB_API. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of Developer objects with the corresponding data.
            JSONObject baseDeveloperJson = new JSONObject(developerJSON);
            JSONArray javaDeveloperArray = baseDeveloperJson.getJSONArray("items");

            for (int i = 0; i < javaDeveloperArray.length(); i++) {
                JSONObject currentJavaDeveloper = javaDeveloperArray.getJSONObject(i);
                String javaDeveloperUsername = currentJavaDeveloper.getString("login");
                String javaDeveloperImage = currentJavaDeveloper.getString("avatar_url");
                String javaDeveloperUrl = currentJavaDeveloper.getString("html_url");

                // Create a new Developer object
                Developer developer = new Developer(javaDeveloperImage, javaDeveloperUsername, javaDeveloperUrl);
                // Add new developer to the list of developers
                javaDevelopers.add(developer);
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of developers
        return javaDevelopers;
    }

}
