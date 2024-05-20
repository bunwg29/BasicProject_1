package org.example.source.model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class imageLoaderModel {
    public static void loadImage(String imageUrl, ImageView imageView) {
        new Thread(() -> {
            Image image = new Image(imageUrl);
            Platform.runLater(() -> imageView.setImage(image));
        }).start();
    }
}
