package org.example.source.controller;

import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.source.model.blogModel;


// This class use for support get blogs data from database and set corresponding data
public class blogController {
    // Variables for .fxml file
    @FXML
    private Label blogName;
    @FXML
    private ImageView blogImage;
    @FXML
    private Button visit_button;
    @FXML
    private HBox boxProgrammingBook;

    // This is a function set data that is got from database into blog fxml file
    public void setData(blogModel blogModel) {
        blogName.setText(blogModel.getBlogName());
        blogImage.setImage(new Image(blogModel.getBlogImageLink()));
        // Set shadow for blog card
        Rectangle clip = new Rectangle();
        clip.setWidth(200);
        clip.setHeight(170);
        clip.setArcHeight(50.0);
        clip.setArcWidth(50.0);
        clip.setStroke(Color.BLACK);
        blogImage.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = blogImage.snapshot(parameters, null);
        blogImage.setClip(null);
        blogImage.setEffect(new DropShadow(20, Color.WHITE));
        blogImage.setImage(image);
    }

    // Method return button to handle in dashboardController
    public Button getVisitButton() {
        return visit_button;
    }
}