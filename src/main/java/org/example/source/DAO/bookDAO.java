package org.example.source.DAO;

import java.util.ArrayList;

// DAO model use for handle data from database with content relative to book
public interface bookDAO<T> {
    // Function use for get data of book from database
    ArrayList<T> getBookData();

    // Function use for update data of programming book
    void updateBook(int id, String bookImage, String bookName, int bookQuantity);

    // Function use for check existing of bookId in database
    boolean findBookId(int id);

    // Function use for check existing of link image book in database
    boolean finLinkImage(String bookImage);

    void insertBook(int id, String bookImage, String bookName, int bookQuantity);
}
