package org.example.source.DAO;

import org.example.source.database.connectDatabase;
import org.example.source.model.bookModel;

import java.sql.*;
import java.util.ArrayList;

public class bookDataDAO implements BookDAO<bookModel>, Runnable {
    bookModel book = null;
    ArrayList<bookModel> bookDatabase = new ArrayList<bookModel>();

    @Override
    public ArrayList<bookModel> getBookData() {
        Thread thread = new Thread(this);
        thread.run();
        return bookDatabase;
    }
    @Override
    public void run() {
        try {
            Connection con = connectDatabase.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM programmingBook";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String bookId = rs.getString("bookId");
                String imageSrc = rs.getString("bookImage");
                String bookName = rs.getString("bookName");
                String bookQuantity = String.valueOf(rs.getInt("bookQuantity"));
                book = new bookModel(bookId, imageSrc, bookName, bookQuantity);
                bookDatabase.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}