package com.penningtonb.podcast;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class MainActivity extends AppCompatActivity implements IProcess{

    private static final String TAG = "MainActivity";
    RAdapter rAdapter;
    ArrayList<String> feedList;

    EditText feedUrl;
    TextView feedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedList = new ArrayList<>();
        Context context = this;
        Button feedButton = findViewById(R.id.getFeedButton);
        feedUrl = findViewById(R.id.feedUrlText);
        feedTitle = findViewById(R.id.feedTitleText);

        final FeedFetch fetcher = new FeedFetch(this);

        rAdapter = new RAdapter(this, feedList);
        RecyclerView recyclerView = findViewById(R.id.RView);
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void updateFeed(View v) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Log.i(TAG, "Starting thread...");

                //String link = "https://nodumbqs.libsyn.com/rss";
                String link = feedUrl.getText().toString();
                InputStream inputStream = null;
                try {
                    inputStream = new URL(link).openConnection().getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Feed feed = null;
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
                assert feed != null;
                Log.i(TAG, "Processing feed: " + feed.getTitle());
                feedTitle.setText(feed.getTitle());

                feedList.clear();

                for (Item item : feed.getItems()) {
                    String title = item.getTitle();
                    feedList.add(title);
                    //Log.i(TAG, "Item title: " + (title == null ? "N/A" : title));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateAdapter(feedList);
                    }
                });
            }
        });
        t.start();

    }

    @Override
    public void updateAdapter(ArrayList<String> result) {
        Log.i(TAG, "Updated feed list");
        for (String item : feedList) {
            Log.i("Episode title: ", item);
        }
        rAdapter.notifyDataSetChanged();
    }
}
