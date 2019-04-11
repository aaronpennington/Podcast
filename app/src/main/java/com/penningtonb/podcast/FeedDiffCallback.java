package com.penningtonb.podcast;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class FeedDiffCallback extends DiffUtil.Callback {

    private final ArrayList<String> oldFeedList;
    private final ArrayList<String> newFeedList;

    public FeedDiffCallback(ArrayList<String> oldFeedList, ArrayList<String> newFeedList) {
        this.oldFeedList = oldFeedList;
        this.newFeedList = newFeedList;

    }

    @Override
    public int getOldListSize() {
        return oldFeedList.size();
    }

    @Override
    public int getNewListSize() {
        return newFeedList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return oldFeedList.get(i) == newFeedList.get(i1);
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return oldFeedList.get(i).equals(newFeedList.get(i1));
    }

    @Nullable
    @Override
    public Object getChangePayload(int i, int i1) {
        return super.getChangePayload(i, i1);
    }
}
