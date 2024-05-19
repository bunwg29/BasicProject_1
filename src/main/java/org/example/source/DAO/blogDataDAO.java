package org.example.source.DAO;

import  org.example.source.model.blogModel;
import org.example.source.database.connectDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class blogDataDAO implements BlogDAO<blogModel>, Runnable{
    ArrayList<blogModel> blogDatabase = new ArrayList<blogModel>();
    blogModel blogModel = null;
    @Override
    public ArrayList<blogModel> getBlogData() {
        Thread thread = new Thread(this);
        thread.run();
        return blogDatabase;
    }

    @Override
    public void run() {
        try {
            Connection con = connectDatabase.getConnection();
            String sql = "SELECT * FROM blog";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String blogName = rs.getString("blogName");
                String blogLink = rs.getString("blogLink");
                String blogImageLink = rs.getString("blogImageLink");

                blogModel = new blogModel(blogName, blogLink, blogImageLink);
                blogDatabase.add(blogModel);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
