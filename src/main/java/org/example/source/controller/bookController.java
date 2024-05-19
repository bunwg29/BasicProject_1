package org.example.source.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.source.model.bookModel;


public class bookController{
    @FXML
    private Label bookId;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookName;

    @FXML
    private Label bookQuantity;

    public void setData(bookModel book){
        bookId.setText(book.getBookId());
        bookImage.setImage(new Image(book.getImageSrc()));
        bookImage.setFitWidth(200);
        bookImage.setFitHeight(200);
        bookName.setText(book.getBookName());
        bookQuantity.setText(book.getBookQuantity());
    }
}