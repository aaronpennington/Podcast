package com.penningtonb.podcast;

import java.util.ArrayList;

/**
 * A callback interface which allows data to be passed from the FeedFetch thread to another
 * Activity.
 */
public interface IProcess {
    void updateAdapter(ArrayList<String> result, String feedTitle);
}
