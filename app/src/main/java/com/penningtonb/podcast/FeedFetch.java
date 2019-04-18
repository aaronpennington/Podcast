package com.penningtonb.podcast;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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
    private String feedUrl;
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
    public void run(){
        Log.i(TAG, "Starting thread...");

        // A valid rss feed url is given from the MainActivity. Then a connection is opened to the
        // url.
        String link = sterilizeUrl(feedUrl);

        // A sample url for debugging.
        //String link = "https://nodumbqs.libsyn.com/rss";

        InputStream inputStream = null;
        try {
            inputStream = new URL(link).openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The EARL library is used to parse the information from the website.
        Feed feed = null;
        Drawable d = null;
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

        InputStream is = null;
        try {
            assert feed != null;
            is = new URL(feed.getImageLink()).openConnection().getInputStream();
            d = Drawable.createFromStream(is, feed.getImageLink());
        } catch (IOException e) {
            e.printStackTrace();
        }



        // The title and a list of episodes are added to a list, which will be passed to MainActivity.
        assert d != null;

        Log.i(TAG, "Processing feed: " + feed.getTitle());

        final Feed finalFeed = feed;
        final Drawable finalD = d;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProcess.updateAdapter(finalFeed, finalD);
            }
        });
    }

    private String sterilizeUrl(String feedUrl) {
        String newUrl = "";

        int dots = 0;
        // Check whether the url needs 'www.' appended
        for (int i = 0; i < feedUrl.length(); i++) {
            if (feedUrl.charAt(i) == '.'){
                dots++;
            }
        }

        if (dots < 2){
            feedUrl = "www." + feedUrl;
        }

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
