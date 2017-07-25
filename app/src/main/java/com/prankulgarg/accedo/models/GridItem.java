package com.prankulgarg.accedo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.prankulgarg.accedo.Constants;

/**
 * Created by prankulgarg on 7/24/17.
 */

public class GridItem implements Parcelable {
    public static final Parcelable.Creator<GridItem> CREATOR = new Parcelable.Creator<GridItem>() {
        @Override
        public GridItem createFromParcel(Parcel source) {
            return new GridItem(source);
        }

        @Override
        public GridItem[] newArray(int size) {
            return new GridItem[size];
        }
    };
    private int number;
    private int state = Constants.STATE_CLOSE;

    public GridItem() {
    }

    protected GridItem(Parcel in) {
        this.number = in.readInt();
        this.state = in.readInt();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.number);
        dest.writeInt(this.state);
    }
}
