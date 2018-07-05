// IBookService.aidl
package com.wingjay.titan_android.aidl.shared;

// Declare any non-default types here with import statements
import com.wingjay.titan_android.aidl.shared.Book;

interface IBookService {
    List<Book> getBookList();
    void addBook(in Book book);
}
