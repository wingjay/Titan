package com.wingjay.titan_android.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wingjay.titan_android.aidl.shared.Book;
import com.wingjay.titan_android.aidl.shared.ITestService;

import java.util.ArrayList;
import java.util.List;

public class TestServiceNative extends Service {

    private static final String CALLING_PACKAGE_FILTER = "com.wingjay.titan_android";
    private boolean loggingEnable = true;
    private List<Book> bookList = new ArrayList<>();

    private Binder binder = new ITestService.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            // 权限验证，进行包名校验
            String[] packages = TestServiceNative.this.getPackageManager().getPackagesForUid(getCallingUid());
            return packages != null && packages.length > 0 && packages[0].equals(CALLING_PACKAGE_FILTER)
                    && super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        for (int i = 0; i < 10; i++) {
            Book b = new Book(i, "Book#" + i);
            bookList.add(b);
        }
        new LoggingWorker().start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log("onBind");
        return binder;
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
