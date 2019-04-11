package com.penningtonb.podcast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IProcess{

    private static final String TAG = "MainActivity";
    RAdapter rAdapter;
    FeedFetch fetcher;

    ArrayList<String> feedList;
    EditText feedUrl;
    TextView feedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedList = new ArrayList<>();
        findViewById(R.id.getFeedButton);
        feedUrl = findViewById(R.id.feedUrlText);
        feedTitle = findViewById(R.id.feedTitleText);

        rAdapter = new RAdapter(feedList);
        RecyclerView recyclerView = findViewById(R.id.RView);
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void updateFeed(View v) {
        fetcher = new FeedFetch(this, this, feedUrl.getText().toString());
        Thread t = new Thread(fetcher);
        t.start();
    }

    @Override
    public void updateAdapter(ArrayList<String> result, String feedTitle) {
        Log.i(TAG, "Updated feed list");
        feedList.clear();
        feedList.addAll(result);
        for (String item : feedList) {
            Log.i("Episode title: ", item);
        }
        this.feedTitle.setText(feedTitle);
        rAdapter.notifyDataSetChanged();
    }
}
