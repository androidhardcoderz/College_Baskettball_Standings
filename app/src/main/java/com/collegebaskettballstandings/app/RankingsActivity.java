package com.collegebaskettballstandings.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class RankingsActivity extends AppCompatActivity implements DownloadResultReceiver.Receiver {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private RankingsFragment rankingsFragment;
    private CanidatesFragment canidatesFragment;
    private final int ALARM_REPEAT = 3600000; //1 hours in MS
    private CheckBox notificationsBox;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private DownloadResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        notificationsBox = (CheckBox) findViewById(R.id.notificationCheckBox);

        //create broadcast receiver with pending intent to run when user opens app
        Intent alarm = new Intent(this, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);

        //if current alarm s not running create a new one and set repeating time
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //set repeating alarm to fire every 6 hours if data is new on server issue notification
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), ALARM_REPEAT * 2, pendingIntent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //start background intent
            /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadService.class);

            /* Send optional extras to Download IntentService */
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        startService(intent);

        rankingsFragment = new RankingsFragment();
        canidatesFragment = new CanidatesFragment();

        //if first run = true, and savedinstance state is null
        if(SavedData.isFirstRun(this) == true && savedInstanceState == null){
            showNotificationDialog();
        }

        notificationsBox.setChecked(SavedData.getShowNotifications(this));
        notificationsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SavedData.setShowNotifications(RankingsActivity.this,isChecked);
            }
        });
    }

    private void showDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RankingsActivity.this);

        // set title
        alertDialogBuilder.setTitle("Current Rankings");

        // set dialog message
        alertDialogBuilder
                .setTitle("Error")
                .setMessage("No Internet Connection Please Connect And Try Again")
                .setCancelable(false)
                .setIcon(ContextCompat.getDrawable(RankingsActivity.this, R.mipmap.ic_launcher))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        RankingsActivity.this.finish();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case DownloadService.STATUS_RUNNING:
                break;
            case DownloadService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                rankingsFragment.showData();
                canidatesFragment.showData();
                break;
            case DownloadService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                if(error.contains("UnknownHostException")){
                    showDialog();
                }
                System.out.println(error);
                break;
        }
    }

    private void showNotificationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        //alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setTitle("Notifications?")
                .setMessage("Receive Notifications For New Rankings?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //set the variable holding the firstrun flag
                        SavedData.setFirstRun(RankingsActivity.this,false);
                        SavedData.setShowNotifications(RankingsActivity.this,true);
                        notificationsBox.setChecked(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //set the first run flag to false
                        SavedData.setFirstRun(RankingsActivity.this,false);
                        SavedData.setShowNotifications(RankingsActivity.this, false);
                        notificationsBox.setChecked(false);
                    }
                })
                .setNeutralButton("Ask Me Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do not do anything will pop up next app run until user
                        //selects yes or no
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return rankingsFragment;
            }else{
                return canidatesFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TOP 25";
                case 1:
                    return "Candidates";
                default:
                    return "";
            }
        }
    }
}
