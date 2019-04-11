package com.penningtonb.podcast;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout row;
        TextView title;

        ViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.a_row);
            title = view.findViewById(R.id.title);
        }
    }

    private ArrayList<String> episodeList;

    RAdapter(ArrayList<String> feedList) {
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
    public RAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }
}
