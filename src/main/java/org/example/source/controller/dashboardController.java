package org.example.source.controller;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.source.DAO.*;
import org.example.source.model.bookModel;
import org.example.source.model.*;

import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class dashboardController implements Initializable {
    // Variables for fxml file
    @FXML
    private Button button_Home;
    @FXML
    private AnchorPane layout_Home;
    @FXML
    private GridPane bookContainer;
    @FXML
    private HBox hbox_Blog;

    // Variable for handle database process
    private static bookDataDAO bookDataDAO;
    private List<blogModel> blogs;
    private List<bookModel> books;
    private List<HBox> blogCards;
    // Variable for handle socket process
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    // Variable for handle thread
    private ExecutorService executor;
    private Socket clientSocket;
    private blogController blogController;
    // Check connection between client and sever to display application
    public boolean connect() {
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            return false;
        }
    }

    // Send message to sever function and print on client console
    private void sendMessageToServer(String message) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
            outputStreamWriter.write(message + "\n");
            outputStreamWriter.flush();
            System.out.println("Client on actioning: " + message);
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
    private void closeConnection() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
                System.out.println("Connection closed.");
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    // Controller for action on dashboard
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            closeConnection();
        }
        executor = Executors.newFixedThreadPool(10);
        blogs = new ArrayList<>(blog());
        books = new ArrayList<>(book());
        blogCards = new ArrayList<>();
        // Set action for button of home page in dashboard
        button_Home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    int col = 0;
                    int row = 1;

                    // Get and display blog card on dashboard
                    for (int i = 0; i < blogs.size(); i++) {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/org/example/source/view/blog.fxml"));
                        HBox blogCard = fxmlLoader.load();
                        blogCards.add(blogCard);
                        blogController = fxmlLoader.getController();
                        blogController.setData(blog().get(i));
                        // Handle event for visit button in blog card
                        Button visitButton = blogController.getVisitButton();
                        int finalI = i;
                        visitButton.setOnAction(event -> {
                            try {
                                Desktop.getDesktop().browse(new URI(blogs.get(finalI).getBlogLink()));
                                sendMessageToServer("client is accessing: " + blogs.get(finalI).getBlogLink());
                            } catch (Exception e) {
                                System.err.println("Error open browser" + e.getMessage());
                            }
                        });
                        hbox_Blog.getChildren().add(blogCard);
                    }

                    // Create book card in dashboard
                    for (bookModel book : books) {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/org/example/source/view/book.fxml"));
                        VBox cardBook = fxmlLoader.load();
                        bookController bookController = fxmlLoader.getController();
                        bookController.setData(book);
                        if(col == 6) {
                            col = 0;
                            ++row;
                        }
                        bookContainer.add(cardBook, col++, row);
                        GridPane.setMargin(cardBook, new Insets(10));
                    }
                }catch(IOException e){
                    e.printStackTrace();
                } finally {
                    executor.shutdown();
                }
                StringBuilder bookData = new StringBuilder();
                StringBuilder blogData = new StringBuilder();
                for (bookModel book : books) {
                    bookData.append(book.toString()).append("\n");
                }

                for (blogModel blog : blogs) {
                    blogData.append(blog.toString()).append("\n");
                }
                sendMessageToServer("Press home button");
                sendMessageToServer(bookData.toString());
                sendMessageToServer(blogData.toString());
                // Visible for home in dashboard
                layout_Home.setVisible(true);
            }
        });

    }

    // Functions of get data from database are served in home dashboard
    public ArrayList<bookModel> book() {
        bookDataDAO = new bookDataDAO();
        dataBookFetcher dataFetcher = new dataBookFetcher(bookDataDAO);
        try {
            Future<ArrayList<bookModel>> bookFuture = executor.submit(dataFetcher);
            return bookFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public ArrayList<blogModel> blog() {
        blogDataDAO blogDataDAO = new blogDataDAO();
        dataBlogFetcher dataFetcher = new dataBlogFetcher(blogDataDAO);
        try {
            Future<ArrayList<blogModel>> blogFuture = executor.submit(dataFetcher);
            return blogFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}