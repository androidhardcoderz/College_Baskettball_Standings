package com.collegebaskettballstandings.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Scott on 11/3/2015.
 */
public class Team implements Parcelable {

    private String id;
    private String name;
    private String market;
    private String rank;
    private String wins;
    private String losses;
    private String votes;
    private String number;

    public String getPrev_rank() {
        return prev_rank;
    }

    public void setPrev_rank(String prev_rank) {
        this.prev_rank = prev_rank;
    }

    private String prev_rank;

    public Team(){

    }

    public String getFp_votes() {
        return fp_votes;
    }

    public void setFp_votes(String fp_votes) {
        this.fp_votes = fp_votes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    private String points;
    private String fp_votes;

    protected Team(Parcel in) {
        id = in.readString();
        name = in.readString();
        market = in.readString();
        rank = in.readString();
        wins = in.readString();
        losses = in.readString();
        points = in.readString();
        fp_votes = in.readString();
        prev_rank = in.readString();
        votes = in.readString();
        number = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(market);
        dest.writeString(rank);
        dest.writeString(wins);
        dest.writeString(losses);
        dest.writeString(points);
        dest.writeString(fp_votes);
        dest.writeString(prev_rank);
        dest.writeString(votes);
        dest.writeString(number);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}