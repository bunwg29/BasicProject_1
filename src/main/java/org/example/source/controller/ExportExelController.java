package org.example.source.controller;

import javafx.scene.control.Alert;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.source.database.connectDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportExelController {
    public void exportborrowExel(File fileselected) {
        try {
            // Conect to database
            Connection conn = connectDatabase.getConnection();

            // Prepare SQL statement
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM borrowlistTotal";
            ResultSet rs = stmt.executeQuery(sql);

            // Create workbook
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Borrowlist");

            // Add title of column
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("idborrow");
            headerRow.createCell(1).setCellValue("usernameLogin");
            headerRow.createCell(2).setCellValue("bookId");
            headerRow.createCell(3).setCellValue("databorrow");
            headerRow.createCell(4).setCellValue("username");
            headerRow.createCell(5).setCellValue("bookName");

            // Add data to table
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getInt("idborrow"));
                row.createCell(1).setCellValue(rs.getString("usernameLogin"));
                row.createCell(2).setCellValue(rs.getInt("bookId"));
                row.createCell(3).setCellValue(rs.getTimestamp("databorrow"));
                row.createCell(4).setCellValue(rs.getString("username"));
                row.createCell(5).setCellValue(rs.getString("bookName"));
            }

            // Save exel file
            FileOutputStream fileOut = new FileOutputStream(fileselected);
            workbook.write(fileOut);
            fileOut.close();

            // Close conection
            rs.close();
            stmt.close();
            conn.close();
            System.out.println("Export exel successfully");

            System.out.println("Export exel successfully!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Successfully");
            alert.setContentText("Export info about borrow book successfully!");
            alert.showAndWait();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void exportbackExel(File fileselected) {
        try {
            // Conect to database
            Connection conn = connectDatabase.getConnection();

            // Prepare SQL statement
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM backlistTotal";
            ResultSet rs = stmt.executeQuery(sql);

            // Create workbook
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Backlist");

            // Add title column
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("idborrow");
            headerRow.createCell(1).setCellValue("usernameLogin");
            headerRow.createCell(2).setCellValue("bookId");
            headerRow.createCell(3).setCellValue("bookName");
            headerRow.createCell(4).setCellValue("databorrow");

            // Add data to Workbook and worksheet
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getInt("idborrow"));
                row.createCell(1).setCellValue(rs.getString("usernameLogin"));
                row.createCell(2).setCellValue(rs.getInt("bookId"));
                row.createCell(3).setCellValue(rs.getString("bookName"));
                row.createCell(4).setCellValue(rs.getTimestamp("databorrow"));
            }

            // Save exel file
            FileOutputStream fileOut = new FileOutputStream(fileselected);
            workbook.write(fileOut);
            fileOut.close();

            // Close connection
            rs.close();
            stmt.close();
            conn.close();

            System.out.println("Export exel successfully!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Successfully");
            alert.setContentText("Export info about return book successfully!");
            alert.showAndWait();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
