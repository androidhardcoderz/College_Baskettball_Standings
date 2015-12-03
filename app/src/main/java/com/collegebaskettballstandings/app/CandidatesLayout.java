package com.collegebaskettballstandings.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Scott on 11/4/2015.
 */
public class CandidatesLayout extends LinearLayout {

    @Bind(R.id.rankTextView)
    TextView rank;
    @Bind(R.id.teamNameTextView) TextView name;
    @Bind(R.id.recordTextView) TextView record;

    public CandidatesLayout(Context context,Team team) {
        super(context);

        bindLayout();

        rank.setText(team.getNumber());
        name.setText(team.getMarket() + " " + team.getName());
        record.setText(team.getFp_votes());
    }

    private void bindLayout() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.team_layout,this,true);
        ButterKnife.bind(this, this);

    }
}
