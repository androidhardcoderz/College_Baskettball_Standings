package com.collegebaskettballstandings.app;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    private Rankings rankings;

    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();

            /* Update UI: Download Service is Running */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        try {
            /* Sending result back to activity */
            String result = downloadData(getString(R.string.API_FILE_CALL));
            if(result != null && !result.equals(""))
                SavedData.saveJSONData(getApplicationContext(),result);

            receiver.send(STATUS_FINISHED, bundle);
            //unknown host exception no internet detected
        } catch(UnknownHostException uhe){
            bundle.putString(Intent.EXTRA_TEXT, uhe.toString());
            receiver.send(STATUS_ERROR, bundle);

        } catch (Exception e) {
                /* Sending error message back to activity */
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

  private String downloadData(String requestUrl) throws IOException,
            DownloadException {

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;


        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();


        urlConnection.setRequestProperty("Content-Type", "application/json");


        urlConnection.setRequestProperty("Accept", "application/json");


        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();

        if (statusCode == 200) {
            inputStream = new BufferedInputStream(
                    urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            System.out.println(response);
            return response;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }


    private String convertInputStreamToString(InputStream is)
            throws IOException {

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(is));
        String line = "";
        String result = "";

        //read each line contents
        int lineNum = 0;
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        //close stream when finished!
        if(bufferedReader != null){
            bufferedReader.close();
        }


        return result;
    }


    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
