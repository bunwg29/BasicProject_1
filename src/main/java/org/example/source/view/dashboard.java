package org.example.source.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.source.Sever_Client.ClientProcess;

import java.io.IOException;

public class dashboard extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ClientProcess clientProcess = new ClientProcess();
        if(clientProcess.connect()) {
            FXMLLoader fxmlLoader = new FXMLLoader(dashboard.class.getResource("dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 992, 670);
            stage.setScene(scene);
            stage.show();
        }else {
            System.out.println("Error connecting to server");
        }
    }

}