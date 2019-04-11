package com.penningtonb.podcast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout row;
        TextView title;

        ViewHolder(View view) {
            super(view);
            row = (ConstraintLayout) view.findViewById(R.id.a_row);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    private ArrayList<String> episodeList;

    RAdapter(Context c, ArrayList<String> feedList) {
        episodeList = feedList;
    }

    @Override
    public void onBindViewHolder(@NonNull RAdapter.ViewHolder viewHolder, int i) {
        TextView title = viewHolder.title;
        title.setText(episodeList.get(i));
    }

    @Override
    public int getItemCount() {
        if (episodeList == null)
            return 0;
        else
            return episodeList.size();
    }

    @NonNull
    @Override
    public RAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    public void updateList(ArrayList<String> feedList) {
        final FeedDiffCallback diffCallback = new FeedDiffCallback(episodeList, feedList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
    }
}
