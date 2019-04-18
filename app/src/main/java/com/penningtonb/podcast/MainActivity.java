package com.penningtonb.podcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    EditText feedUrl;
    ArrayList<String> subscriptions;
    RAdapter rAdapter;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.getFeedButton);
        feedUrl = findViewById(R.id.feedUrlText);
        subscriptions = new ArrayList<>();

        //sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);

        rAdapter = new RAdapter(subscriptions);
        RecyclerView recyclerView = findViewById(R.id.subscriptionsRecyclerView);
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Launches DisplayFeedActivity to show all episodes of an rss feed.
     * @param view Fetch Feed button
     */
    public void displayFeed(View view) {
        Intent displayFeedIntent = new Intent(MainActivity.this, DisplayFeedActivity.class);
        displayFeedIntent.putExtra("Feed URL", this.feedUrl.getText().toString());
        startActivity(displayFeedIntent);
        Log.i(TAG, "Display Feed Activity started.");
    }

    public void subscribeToFeed(View view) {
        // Save rss feed title to subscriptions, and save subscriptions to sharedPrefs.
        Toast.makeText(this, "Oops, this doesn't work yet!", Toast.LENGTH_SHORT).show();
    }
}
