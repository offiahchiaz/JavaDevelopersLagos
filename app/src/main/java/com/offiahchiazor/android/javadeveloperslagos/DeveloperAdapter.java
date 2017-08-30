package com.offiahchiazor.android.javadeveloperslagos;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

/**
 * An {@link DeveloperAdapter} knows how to create a list item layout for each
 * developer in the data source (a list of {@link Developer} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView to
 * be displayed to the user
 *
 * Created by Offiah Chiazor on 04/08/2017.
 */
public class DeveloperAdapter extends ArrayAdapter<Developer> {

    private Context mContext;

    /**
     * Construct a new {@link DeveloperAdapter}
     *
     * @param context of the app
     * @param developers is the list of developers, which is the data source
     *                   of the adapter
     */
    public DeveloperAdapter(Context context, List<Developer> developers) {
        super(context, 0, developers);
        mContext = context;

    }


    /**
     * Returns a list view that displays information about the developer at the
     * given position in the list of developers.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView)
        // that we can reuse, otherwise, if convertView is null, then inflate a
        // new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.developer_list_item, parent, false);
        }

        // Find the developer at the given position in the list of developers
        Developer currentDeveloper = getItem(position);

        // Find the TextView with view ID developer_image
        final ImageView imageView = (ImageView) listItemView.findViewById(R.id.developer_image);
        Glide.with(mContext).load(currentDeveloper.getProfileImage()).asBitmap()
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                          @Override
                     protected void setResource(Bitmap resource) {
                              RoundedBitmapDrawable circularBitmapDrawable =
                                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                              circularBitmapDrawable.setCircular(true);
                              imageView.setImageDrawable(circularBitmapDrawable);
                          }
                      }
                );


        // Find the TextView with view ID developer_username
        TextView usernameTextView = (TextView) listItemView.findViewById(R.id.developer_username);
        // Display the developer username of the current developer in that TextView
        usernameTextView.setText(currentDeveloper.getDeveloperUsername());

        // Return the list view that is now showing the appropriate data
        return listItemView;
    }
}

