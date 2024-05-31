package org.example.source.DAO;

import org.example.source.model.blogModel;

import java.util.ArrayList;
import java.util.concurrent.Callable;

// This class use for create new task in application to get data of blog content
public class dataBlogFetcher implements Callable<ArrayList<blogModel>> {
    private final blogDataDAO blogDAO; // Inject the DAO dependency

    public dataBlogFetcher(blogDataDAO blogDAO) {
        this.blogDAO = blogDAO;
    }

    @Override
    public ArrayList<blogModel> call() throws Exception {
        return blogDAO.getBlogData(); // Use the DAO to fetch data
    }
}
