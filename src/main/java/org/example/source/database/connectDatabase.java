package org.example.source.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// This is a class handle connection to database
public class connectDatabase {
    // Connection function
    public static Connection getConnection() {
        Connection con = null;
        try {
            String url = "jdbc:mySQL://localhost:3306/library_project";
            String user = "root";
            String password = "123456";

            con = DriverManager.getConnection(url, user, password);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    // Close connect
    public static void closeConnection(Connection con) {
        try{
            if(con != null) {
                con.close();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
