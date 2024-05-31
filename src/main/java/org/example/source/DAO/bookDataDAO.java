package org.example.source.DAO;

import javafx.scene.control.Alert;
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Something went wrong");
            alert.showAndWait();
            e.printStackTrace();
        }
        return bookDatabase;
    }

    @Override
    public void updateBook(int bookId, String bookImage, String bookName, int bookQuantity) {

        String sql = "UPDATE programmingBook SET bookImage = ?, bookName = ?, bookQuantity = ? WHERE bookId = ?";

        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bookImage);
            ps.setString(2, bookName);
            ps.setInt(3, bookQuantity);
            ps.setInt(4, bookId);
            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Successfully");
            alert.setContentText("Updated information successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertBook(int id, String bookImage, String bookName, int bookQuantity) {
        String sql = "INSERT INTO programmingBook VALUES(?,?,?,?)";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, bookImage);
            ps.setString(3, bookName);
            ps.setInt(4, bookQuantity);
            ps.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Successfully");
            alert.setContentText("Inserted information successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Something went wrong");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @Override
    public boolean findBookId(int id) {
        String sql = "SELECT bookId FROM programmingBook WHERE bookId = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean finLinkImage(String bookImage) {
        String sql = "SELECT bookImage FROM programmingBook WHERE bookImage = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bookImage);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}