package com.collegebaskettballstandings.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.appbrain.AppBrainBanner;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Scott on 10/16/2015.
 */
public class RankingsFragment extends Fragment {

    Rankings rankings;

    private final String CLASS = "Rankings Fragment";
    private LayoutLoaderTask layoutLoaderTask;
    private ProgressBar bar;
    @Bind(R.id.rankingsLayout)
    LinearLayout layout;

    @Bind(R.id.titleTextView)
    TextView title;

    @Bind(R.id.headerTableRow)
    TableRow tableRow;

    @Bind(R.id.rnk) TextView rnk;
    @Bind(R.id.tmn) TextView tmn;
    @Bind(R.id.recordTextView) TextView rcd;

    public interface ITeamLoaded {
        void onTeamLoaded(Team team);
    }

    public RankingsFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rankings_fragment,container,false);

        ButterKnife.bind(this, view); //attach views to butterknife lib

        Underliner.underlineTextViewContents(rnk);
        Underliner.underlineTextViewContents(tmn);
        Underliner.underlineTextViewContents(rcd);

        bar = (ProgressBar) view.findViewById(R.id.progressBar);

        return view;
    }

    public void showData(){

        //start Asynctask to load teams into layout
        layoutLoaderTask = new LayoutLoaderTask();
        layoutLoaderTask.execute(SavedData.getJSONData(getActivity()));
    }


    private void loadLayoutsInView(Team team){

        TeamLayout lay = new TeamLayout(getActivity(),team);
        layout.addView(lay, layout.getChildCount() - 1);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(layoutLoaderTask != null && layoutLoaderTask.getStatus() == AsyncTask.Status.RUNNING){
            layoutLoaderTask.cancel(true);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppBrainBanner banner = new AppBrainBanner(getActivity());
        layout.addView(banner,layout.getChildCount());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    class LayoutLoaderTask extends AsyncTask<String,Team,String>{

        ITeamLoaded iTeamLoaded = new ITeamLoaded() {
            @Override
            public void onTeamLoaded(Team team) {
                publishProgress(team);
            }
        };


        @Override
        protected void onProgressUpdate(Team... values) {
            super.onProgressUpdate(values);
            loadLayoutsInView(values[0]);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            JSONParser parser = new JSONParser(iTeamLoaded);

            try {
                parser.parseResult(params[0]);
                return new JSONObject(params[0]).getJSONObject("poll").getString("name") + "\n Week: "
                        + new JSONObject(params[0]).getString("week").replace("W","");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            title.setText(s); //set title of data!
            bar.setVisibility(View.INVISIBLE);
        }
    }
}
