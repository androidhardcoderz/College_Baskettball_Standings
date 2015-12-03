package com.collegebaskettballstandings.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 11/3/2015.
 */
public class Rankings implements Parcelable {

    private List<Team> top25;
    private String title;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    private String week;

    public Rankings(){
        top25 = new ArrayList<>();
        candidates = new ArrayList<>();
    }

    public List<Team> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Team> candidates) {
        this.candidates = candidates;
    }

    public List<Team> getTop25() {
        return top25;
    }

    public void setTop25(List<Team> top25) {
        this.top25 = top25;
    }

    private List<Team> candidates;

    protected Rankings(Parcel in) {
        title = in.readString();
        week = in.readString();
        if (in.readByte() == 0x01) {
            top25 = new ArrayList<Team>();
            in.readList(top25, Team.class.getClassLoader());
        } else {
            top25 = null;
        }
        if (in.readByte() == 0x01) {
            candidates = new ArrayList<Team>();
            in.readList(candidates, Team.class.getClassLoader());
        } else {
            candidates = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(week);

        if (top25 == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(top25);
        }
        if (candidates == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(candidates);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Rankings> CREATOR = new Parcelable.Creator<Rankings>() {
        @Override
        public Rankings createFromParcel(Parcel in) {
            return new Rankings(in);
        }

        @Override
        public Rankings[] newArray(int size) {
            return new Rankings[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}