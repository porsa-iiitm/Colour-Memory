package com.prankulgarg.accedo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prankulgarg.accedo.R;
import com.prankulgarg.accedo.models.ScoreItem;

import java.util.ArrayList;

/**
 * Created by prankulgarg on 7/24/17.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.CustomViewHolder> {
    private ArrayList<ScoreItem> mScoreItemArrayList;

    public ScoreAdapter(ArrayList<ScoreItem> scoreItemArrayList) {
        this.mScoreItemArrayList = scoreItemArrayList;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
        final ScoreItem scoreItem = mScoreItemArrayList.get(position);

        customViewHolder.tvRank.setText(String.valueOf(customViewHolder.getAdapterPosition() + 1));
        customViewHolder.tvName.setText(scoreItem.getName());
        customViewHolder.tvScore.setText(String.valueOf(scoreItem.getScore()));
    }

    @Override
    public int getItemCount() {
        return mScoreItemArrayList.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_score_item, parent, false);
        return new CustomViewHolder(view);
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvScore;

        CustomViewHolder(View v) {
            super(v);
            tvRank = (TextView) v.findViewById(R.id.tv_rank);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvScore = (TextView) v.findViewById(R.id.tv_score);
        }
    }
}