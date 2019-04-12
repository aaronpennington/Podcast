package com.penningtonb.podcast;

import android.graphics.drawable.Drawable;

import com.einmalfel.earl.Feed;

/**
 * A callback interface which allows data to be passed from the FeedFetch thread to another
 * Activity.
 */
public interface IProcess {
    void updateAdapter(Feed feed, Drawable feedImage);
}
