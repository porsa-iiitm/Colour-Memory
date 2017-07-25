package com.prankulgarg.accedo.models;

import android.support.annotation.NonNull;

/**
 * Created by prankulgarg on 7/24/17.
 */

public class ScoreItem implements Comparable<ScoreItem> {
    private String name;
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(@NonNull ScoreItem o) {
        //descending order
        return (int) (o.score - this.score);
    }
}
