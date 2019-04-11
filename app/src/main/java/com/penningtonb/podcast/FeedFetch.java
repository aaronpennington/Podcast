package com.penningtonb.podcast;

import android.util.Log;
import android.view.View;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class FeedFetch{
    private String TAG = "FeedFetch";
    private ArrayList<String> feedList = new ArrayList<String>();
    private IProcess mProcess;

    FeedFetch(IProcess process) {
        this.mProcess = process;
    }

    ArrayList<String> getFeed() {
        String link = "https://nodumbqs.libsyn.com/rss";
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

        for (Item item : feed.getItems()) {
            String title = item.getTitle();
            feedList.add(title);
            //Log.i(TAG, "Item title: " + (title == null ? "N/A" : title));
        }
        mProcess.updateAdapter(feedList);
        return feedList;
    }
}
