package com.collegebaskettballstandings.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 11/21/2015.
 */
public class JSONParser {

    RankingsFragment.ITeamLoaded iTeamLoaded;

    public JSONParser(RankingsFragment.ITeamLoaded iTeamLoaded){
        this.iTeamLoaded = iTeamLoaded;

    }

    public List<Team> parseResult(String string) throws JSONException {

        List<Team> teams = new ArrayList<>();

        JSONArray rankingsArray = new JSONObject(string).getJSONArray("rankings");

        System.out.println(rankingsArray.length() + " LENGTH OF RANKINGS ARRAY");

        for(int i = 0; i < 25;i++){

            JSONObject teamObject = rankingsArray.getJSONObject(i);

            Team team = new Team();

            try{

                team.setId(teamObject.getString("id"));
                team.setName(teamObject.getString("name"));
                team.setMarket(teamObject.getString("market"));
                team.setRank(teamObject.getString("rank"));
                team.setWins(teamObject.getString("wins"));
                team.setLosses(teamObject.getString("losses"));
                getPreviousRanking(teamObject, team);

                System.out.println(team.getName() + " " + i);


            }catch(JSONException jse){
                jse.printStackTrace();
            }

            teams.add(team);

        }

        return teams;
    }

    private void getPreviousRanking(JSONObject teamObject, Team team)  {
        try {
            team.setPrev_rank(teamObject.getString("prev_rank"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseCandidates(String string) throws JSONException {
        //CANDIADATES TO BE ADDED IN THE FUTURE

        JSONArray candidatesArray = new JSONObject(string).getJSONArray("candidates");
        for(int a = 0; a < candidatesArray.length();a++){

            JSONObject candidatesObject = candidatesArray.getJSONObject(a);
            Team team = new Team();
            try {
                team.setNumber((a +1) + "");
                team.setId(candidatesObject.getString("id"));
                team.setName(candidatesObject.getString("name"));
                team.setMarket(candidatesObject.getString("market"));
                team.setWins(candidatesObject.getString("wins"));
                team.setLosses(candidatesObject.getString("losses"));
                team.setFp_votes(candidatesObject.getString("votes"));

                iTeamLoaded.onTeamLoaded(team);
            }catch(JSONException jsonException){
                jsonException.printStackTrace();
            }

            /*
               "candidates": [{
                "id": "0d037a5d-827a-44dd-8b70-57603d671d5d",
                "name": "Utes",
                "market": "Utah",
                "wins": 4,
                "losses": 1,
                "prev_rank": 16,
                "votes": 95
            }, {
             */

        }
    }
}
