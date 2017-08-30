package com.offiahchiazor.android.javadeveloperslagos;

import android.app.LoaderManager;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeveloperActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Developer>> {

    /** URL for developer data from the github dataset */
    private static final String GITHUB_API =
            "https://api.github.com/search/users?q=type:user+location:lagos+language:java";

    /**
     * Constant value for the developer ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders
     */
    private static final int DEVELOPER_ID = 1;

    /** Adapter for the list of developers */
    private DeveloperAdapter mDeveloperAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView developerListView = (ListView) findViewById(R.id.developer_list);

        // Get the empty state displayed if no developer found data to display
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        developerListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of developers as input
        mDeveloperAdapter = new DeveloperAdapter(this, new ArrayList<Developer>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        developerListView.setAdapter(mDeveloperAdapter);

        // Set an item click listener on the ListView, which sends an intent
        //to details_activity to open more information about the selected developer
        developerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Current developer clicked
                Developer developer = mDeveloperAdapter.getItem(i);

                String developerImage = developer.getProfileImage();
                String developerUsername = developer.getDeveloperUsername();
                String developerHtmlUrl = developer.getDeveloperUrl();

                // Connect to the details_Activity through intent
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("image", developerImage);
                detailIntent.putExtra("username", developerUsername);
                detailIntent.putExtra("html", developerHtmlUrl);
                startActivity(detailIntent);
            }
        });

        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(DEVELOPER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Developer>> onCreateLoader(int i, Bundle bundle) {
        return new DeveloperLoader(this, GITHUB_API);
    }

    @Override
    public void  onLoadFinished(Loader<List<Developer>> loader, List<Developer> developers) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No developer data found."
        mEmptyStateTextView.setText(R.string.no_developer_found);

        // Clear the adapter of previous developer data
        mDeveloperAdapter.clear();

        // If there is a valid list of {@link Developer}s, then add them to
        // the adapter's data set. This will trigger the ListView to update.
        if (developers != null && !developers.isEmpty()) {
            mDeveloperAdapter.addAll(developers);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Developer>> loader) {
        // Loader reset, so we can clear out our existing data
        mDeveloperAdapter.clear();
    }


}
