package com.offiahchiazor.android.javadeveloperslagos;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DetailActivity extends AppCompatActivity {

    String developerUsername;
    String developerUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        // Display developer Image
        String profile = getIntent().getExtras().getString("image");

        ImageView imageView = (ImageView) findViewById(R.id.detail_image);
        Glide.with(getApplicationContext()).load(profile)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


        // Display developer username
        TextView user = (TextView) findViewById(R.id.detail_username);

        developerUsername = getIntent().getExtras().getString("username");
        user.setText("Developer Username: " + developerUsername);

        // Display link to developer github page
        TextView url = (TextView) findViewById(R.id.detail_url);

        developerUrl = getIntent().getExtras().getString("html");
        url.setText("Github Url: " + developerUrl);
        Linkify.addLinks(url, Linkify.WEB_URLS);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.share_detail:

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Check out this awesome developer @ " +
                        "Github Username: " + developerUsername + "\n" +
                        "Github Url: " + developerUrl;
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share"));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
