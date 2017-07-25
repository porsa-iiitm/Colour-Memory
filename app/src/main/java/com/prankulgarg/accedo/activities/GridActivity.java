package com.prankulgarg.accedo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prankulgarg.accedo.R;
import com.prankulgarg.accedo.ScoresDBHelper;
import com.prankulgarg.accedo.Utils;
import com.prankulgarg.accedo.adapters.GridAdapter;
import com.prankulgarg.accedo.models.GridItem;
import com.prankulgarg.accedo.models.ScoreItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GridActivity extends AppCompatActivity implements GridAdapter.IGrid {
    private int columnCount = 4;
    private TextView tvScore;
    private GridAdapter mGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        //Set RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columnCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        tvScore = (TextView) findViewById(R.id.tv_score);

        //Set Adapter
        mGridAdapter = new GridAdapter(this, getGriditemArrayList());
        mGridAdapter.setIGrid(this);
        recyclerView.setAdapter(mGridAdapter);

        //Set click listeners
        findViewById(R.id.btn_high_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GridActivity.this, ScoresActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<GridItem> getGriditemArrayList() {
        int gridSize = columnCount * columnCount;

        //Generate random numbers from 0 gridSize/2 -1
        Random random = new Random();
        HashMap<Integer, Integer> randomHashMap = new HashMap<>();
        ArrayList<GridItem> gridItemArrayList = new ArrayList<>();

        int i = 0;
        while (i < gridSize) {
            int rand = random.nextInt(gridSize / 2);

            if (!randomHashMap.containsKey(rand) || randomHashMap.get(rand) != 2) {
                if (!randomHashMap.containsKey(rand)) {
                    randomHashMap.put(rand, 1);
                } else {
                    randomHashMap.put(rand, 2);
                }
                GridItem gridItem = new GridItem();
                gridItem.setNumber(rand);
                gridItemArrayList.add(gridItem);
                i++;
            }
        }
        return gridItemArrayList;
    }


    @Override
    public void updateScore(int score) {
        tvScore.setText(String.valueOf(score));
    }

    @Override
    public void onGameFinished() {
        showUserNamePopup();
    }

    private void showUserNamePopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.view_edittext, null);
        final EditText edittext = (EditText) view.findViewById(R.id.edit_text);
        builder.setTitle(getString(R.string.title_name));
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.lbl_submit), null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = edittext.getText().toString().trim();
                        if (!TextUtils.isEmpty(name)) {
                            Utils.hideSoftKeyboard(GridActivity.this);
                            alertDialog.dismiss();

                            //Insert a new entry in table
                            ScoreItem scoreItem = new ScoreItem();
                            scoreItem.setName(name);
                            scoreItem.setScore(Integer.parseInt(tvScore.getText().toString()));
                            ScoresDBHelper.getInstance(GridActivity.this).insert(scoreItem);

                            //Open scores screen
                            tvScore.setText("");
                            mGridAdapter.resetGrid();
                            Intent intent = new Intent(GridActivity.this, ScoresActivity.class);
                            startActivity(intent);
                        } else {
                            Utils.showError(GridActivity.this, view, getString(R.string.error_empty_name));
                        }
                    }
                });
            }
        });
        //show dialog
        if (!isFinishing()) {
            alertDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGridAdapter.onActivityPause();
    }
}
