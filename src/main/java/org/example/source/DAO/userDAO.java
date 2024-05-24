package org.example.source.DAO;

public interface userDAO {

    // Method use for check input of login mode between user and admin
    public String check(String a, String b);

    // Method get name of user to support other features
    public String getUserName (String iduser);

    // Method to add new user account to application
    public void insertUser(int id, String username, String email, String password, String name);
}
