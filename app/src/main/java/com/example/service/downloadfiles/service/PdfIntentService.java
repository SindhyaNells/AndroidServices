package com.example.service.downloadfiles.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sindhya on 3/26/17.
 */
public class PdfIntentService extends IntentService {

    String LOG_TAG=PdfIntentService.class.getSimpleName();
    public PdfIntentService() {
        super("pdf-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        HttpURLConnection urlConnection;
        showToastMessage("Starting Service");

        try{
            String str_url=intent.getExtras().getString("url");
            URL url=new URL(str_url);

            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //int file_prog=urlConnection.getContentLength();

            File sd_root= new File("/sdcard/"+"download/");
            File new_file=new File(sd_root,"file1.pdf");

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


        /*try{
            Thread.sleep(5000);

        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }*/
    }

    //use handler to access the UI thread
    private void showToastMessage(final String message){
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
