package org.example.source.controller;

import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.source.model.blogModel;

public class blogController {

    @FXML
    private Label blogName;
    @FXML
    private ImageView blogImage;

    @FXML
    private HBox boxProgrammingBook;

    public void setData(blogModel blogModel) {
        blogName.setText(blogModel.getBlogName());
        blogImage.setImage(new Image(blogModel.getBlogImageLink()));
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
}
