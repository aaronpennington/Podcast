package com.penningtonb.podcast;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import java.util.ArrayList;
import java.util.Objects;

public class DisplayFeedActivity extends AppCompatActivity implements IProcess {

    private static final String TAG = "DisplayFeedActivity";
    RAdapter rAdapter;
    FeedFetch fetcher;
    ArrayList<String> feedList;
    TextView feedTitle;
    TextView feedDescription;
    ImageView feedImage;
    String feedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_feed);

        Intent intent = getIntent();
        feedUrl = intent.getStringExtra("Feed URL");

        feedList = new ArrayList<>();
        findViewById(R.id.getFeedButton);
        feedTitle = findViewById(R.id.feedTitleText);
        feedDescription = findViewById(R.id.feedDescriptionText);
        feedDescription.setMovementMethod(new ScrollingMovementMethod());
        feedImage = findViewById(R.id.feedImage);

        rAdapter = new RAdapter(feedList);
        RecyclerView recyclerView = findViewById(R.id.RView);
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateFeed();
    }

    /**
     * Creates a FeedFetch thread, which makes a network call in a background thread to the given
     * URL. Upon completion, the updateAdapter() method is called, which updates the RecyclerView.
     */
    public void updateFeed() {
        fetcher = new FeedFetch( this, feedUrl);
        Thread t = new Thread(fetcher);
        t.start();
    }

    /**
     * Takes a list of episode titles and a feed title from the FeedFetch thread. Adds the episode
     * titles to a list, and updates the RecyclerView list to reflect that change.
     * @param feed Feed object given by the EARL library after parsing the provided URL.
     * @param feedImage Drawable object made in FeedFetch thread, which is set to the podcast artwork.
     */
    @Override
    public void updateAdapter(Feed feed, Drawable feedImage) {
        Log.i(TAG, "Updated feed list");

        // Clear any existing episodes, then add the list from the FeedFetch thread.
        feedList.clear();
        for (Item item : feed.getItems()) {
            String title = item.getTitle();
            feedList.add(title);
            Log.i(TAG, "Episode title: " + item.getTitle());
        }

        Log.i(TAG, "Image URL: " + feed.getImageLink());

        this.feedTitle.setText(feed.getTitle());
        this.feedDescription.setText(feed.getDescription());

        // Create the podcast image
        this.feedImage.setImageDrawable(feedImage);

        // Let the recycler view adapter know that the feedList has been updated.
        rAdapter.notifyDataSetChanged();
    }
}
