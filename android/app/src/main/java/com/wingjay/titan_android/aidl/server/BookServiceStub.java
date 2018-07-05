package com.wingjay.titan_android.aidl.server;

import android.content.Context;
import android.os.Parcel;
import android.os.RemoteException;

import com.wingjay.titan_android.aidl.shared.Book;
import com.wingjay.titan_android.aidl.shared.IBookService;

import java.util.ArrayList;
import java.util.List;

/**
 * running in book process, implement all ITestService logic
 */
public class BookServiceStub extends IBookService.Stub {

    private static final String CALLING_PACKAGE_FILTER = "com.wingjay.titan_android";
    private Context context;
    private List<Book> bookList = new ArrayList<>();

    BookServiceStub(Context ctx) {
        context = ctx;

        for (int i = 0; i < 10; i++) {
            Book b = new Book(i, "Book#" + i);
            bookList.add(b);
        }
    }

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
            String[] packages = context.getPackageManager().getPackagesForUid(getCallingUid());
            return packages != null && packages.length > 0 && packages[0].equals(CALLING_PACKAGE_FILTER)
                    && super.onTransact(code, data, reply, flags);
    }
}
