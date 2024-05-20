package org.example.source.DAO;

import org.example.source.model.blogModel;
import org.example.source.database.connectDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class blogDataDAO implements BlogDAO<blogModel> {

    @Override
    public ArrayList<blogModel> getBlogData() {
        ArrayList<blogModel> blogDatabase = new ArrayList<>();
        try (Connection con = connectDatabase.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM blog")) {

            while (rs.next()) {
                String blogName = rs.getString("blogName");
                String blogLink = rs.getString("blogLink");
                String blogImageLink = rs.getString("blogImageLink");

                blogModel blog = new blogModel(blogName, blogLink, blogImageLink);
                blogDatabase.add(blog);
            }
            return blogDatabase;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Or throw a custom exception
        }
    }
}