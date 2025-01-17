package org.example.source.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.source.controller.dashboardController;
import org.example.source.controller.dashboardAminController;
import java.io.IOException;


// This is class extends Application class and display dashboard
public class dashboard extends Application {
    @Override
    public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(dashboard.class.getResource("User.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 340, 500);
            stage.setScene(scene);
            stage.show();
    }
    public static void main(String[] args) {
        dashboardController controller = new dashboardController();
        dashboardAminController adminController = new dashboardAminController();
        if(controller.connect()) {
            launch();
        }else {
            System.out.println("Failed to connect to server");
        }
    }
}
