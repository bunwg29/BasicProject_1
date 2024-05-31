package org.example.source.DAO;

public interface userDAO {

    // Method use for check input of login mode between user and admin
    String check(String a, String b);

    // Method get name of user to support other features
    String getUserName(String iduser);

    // Method to add new user account to application
    void insertUser(int id, String username, String email, String password, String name);

    // Method use for update information of user
    void updateUser(String username, String email, String password, String name);
}
