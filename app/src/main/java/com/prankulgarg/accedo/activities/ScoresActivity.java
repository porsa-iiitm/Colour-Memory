package com.prankulgarg.accedo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.prankulgarg.accedo.R;
import com.prankulgarg.accedo.ScoresDBHelper;
import com.prankulgarg.accedo.adapters.ScoreAdapter;

public class ScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //Set RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //Set Adapter
        ScoreAdapter scoreAdapter = new ScoreAdapter(ScoresDBHelper.getInstance(this).getSortedScoresArrayList());
        recyclerView.setAdapter(scoreAdapter);
    }
}
