package com.collegebaskettballstandings.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Scott on 11/23/2015.
 */
public class BackgroundService extends Service {

    private final String TAG = BackgroundService.class.getSimpleName();

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        public void run() {

            if(SavedData.getShowNotifications(getApplicationContext()) == true) {
                DownloadURLData downloadURLData = new DownloadURLData();
                try {
                    String result = new InputStreamConverter(context).convertInputStreamToString
                            (downloadURLData.downloadData(context.getString(R.string.API_FILE_CALL)));

                    if (!result.equals(SavedData.getJSONData(context))) {
                        //new data is available
                        Notify notify = new Notify(context);
                        notify.buildNotification();
                        notify.issueNotification();
                        Log.i(TAG, "ISSUED NOTIFICATION");
                    } else {
                        Log.i(TAG, "DATA THE SAME");
                    }
                } catch (DownloadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

}