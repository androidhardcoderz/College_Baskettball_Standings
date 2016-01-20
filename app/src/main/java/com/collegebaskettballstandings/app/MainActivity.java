package com.collegebaskettballstandings.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements DownloadResultReceiver.Receiver{

    private DownloadResultReceiver mReceiver;
    private RankingsFragment rankingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //start background intent
            /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadService.class);

            /* Send optional extras to Download IntentService */
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        startService(intent);

        rankingsFragment = (RankingsFragment) getSupportFragmentManager().findFragmentById(R.id.rankfrag);

    }

    private void showDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Current Rankings");

        // set dialog message
        alertDialogBuilder
                .setTitle("Error")
                .setMessage("No Internet Connection Please Connect And Try Again")
                .setCancelable(false)
                .setIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.ic_launcher))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                        MainActivity.this.finish();

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
                rankingsFragment.showData(SavedData.getJSONData(MainActivity.this));
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

}
