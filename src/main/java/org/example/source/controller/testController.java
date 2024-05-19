package org.example.source.controller;

import org.example.source.DAO.blogDataDAO;
import org.example.source.model.blogModel;


import java.util.ArrayList;


public class testController {
    public static void main(String[] args) {
       ArrayList<blogModel> list = new ArrayList<blogModel>();
       blogDataDAO blogData = new blogDataDAO();
       list = blogData.getBlogData();
       for (blogModel blogModel : list) {
           System.out.println(blogModel.toString());
       }

    }
}
