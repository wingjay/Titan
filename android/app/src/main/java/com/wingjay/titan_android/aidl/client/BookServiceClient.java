package com.wingjay.titan_android.aidl.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.wingjay.titan_android.aidl.shared.Book;
import com.wingjay.titan_android.aidl.shared.IBookService;

import java.util.List;

/**
 * manage BookService lifecycle, bind/unbind
 */
public class BookServiceClient implements ServiceConnection {

    private static BookServiceClient inst;
    private static final Object lock = new Object();

    private static Context appContext;
    private IBookService bookService;
    private BookServiceClient() {}
    public static BookServiceClient getInstance(Context context) {
        appContext = context.getApplicationContext();
        if (inst == null) {
            synchronized (lock) {
                if (inst == null) {
                    inst = new BookServiceClient();
                }
            }
        }
        return inst;
    }

    public void start() {
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.bindService(i, this, Context.BIND_AUTO_CREATE);
    }

    public void stop() {
        if (bookService != null) {
            appContext.unbindService(this);
        }
    }

    public void addBook(Book book) {
        if (bookService == null) {
            start();
            return;
        }
        try {
            bookService.addBook(book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBookList() {
        try {
            return bookService.getBookList();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        bookService = IBookService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bookService = null;
    }
}
