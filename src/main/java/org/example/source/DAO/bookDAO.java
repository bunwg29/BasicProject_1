package org.example.source.DAO;

import java.util.ArrayList;
// DAO model use for handle data from database with content relative to book
public interface bookDAO<T>{
    // Function use for get data of book from database
    public ArrayList<T> getBookData();

}
