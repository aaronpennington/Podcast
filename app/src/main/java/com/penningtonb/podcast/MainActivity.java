package com.penningtonb.podcast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IProcess{

    private static final String TAG = "MainActivity";
    RAdapter rAdapter;
    FeedFetch fetcher;

    ArrayList<String> feedList;
    EditText feedUrl;
    TextView feedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedList = new ArrayList<>();
        findViewById(R.id.getFeedButton);
        feedUrl = findViewById(R.id.feedUrlText);
        feedTitle = findViewById(R.id.feedTitleText);

        rAdapter = new RAdapter(feedList);
        RecyclerView recyclerView = findViewById(R.id.RView);
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * Creates a FeedFetch thread, which makes a network call in a background thread to the given
     * URL. Upon completion, the updateAdapter() method is called, which updates the RecyclerView.
     * @param v The "Fetch Feed" button.
     */
    public void updateFeed(View v) {
        fetcher = new FeedFetch( this, feedUrl.getText().toString());
        Thread t = new Thread(fetcher);
        t.start();
    }

    /**
     * Takes a list of episode titles and a feed title from the FeedFetch thread. Adds the episode
     * titles to a list, and updates the RecyclerView list to reflect that change.
     * @param result A list of episode titles.
     * @param feedTitle The title of the RSS feed.
     */
    @Override
    public void updateAdapter(ArrayList<String> result, String feedTitle) {
        Log.i(TAG, "Updated feed list");

        // Clear any existing episodes, then add the list from the FeedFetch thread.
        feedList.clear();
        feedList.addAll(result);

        // Loop through all episode titles. For debugging purposes only.
        for (String item : feedList) {
            Log.i("Episode title: ", item);
        }

        this.feedTitle.setText(feedTitle);

        // Let the recycler view adapter know that the feedList has been updated.
        rAdapter.notifyDataSetChanged();
    }
}
