package org.example.source.DAO;

import org.example.source.database.connectDatabase;

import java.sql.*;

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
                String userQuery = "SELECT role FROM user WHERE username = ? AND password = ?"; // Lấy role từ bảng user
                try (PreparedStatement ps = con.prepareStatement(userQuery)) {
                    ps.setString(1, username);
                    ps.setString(2, password); // Lưu ý: Nên băm mật khẩu trước khi so sánh
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            userType = rs.getString("role");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc xử lý lỗi theo cách khác
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
        }
        return name.toString();
    }
}