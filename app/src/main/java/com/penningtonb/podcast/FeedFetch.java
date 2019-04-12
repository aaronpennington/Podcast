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

    /**
     * FeedFetch constructor which provides the rss feed URL.
     * @param activity MainActivity, which is also cast to the callback interface.
     * @param feedUrl URL of the rss feed.
     */
    FeedFetch(Activity activity, String feedUrl) {
        this.mProcess = (IProcess)activity;
        this.activity = activity;
        this.feedUrl = feedUrl;
        Log.i(TAG, "Initialized Feed Fetcher");
    }

    /**
     * A thread which makes a network call to a website based on the url given in the MainActivity.
     * Using the EARL library, a list of episode titles is obtained, as well as a feed title. Finally,
     * the updateAdapter() method from the MainActivity is called, which updates the RecyclerView.
     */
    @Override
    public void run() {
        Log.i(TAG, "Starting thread...");

        // A valid rss feed url is given from the MainActivity. Then a connection is opened to the
        // url.
        String link = sterilizeUrl(feedUrl);
        InputStream inputStream = null;
        try {
            inputStream = new URL(link).openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The EARL library is used to parse the information from the website.
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

        // The title and a list of episodes are added to a list, which will be passed to MainActivity.
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

    private String sterilizeUrl(String feedUrl) {
        String newUrl = "";

        // Check if the provided url beings with http://
        // if so, change to https://
        if (feedUrl.substring(0, 6).equals("http://")) {
            newUrl = "https://" + feedUrl.substring(7);
        }
        else if (!feedUrl.substring(0, 7).equals("https://")) {
            newUrl = "https://" + feedUrl;
        }

        Log.i(TAG, "New url = " + newUrl);
        return newUrl;
    }
}
