package com.offiahchiazor.android.javadeveloperslagos;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Offiah Chiazor on 04/08/2017.
 */
public class DeveloperLoader extends AsyncTaskLoader<List<Developer>> {

    /** Query URL */
    private String mUrl;

    /**
     * Construct a new {@link Developer}
     * @param context of the activity
     * @param url to load data from
     */
    public DeveloperLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected  void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread
     * @return
     */
    @Override
    public List<Developer> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list
        // of developers.
        List<Developer> result = QueryUtils.fetchDeveloperData(mUrl);
        return result;
    }

}
