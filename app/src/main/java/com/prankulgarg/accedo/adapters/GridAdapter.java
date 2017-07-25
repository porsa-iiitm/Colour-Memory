package com.prankulgarg.accedo.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prankulgarg.accedo.Constants;
import com.prankulgarg.accedo.R;
import com.prankulgarg.accedo.models.GridItem;

import java.util.ArrayList;

/**
 * Created by prankulgarg on 7/24/17.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.CustomViewHolder> {
    private ArrayList<GridItem> mGridItemArrayList;
    private TypedArray mCardsArray;
    private Handler mHandler;
    private IGrid iGrid;

    private int mLastIndex = -1, mCurrentIndex = -1;
    private int mWinCount = 0, mScore = 0;
    private int defaultCardPosition;

    public GridAdapter(Context context, ArrayList<GridItem> gridItemArrayList) {
        mGridItemArrayList = gridItemArrayList;
        mCardsArray = context.getResources().obtainTypedArray(R.array.cards);
        defaultCardPosition = mCardsArray.length() - 1;
        mHandler = new Handler();
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
        final GridItem gridItem = mGridItemArrayList.get(position);

        int cardIndex = (gridItem.getState() == Constants.STATE_CLOSE) ? defaultCardPosition : gridItem.getNumber();
        customViewHolder.ivCard.setImageResource(mCardsArray.getResourceId(cardIndex, defaultCardPosition));

        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleItemClick(gridItem, position);
            }
        });
    }

    private void handleItemClick(GridItem gridItem, int position) {
        if (gridItem.getState() == Constants.STATE_OPEN_WIN) {
            return;
        }

        if (mLastIndex == -1) {
            mLastIndex = position;
            gridItem.setState(Constants.STATE_OPEN);
            notifyItemChanged(position);
            return;
        } else if (mCurrentIndex == -1) {
            mCurrentIndex = position;
            gridItem.setState(Constants.STATE_OPEN);
            notifyItemChanged(position);
        }

        if (mLastIndex != mCurrentIndex) {
            if (mGridItemArrayList.get(mLastIndex).getNumber() == mGridItemArrayList.get(mCurrentIndex).getNumber()) {
                mGridItemArrayList.get(mLastIndex).setState(Constants.STATE_OPEN_WIN);
                mGridItemArrayList.get(mCurrentIndex).setState(Constants.STATE_OPEN_WIN);

                mScore += 2;
                mWinCount += 2;

                notifyItemChanged(mLastIndex);
                notifyItemChanged(mCurrentIndex);
                mLastIndex = -1;
                mCurrentIndex = -1;
            } else {
                final int tempLastIndex = mLastIndex;
                final int tempCurrentIndex = mCurrentIndex;
                mScore -= 1;

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGridItemArrayList.get(tempLastIndex).setState(Constants.STATE_CLOSE);
                        mGridItemArrayList.get(tempCurrentIndex).setState(Constants.STATE_CLOSE);
                        notifyItemChanged(tempLastIndex);
                        notifyItemChanged(tempCurrentIndex);
                    }
                }, Constants.WAIT_DURATION);
                mLastIndex = -1;
                mCurrentIndex = -1;
            }
            if (iGrid != null) {
                iGrid.updateScore(mScore);
            }

            if (isGameFinished()) {
                if (iGrid != null) {
                    iGrid.onGameFinished();
                }
            }
        }
    }

    public void resetGrid() {
        for (GridItem gridItem : mGridItemArrayList) {
            gridItem.setState(Constants.STATE_CLOSE);
        }
        notifyDataSetChanged();
        mWinCount = 0;
        mScore = 0;
    }

    @Override
    public int getItemCount() {
        return mGridItemArrayList.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_grid_item, parent, false);
        return new CustomViewHolder(view);
    }

    private boolean isGameFinished() {
        return mWinCount == mGridItemArrayList.size();
    }

    public void setIGrid(IGrid iGrid) {
        this.iGrid = iGrid;
    }

    public void onActivityPause() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public interface IGrid {
        void updateScore(int score);

        void onGameFinished();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCard;

        CustomViewHolder(View v) {
            super(v);
            ivCard = (ImageView) v.findViewById(R.id.iv_card);
        }
    }
}