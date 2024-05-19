package org.example.source.controller;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.source.DAO.blogDataDAO;
import org.example.source.DAO.bookDataDAO;
import org.example.source.model.bookModel;
import org.example.source.model.*;

import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private Button button_Home;

    @FXML
    private AnchorPane layout_Home;

    @FXML
    private GridPane bookContainer;

    @FXML
    private HBox hbox_Blog;
    private List<bookModel> books;
    private List<blogModel> blogs;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blogs = new ArrayList<>(blog());
        books = new ArrayList<>(book());
        int col = 0;
        int row = 1;
        try {
            for (int i = 0; i < blogs.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/org/example/source/view/blog.fxml"));
                HBox blogCard = fxmlLoader.load();
                blogController blogController = fxmlLoader.getController();
                blogController.setData(blog().get(i));
                hbox_Blog.getChildren().add(blogCard);
            }

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
        }
        button_Home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                layout_Home.setVisible(true);
            }
        });
    }
    public ArrayList<bookModel> book() {
        bookDataDAO bookDataDAO = new bookDataDAO();
        return bookDataDAO.getBookData();
    }

    public ArrayList<blogModel> blog() {
        blogDataDAO blogDataDAO = new blogDataDAO();
        return blogDataDAO.getBlogData();
    }
}