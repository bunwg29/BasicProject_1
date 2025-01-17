package org.example.source.DAO;

import java.util.ArrayList;

// DAO model use for handle data from database with content relative to blog
public interface blogDAO<T> {
    // Function use for get data of blogs from database
    ArrayList<T> getBlogData();
}
