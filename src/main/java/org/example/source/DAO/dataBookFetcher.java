package org.example.source.DAO;

import org.example.source.model.bookModel;

import java.util.ArrayList;
import java.util.concurrent.Callable;
// This class use for create new task in application to get data of book content
public class dataBookFetcher implements Callable<ArrayList<bookModel>> {
    private final bookDataDAO bookDAO;
    public dataBookFetcher(bookDataDAO bookDAO) {
        this.bookDAO = bookDAO; // Inject the DAO dependency
    }

    @Override
    public ArrayList<bookModel> call() throws Exception {
        return bookDAO.getBookData(); // Use the DAO to fetch data
    }
}
