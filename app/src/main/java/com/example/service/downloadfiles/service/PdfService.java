package com.example.service.downloadfiles.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.example.service.downloadfiles.R;
import com.example.service.downloadfiles.activity.DownloadActivity;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.service.downloadfiles.activity.DownloadActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sindhya on 3/27/17.
 */
public class PdfService extends Service {

    //ProgressDialog progressBar;
    ArrayList<String> url_list;

    private NotificationManager notificationManager;

    @Override
    public void onCreate(){
        super.onCreate();
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        displayNotificationMessage("Downloading files");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        url_list=intent.getStringArrayListExtra("url_list");


        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute(url_list);

        /*progressBar=new ProgressDialog(getApplication().getApplicationContext());
        progressBar.setTitle("Downloading Files");
        progressBar.setMessage("Downloading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setMax(100);
        progressBar.setCancelable(true);
        progressBar.show();*/


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancelAll();
    }


    private class DownloadFilesTask extends AsyncTask<ArrayList<String>,Integer,Void>{

        String LOG_TAG=DownloadFilesTask.class.getSimpleName();

        ArrayList<String> url_list;

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {

            url_list = arrayLists[0];
            for (int i = 0; i < url_list.size(); i++) {
                String url = url_list.get(i);
                downloadFile(url);
            }

            return null;
        }

        private void downloadFile(String url){

            HttpURLConnection urlConnection;

            try{
                String str_url=url;

                URL pdf_url=new URL(str_url);

                urlConnection=(HttpURLConnection)pdf_url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int file_len=urlConnection.getContentLength();

                File sd_root= new File("/sdcard/"+"download/");
                File new_file=new File(sd_root,str_url.substring(str_url.lastIndexOf('/') + 1));

                InputStream inputStream=new BufferedInputStream(urlConnection.getInputStream());
                OutputStream outputStream=new FileOutputStream(new_file);

                byte file_data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = inputStream.read(file_data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / file_len));
                    //updateProgress(file_len,total);
                    outputStream.write(file_data, 0, count);

                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            }catch (IOException e){
                Log.e(LOG_TAG,e.toString());
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            //progressBar.setProgress(progress[0]);
            /*if(url_list != null) {
                progressBar.setMessage("Loading " + (rowItems.size()+1) + "/" + noOfURLs);
            }*/

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("DOWNLOAD_PROGRESS");
            broadcastIntent.putExtra("progress",progress[0]);
            sendBroadcast(broadcastIntent);

            Log.d("Loading ", String.valueOf(progress[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopSelf();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("FILE_DOWNLOAD");
            getBaseContext().sendBroadcast(broadcastIntent);

            //progressBar.dismiss();
        }
    }

    private void displayNotificationMessage(String message)
    {
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, DownloadActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(message)
                .setContentText("Downloading files in progress..")
                .setSmallIcon(R.drawable.emo_im_winking)
                .setContentIntent(contentIntent);

        notificationBuilder.setProgress(0, 0, false);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
