package org.example.source.DAO;

import org.example.source.model.bookModel;
import org.example.source.model.blogModel;
import java.util.ArrayList;

public class testDAO {
    public static void main(String[] args) {
        ArrayList<bookModel> testBook = new ArrayList<bookModel>();
        bookDataDAO bookDataDAO = new bookDataDAO();
        testBook = bookDataDAO.getBookData();

        for(bookModel book : testBook) {
            System.out.println(book.toString());
        }

    }
}
