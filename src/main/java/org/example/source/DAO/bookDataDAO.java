package org.example.source.DAO;

import org.example.source.database.connectDatabase;
import org.example.source.model.bookModel;

import java.sql.*;
import java.util.ArrayList;
//This class support handle get data of book content in application
public class bookDataDAO implements bookDAO<bookModel> {
    bookModel book = null;
    ArrayList<bookModel> bookDatabase = new ArrayList<bookModel>();

    // Get book data content support for display book at home dashboard
    @Override
    public ArrayList<bookModel> getBookData() {
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
        return bookDatabase;
    }




}