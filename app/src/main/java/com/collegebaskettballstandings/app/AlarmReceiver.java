package com.collegebaskettballstandings.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Scott on 11/23/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        if ("android.intent.action.BOOT_COMPLETED".equals(arg1.getAction())) {
            //Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
            Intent background = new Intent(arg0, BackgroundService.class);
            arg0.startService(background);
        }else{
            //Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
            Intent background = new Intent(arg0, BackgroundService.class);
            arg0.startService(background);
        }

    }

}
