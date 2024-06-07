package org.example.source.DAO;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.example.source.database.connectDatabase;
import org.mindrot.jbcrypt.BCrypt;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDataDAO implements userDAO {
    // This function use for check user exist in database
    @Override
    public String check(String username, String password) {
        String userType = "failed"; // Mặc định là đăng nhập thất bại
        try (Connection con = connectDatabase.getConnection()) {
            // 1. Kiểm tra admin
            String adminQuery = "SELECT role FROM admin WHERE username = ? AND password = ?"; // Lấy role từ bảng admin
            try (PreparedStatement ps = con.prepareStatement(adminQuery)) {
                ps.setString(1, username);
                ps.setString(2, password); // Lưu ý: Nên băm mật khẩu trước khi so sánh
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userType = rs.getString("role");
                    }
                }
            }
            // 2. Nếu không phải admin, kiểm tra người dùng thông thường
            if (userType.equals("failed")) {
                String userQuery = "SELECT password, role FROM user WHERE username = ?"; // Lấy role từ bảng user
                try (PreparedStatement ps = con.prepareStatement(userQuery)) {
                    ps.setString(1, username);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String hashedPassword = rs.getString("password"); // Lấy mật khẩu đã băm
                            if (BCrypt.checkpw(password, hashedPassword)) { // So sánh mật khẩu băm
                                userType = rs.getString("role");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Something went wrong");
            alert.setContentText("Something went wrong");
            alert.showAndWait();
        }
        return userType;
    }

    @Override
    public String getUserName(String iduser) {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = connectDatabase.getConnection();
            String sql = "SELECT name FROM user WHERE username = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, iduser);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                name.append(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            /*Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Something went wrong");
            alert.setContentText("Something went wrong");
            alert.showAndWait();*/
        }
        return name.toString();
    }

    @Override
    public void insertUser(int id, String username, String email, String password, String name) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO user VALUES(?,?,?,?,?,?)";
        try (Connection con = connectDatabase.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, hashedPassword);
            ps.setString(5, name);
            ps.setString(6, "user");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Something went wrong");
            alert.setContentText("Something went wrong");
            alert.showAndWait();
        }
    }

    @Override
    public void updateUser(String username, String email, String password, String name) {
        String sql = "UPDATE user SET email = ?, password = ?, name = ? WHERE username = ?";
        try (Connection con = connectDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            // Băm mật khẩu mới trước khi lưu
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            ps.setString(2, hashedPassword);
            ps.setString(3, name);
            ps.setString(4, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Something went wrong");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}