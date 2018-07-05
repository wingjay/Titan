package com.wingjay.titan_android.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class BookServiceServer extends Service {

    private boolean loggingEnable = true;

    private Binder stub = new BookServiceStub(this);

    @Override
    public void onCreate() {
        super.onCreate();
        new LoggingWorker().start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log("onBind");
        return stub;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        loggingEnable = false;
        super.onDestroy();
    }

    class LoggingWorker extends Thread {
        @Override
        public void run() {
            super.run();
            while (loggingEnable) {
                log("logging running");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void log(String msg) {
        Log.d("jaydebug", msg);
    }
}
