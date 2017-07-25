package com.prankulgarg.accedo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.prankulgarg.accedo.models.ScoreItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by prankulgarg on 7/24/17.
 */
public class ScoresDBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "table_scores";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_SCORE = "score";
    private static SQLiteDatabase mDB;
    private static ScoresDBHelper mHelper;

    private ScoresDBHelper(Context context) {
        super(context, "ScoresDBHelper", null, 1);
    }

    public static ScoresDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new ScoresDBHelper(context.getApplicationContext());
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String gameDataQuery = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_NAME + " STRING, "
                + COL_SCORE + " INTEGER "
                + ")";
        db.execSQL(gameDataQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


    @NonNull
    public boolean insert(ScoreItem scoreItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, scoreItem.getName());
        contentValues.put(COL_SCORE, scoreItem.getScore());

        long insertedId = getDB().insert(TABLE_NAME, null, contentValues);
        Log.d("TAG", "Inserted id: " + insertedId);
        return true;
    }

    @NonNull
    public ArrayList<ScoreItem> getSortedScoresArrayList() {
        ArrayList<ScoreItem> scoreItemArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getDB();
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(COL_NAME);
                int scoreIndex = cursor.getColumnIndex(COL_SCORE);

                while (!cursor.isAfterLast()) {
                    ScoreItem scoreItem = new ScoreItem();
                    scoreItem.setName(cursor.getString(nameIndex));
                    scoreItem.setScore(cursor.getInt(scoreIndex));
                    scoreItemArrayList.add(scoreItem);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Collections.sort(scoreItemArrayList);
        Log.d("TAG", "scoreItemArrayList size: " + scoreItemArrayList.size());
        return scoreItemArrayList;
    }

    /**
     * Method is synchronized so thread concurrent safe.
     *
     * @return
     */
    protected synchronized SQLiteDatabase getDB() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = this.getWritableDatabase();
        }
        return mDB;
    }

    public void closeDB() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
        }
    }
}
