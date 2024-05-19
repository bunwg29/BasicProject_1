package org.example.source.database;

import java.sql.Connection;

public class testConnect {
    public static void main(String[] args) {
        Connection testConnect = connectDatabase.getConnection();
        System.out.println("Connect Sucessfully");
        connectDatabase.closeConnection(testConnect);
    }
}
