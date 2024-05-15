package org.example.final_project.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private Button Home_button;
    @FXML
    private AnchorPane demo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Home_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                demo.setVisible(true);
            }
        });
    }
}