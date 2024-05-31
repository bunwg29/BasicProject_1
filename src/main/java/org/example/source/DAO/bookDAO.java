package org.example.source.DAO;

import java.util.ArrayList;
// DAO model use for handle data from database with content relative to book
public interface bookDAO<T>{
    // Function use for get data of book from database
    public ArrayList<T> getBookData();

   // Function use for update data of programming book
   public void updateBook(int id, String bookImage, String bookName, int bookQuantity);
   // Function use for check existing of bookId in database
   public boolean findBookId(int id);
   // Function use for check existing of link image book in database
    public boolean finLinkImage(String bookImage);
   public void insertBook(int id, String bookImage, String bookName, int bookQuantity);
}
