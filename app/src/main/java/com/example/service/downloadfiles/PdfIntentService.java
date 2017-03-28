package com.example.service.downloadfiles;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by sindhya on 3/26/17.
 */
public class PdfIntentService extends IntentService {

    public PdfIntentService() {
        super("pdf-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        showToastMessage("Starting Service");
        try{
            Thread.sleep(5000);

        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
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
