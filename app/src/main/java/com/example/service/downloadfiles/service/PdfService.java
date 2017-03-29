package com.example.service.downloadfiles.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

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

    ArrayList<String> url_list;

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        url_list=intent.getStringArrayListExtra("url_list");

        for(int i=0;i<url_list.size();i++) {
            DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
            downloadFilesTask.execute(url_list.get(i));
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class DownloadFilesTask extends AsyncTask<String,Void,Void>{

        String LOG_TAG=DownloadFilesTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection urlConnection;

            try{
                String str_url=strings[0];
                URL url=new URL(str_url);

                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //int file_prog=urlConnection.getContentLength();

                File sd_root= new File("/sdcard/"+"download/");
                File new_file=new File(sd_root,str_url.substring(str_url.lastIndexOf('/') + 1));

                InputStream inputStream=new BufferedInputStream(urlConnection.getInputStream());
                OutputStream outputStream=new FileOutputStream(new_file);

                byte file_data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = inputStream.read(file_data)) != -1) {
                    total += count;
                    // publishing the progress....
                    Bundle result = new Bundle();
                    //result.putInt("progress" ,(int) (total * 100 / file_prog));
                    //receiver.send(UPDATE_PROGRESS, resultData);
                    outputStream.write(file_data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            }catch (IOException e){
                Log.e(LOG_TAG,e.toString());
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopSelf();
        }
    }
}
