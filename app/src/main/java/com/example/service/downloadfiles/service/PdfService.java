package com.example.service.downloadfiles.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;

/**
 * Created by sindhya on 3/27/17.
 */
public class PdfService extends Service {

    private Looper looper;



    private class MessageHandler extends Handler{

        public MessageHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }

            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();

        HandlerThread handlerThread=new HandlerThread("ServiceStartArguements", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


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
}
