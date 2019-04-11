package com.penningtonb.podcast;

import android.app.Activity;
import android.util.Log;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class FeedFetch implements Runnable {
    private String TAG = "FeedFetch";
    private ArrayList<String> feedList = new ArrayList<>();
    private String feedUrl;
    private String feedTitle;
    private IProcess mProcess;
    private Activity activity;

    FeedFetch(IProcess process, Activity activity, String feedUrl) {
        this.mProcess = process;
        this.activity = activity;
        this.feedUrl = feedUrl;
        Log.i(TAG, "Initialized Feed Fetcher");
    }

    @Override
    public void run() {
        Log.i(TAG, "Starting thread...");

        //String link = "https://nodumbqs.libsyn.com/rss";
        String link = feedUrl;
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
        feedTitle = feed.getTitle();

        feedList.clear();

        for (Item item : feed.getItems()) {
            String title = item.getTitle();
            feedList.add(title);
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProcess.updateAdapter(feedList, feedTitle);
            }
        });
    }
}
