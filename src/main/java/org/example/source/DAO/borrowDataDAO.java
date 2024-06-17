package org.example.source.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.example.source.DTO.BackBookDTO;
import org.example.source.DTO.BorrowListDTO;
import org.example.source.database.connectDatabase;
import org.example.source.model.borrowModel;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class borrowDataDAO implements borrowDAO {
    ObservableList<borrowModel> borrowModels = FXCollections.observableArrayList();

    @Override
    public int insertBookBorrow(String iduser, int bookId) {
        String insertSql = "INSERT INTO borrowlist (iduser, bookId, databorrow, dayEx) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE programmingBook SET bookQuantity = bookQuantity - 1 WHERE bookId = ?";
        LocalDateTime dateBorrow = LocalDateTime.now();
        LocalDateTime dateEx = LocalDateTime.now().plusDays(7);
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement insertPs = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updatePs = con.prepareStatement(updateSql)) {

            con.setAutoCommit(false);

            try {

                insertPs.setString(1, iduser);
                insertPs.setInt(2, bookId);
                insertPs.setTimestamp(3, Timestamp.valueOf(dateBorrow));
                insertPs.setTimestamp(4, Timestamp.valueOf(dateEx));
                insertPs.executeUpdate();

                updatePs.setInt(1, bookId);
                int rowsAffected = updatePs.executeUpdate();

                if (rowsAffected == 0) {
                    con.rollback();
                    return -2;
                }

                try (ResultSet generatedKeys = insertPs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idborrow = generatedKeys.getInt(1);

                        con.commit();

                        return idborrow;
                    }
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void backBook(String name, int bookId, String bookName) {
        String sql = "INSERT INTO backBook (userName, bookId, bookName, dateBack) VALUES (?, ?, ?, ?)";
        LocalDateTime dateBack = LocalDateTime.now().plusDays(1); // Không cộng thêm 1 ngày nữa

        try (Connection conn = connectDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setString(1, name);
            pstmt.setInt(2, bookId);
            pstmt.setString(3, bookName);
            pstmt.setTimestamp(4, Timestamp.valueOf(dateBack));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error when insert data: " + e.getMessage());
        }
    }
    @Override
    public String findNameBook(int bookId) {
        StringBuilder bookName = new StringBuilder();
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.bookName \n" +
                             "FROM borrowlist b\n" +
                             "JOIN programmingBook p ON b.bookId = p.bookId\n" +
                             "WHERE b.bookId = ?")) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookName.append(rs.getString("bookName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookName.toString();
    }

    @Override
    public int checkBookQuantity(int bookId) {
        int quantity = 0;
        String sql = "SELECT bookQuantity FROM programmingBook WHERE bookId = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quantity = rs.getInt("bookQuantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantity;
    }

    @Override
    public boolean checkRequestBack(int bookId) {
        String sql = "SELECT bookId FROM backBook WHERE bookId = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<BorrowListDTO> getBorrowListData() {
        List<BorrowListDTO> borrowList = new ArrayList<>();
        String sql = "SELECT b.*, u.name AS userName, pb.bookName AS bookName " +
                "FROM borrowlist b " +
                "JOIN user u ON b.iduser = u.username " +
                "JOIN programmingBook pb ON b.bookId = pb.bookId";

        try (Connection con = connectDatabase.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                BorrowListDTO borrowListItem = new BorrowListDTO();
                borrowListItem.setIdBorrow(resultSet.getInt("idborrow"));
                borrowListItem.setIdUser(resultSet.getString("iduser")); // Assuming iduser in borrowlist is actually username (String)
                borrowListItem.setBookId(resultSet.getInt("bookId"));
                borrowListItem.setUserName(resultSet.getString("userName"));
                borrowListItem.setBookName(resultSet.getString("bookName"));
                // Set the dayEx value
                borrowListItem.setDayEx(resultSet.getTimestamp("dayEx"));
                borrowListItem.setDateBorrow(resultSet.getTimestamp("databorrow"));
                borrowList.add(borrowListItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowList;
    }

    @Override
    public void insertToBorrowListTotal(int idborrow, String usernameLogin, int bookId, Timestamp borrowDate, String username, String bookName) {
        String sql = "INSERT INTO borrowlistTotal VALUES(?, ?, ?, ?, ?, ?)";
        String checkSql = "SELECT 1 FROM borrowlistTotal WHERE usernameLogin = ? AND bookId = ? AND databorrow = ?"; // Câu lệnh kiểm tra

        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            // Kiểm tra xem dữ liệu đã tồn tại hay chưa
            checkPs.setString(1, usernameLogin);
            checkPs.setInt(2, bookId);
            checkPs.setTimestamp(3, borrowDate);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) { // Nếu dữ liệu đã tồn tại
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Data already exists");
                alert.setContentText("This borrowing request is already in the system.");
                alert.showAndWait();
            } else { // Nếu dữ liệu chưa tồn tại
                ps.setInt(1, idborrow);
                ps.setString(2, usernameLogin);
                ps.setInt(3, bookId);
                ps.setTimestamp(4, borrowDate);
                ps.setString(5, username);
                ps.setString(6, bookName);
                int rowEffect = ps.executeUpdate();
                if (rowEffect > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ADMIN LIBRARY");
                    alert.setHeaderText("Successfully");
                    alert.setContentText("Approve request successfully");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Something went wrong");
                    alert.setContentText("Something went wrong");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<BackBookDTO> getBackBookData() {
        ObservableList<BackBookDTO> data = FXCollections.observableArrayList();
        String sql = "SELECT * FROM backBook"; // SQL query to retrieve data

        try (Connection connection = connectDatabase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BackBookDTO backBook = new BackBookDTO();
                backBook.setBorrowId(resultSet.getInt("borrowId"));
                backBook.setName(resultSet.getString("username"));
                backBook.setBookId(resultSet.getInt("bookId"));
                backBook.setBookName(resultSet.getString("bookName"));
                backBook.setDateBack(resultSet.getTimestamp("dateBack"));
                data.add(backBook);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    @Override
    public ObservableList<borrowModel> listBorrow(String iduserr) {
        borrowModels.clear();
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM borrowlist WHERE iduser = ?")) {
            ps.setString(1, iduserr);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idborrow = rs.getInt("idborrow");
                String iduser = rs.getString("iduser");
                String bookId = rs.getString("bookId");
                Timestamp databorrow = rs.getTimestamp("databorrow");
                borrowModels.add(new borrowModel(idborrow, iduser, bookId, databorrow));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowModels;
    }

    @Override
    public void inserttoBackListTotal(int idborrow, String usernameLogin, int bookId, String bookName) {
        String sql = "INSERT INTO backlistTotal VALUES(?, ?, ?, ?, ?)";
        String updateSql = "UPDATE programmingBook SET bookQuantity = bookQuantity + 1 WHERE bookId = ?";
        String deleteBackList = "DELETE FROM backBook WHERE bookId = ? AND userName = ?";
        String deleteBorrowList = "DELETE FROM borrowlist WHERE bookId = ? AND iduser = (SELECT username FROM user WHERE name = ?)";
        LocalDateTime dateBack = LocalDateTime.now();
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement ps1 = con.prepareStatement(updateSql);
             PreparedStatement ps2 = con.prepareStatement(deleteBackList);
             PreparedStatement ps3 = con.prepareStatement(deleteBorrowList)
        ) {
            ps.setInt(1, idborrow);
            ps.setString(2, usernameLogin);
            ps.setInt(3, bookId);
            ps.setString(4, bookName);
            ps.setTimestamp(5, Timestamp.valueOf(dateBack));

            ps.executeUpdate();

            ps1.setInt(1, bookId);
            ps1.executeUpdate();

            ps2.setInt(1, bookId);
            ps2.setString(2, usernameLogin);
            ps2.executeUpdate();

            ps3.setInt(1, bookId);
            ps3.setString(2, usernameLogin); // Set usernameLogin for the subquery
            ps3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long checkExpiration(String iduser, int bookId) {
        long taxes = 0;
        LocalDateTime now = LocalDateTime.now();
        String sql = "SELECT dayEx FROM borrowlist WHERE iduser = ? AND bookId = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, iduser);
            ps.setInt(2, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp dayExTimestamp = rs.getTimestamp("dayEx");
                LocalDateTime dayExDateTime = dayExTimestamp.toLocalDateTime();

                if (dayExDateTime.isBefore(now)) {
                    long daysBetween = ChronoUnit.DAYS.between(dayExDateTime, now);
                    taxes = daysBetween * 10000;
                    System.out.println("This book expires in " + daysBetween + " days.");
                } else if (dayExDateTime.isAfter(now)) {
                    taxes = 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taxes;
    }
}

