package org.example.source.Sever_Client;

import com.google.gson.Gson;
import org.example.source.DAO.*;
import org.example.source.controller.validateController;
import org.example.source.model.blogModel;
import org.example.source.model.bookModel;
import org.example.source.model.borrowModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SeverProcess {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private bookDataDAO bookDataDAO;
    private blogDataDAO blogDataDAO;
    private borrowDataDAO borrowDataDAO;
    private userDataDAO userDataDAO;
    private String currentUsername;
    public SeverProcess(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);

            // Khởi tạo bookDataDAO
            bookDataDAO = new bookDataDAO();
            blogDataDAO = new blogDataDAO();
            borrowDataDAO = new borrowDataDAO();
            userDataDAO = new userDataDAO();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }
    }

    public void start() {
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                handleClient();
            } catch (IOException e) {
                System.err.println("Accept failed: " + e.getMessage());
            }
        }
    }

    private void handleClient() {
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client: " + inputLine);
                if (inputLine.startsWith("login:")) {
                    currentUsername = inputLine.substring(6); // Lấy username từ chuỗi yêu cầu
                    out.println("login:success");
                }
                else if (inputLine.startsWith("borrow:")) {
                    String bookId = inputLine.substring(7);
                    int bookIdInt = Integer.parseInt(bookId);
                    if (borrowDataDAO.checkBookQuantity(bookIdInt) > 0) {
                        int newIdBorrow = borrowDataDAO.insertBookBorrow(currentUsername, bookIdInt);
                        out.println("borrow:" + newIdBorrow);
                    } else {
                        out.println("borrow:failed");
                    }
                } else if (inputLine.equals("getBooks")) {
                    List<bookModel> books = bookDataDAO.getBookData();
                    Gson gson = new Gson();
                    String bookData = gson.toJson(books);
                    out.println(bookData);
                } else if (inputLine.equals("getBlogs")) {
                    List<blogModel> blogs = blogDataDAO.getBlogData()   ;
                    Gson gson = new Gson();
                    String blogData = gson.toJson(blogs);
                    out.println(blogData);
                } else if (inputLine.startsWith("updateInfo:")) {
                    String[] info = inputLine.substring(12).split(",");
                    if (validateController.isValidGmail(info[0]) && validateController.checkPassword(info[1])) {
                        userDataDAO.updateUser(currentUsername, info[0], info[1], info[2]);
                        out.println("updateInfo:success");
                    } else {
                        out.println("updateInfo:failed");
                    }
                } else if (inputLine.equals("getBorrowList:")) {
                    List<borrowModel> borrowModels = borrowDataDAO.listBorrow(currentUsername);
                    Gson gson = new Gson();
                    String borrowData = gson.toJson(borrowModels);
                    out.println(borrowData);
                } else if (inputLine.startsWith("backBook:")) {
                    String[] backBookInfo = inputLine.substring(9).split(",");
                    int bookId = Integer.parseInt(backBookInfo[0]);
                    String bookName = backBookInfo[1];
                    if (borrowDataDAO.checkRequestBack(bookId)) {
                        borrowDataDAO.backBook(currentUsername, bookId, bookName);
                        out.println("backBook:success");
                    } else {
                        out.println("backBook:failed");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SeverProcess server = new SeverProcess(8080);
        server.start();
    }
}