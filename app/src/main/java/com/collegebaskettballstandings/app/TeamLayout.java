package com.collegebaskettballstandings.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Scott on 10/16/2015.
 */
public class TeamLayout extends LinearLayout {

    @Bind(R.id.rankTextView) TextView rank;
    @Bind(R.id.teamNameTextView) TextView name;
    @Bind(R.id.recordTextView) TextView record;

    public TeamLayout(Context context,Team team) {
        super(context);

       bindLayout();
        assignData(team);

    }



    private void assignData(Team team) {

        rank.setText(team.getRank());
        name.setText(team.getMarket() + " " + team.getName());

        if(team.getLosses() == null || team.getWins() == null){
            record.setText("N/A");
        }else{
            record.setText(team.getWins() + "-" + team.getLosses());
        }

    }

    private void bindLayout() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.team_layout,this,true);
        ButterKnife.bind(this, this);
    }

}
