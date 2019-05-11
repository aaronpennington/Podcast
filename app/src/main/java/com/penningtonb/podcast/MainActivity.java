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
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.DataFormatException;

public class MainActivity extends AppCompatActivity implements IProcess{

    // TODO:
    //  Media Player
    //  Podcast Search/Discovery API
    //  Make subscriptions list clickable

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
        feedUrl = findViewById(R.id.feedLinkText);
        subscriptions = new ArrayList<>();
        sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove("NDQ");
        editor.apply();

        Map<String, ?> prefs = sharedPrefs.getAll();
        for(Map.Entry<String, ?> entry : prefs.entrySet()) {
            subscriptions.add(entry.getValue().toString());
            Log.d(TAG, "Added " + entry.getValue().toString() + "to subscriptions");
        }

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
        //TODO: Add error handling here.
        // For example, if url is null, display activity should not launch.

        if (feedUrl.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent displayFeedIntent = new Intent(MainActivity.this, DisplayFeedActivity.class);
            displayFeedIntent.putExtra("Feed URL", this.feedUrl.getText().toString());
            startActivity(displayFeedIntent);
            Log.i(TAG, "Display Feed Activity started.");
        }
    }

    /**
     * Adds a rss feed title to a list which is automatically displayed when the app starts.
     * @param view The url of the feed which is being subscribed to.
     */
    public void subscribeToFeed(View view) {
        Toast.makeText(this, "Oops, this doesn't work yet!", Toast.LENGTH_SHORT).show();

        // Save rss feed title to subscriptions, and save subscriptions to sharedPrefs.

        final String link = serializeLink(feedUrl.getText().toString());

        if (link.equals("")) {
            Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
        }
        else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream inputStream = null;
                    try {
                        inputStream = new URL(link).openConnection().getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // The EARL library is used to parse the information from the website.
                    Feed feed = null;
                    final Drawable d = null;
                    try {
                        assert inputStream != null;
                        feed = EarlParser.parseOrThrow(inputStream, 0);

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DataFormatException e) {
                        e.printStackTrace();
                    }

                    final Feed finalFeed = feed;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateAdapter(finalFeed, d);
                        }
                    });
                }
            });
            t.start();
        }
    }

    /**
     * Updates the recycler view with the new list of subscriptions.
     * @param feed The list of feeds subscribed to.
     * @param feedImage RSS feed artwork as a drawable image
     */
    @Override
    public void updateAdapter(Feed feed, Drawable feedImage) {
        String feedTitle = feed.getTitle();

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(feedTitle, feedTitle);
        editor.apply();

        subscriptions.add(sharedPrefs.getString(feedTitle, feedTitle));
        rAdapter.notifyDataSetChanged();
    }

    /**
     * Standardizes the urls that are entered by the user.
     * @param link The url entered by the user
     * @return The updated url. 
     */
    @Override
    public String serializeLink(String link) {
        String newUrl = "";

        int dots = 0;
        // Check whether the url needs 'www.' appended
        for (int i = 0; i < link.length(); i++) {
            if (link.charAt(i) == '.'){
                dots++;
            }
        }

        if (dots < 2){
            link = "www." + link;
            Log.d(TAG, "URL = " + link);
        }

        // Check if the provided url beings with http://
        // if so, change to https://
        if (link.substring(0, 7).equals("http://")) {
            newUrl = "https://" + link.substring(7);
        }
        else if (!link.substring(0, 8).equals("https://")) {
            newUrl = "https://" + link;
        }

        Log.i(TAG, "New url = " + newUrl);
        return newUrl;
    }
}
