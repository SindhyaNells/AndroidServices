package com.example.service.downloadfiles.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.service.downloadfiles.R;
import com.example.service.downloadfiles.activity.BoundedDownloadActivity;
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
 * Created by sindhya on 3/29/17.
 */
public class PdfBoundService extends Service {

    private final IBinder local_binder=new LocalBinder();

    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        displayNotificationMessage("Downloading files");

    }

    public class LocalBinder extends Binder{
        public PdfBoundService getService(){
            return PdfBoundService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {

        return local_binder;
    }

    public void DownloadFiles(ArrayList<String> url_list){

        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute(url_list);
    }

    private class DownloadFilesTask extends AsyncTask<ArrayList<String>,Integer,Void> {

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

            Log.d("Loading ", String.valueOf(progress[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //progressBar.dismiss();
        }
    }

    private void displayNotificationMessage(String message)
    {
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, BoundedDownloadActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(message)
                .setContentText("Downloading files in progress..")
                .setSmallIcon(R.drawable.emo_im_winking)
                .setContentIntent(contentIntent);

        notificationBuilder.setProgress(0, 0, false);
        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancelAll();
    }
}
