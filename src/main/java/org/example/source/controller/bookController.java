package org.example.source.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.example.source.model.bookModel;

public class bookController {

    @FXML
    Button button_borrow;
    // Variable from book.fxml file
    @FXML
    private Label bookId;
    @FXML
    private ImageView bookImage;
    @FXML
    private Label bookName;
    @FXML
    private Label bookQuantity;
    // Variable of book model
    private bookModel book;

    // This is a function set data that is got from database into book fxml file
    public void setData(bookModel book) {
        this.book = book;
        bookId.setText(book.getBookId());
        org.example.source.model.imageLoaderModel.loadImage(book.getImageSrc(), bookImage);
        bookImage.setFitWidth(200);
        bookImage.setFitHeight(200);
        bookName.setText(book.getBookName());
        bookQuantity.setText(book.getBookQuantity());
    }

    // Function get book id corresponding book will be used in dashboardController
    public String getBookId() {
        return book.getBookId();
    }
}