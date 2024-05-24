package org.example.source.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.source.DAO.userDataDAO;
import org.example.source.view.dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    // Variables for .fxml file
    @FXML
    private PasswordField password_txt;
    @FXML
    private TextField username_txt;
    @FXML
    private Button button_login;
    @FXML
    private Button signUpButton;

    // Variables for handle data
    private userDataDAO dao;
    public static String usernameLogin;

    // Handle event
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization variables data
        dao = new userDataDAO();

        // Set action for button_login where you click on it will check you username and password and login
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               if(dao.check(username_txt.getText(), password_txt.getText()).equals("user")) {
                   usernameLogin = username_txt.getText();
                   Stage stage = new Stage();
                   FXMLLoader fxmlLoader = new FXMLLoader(dashboard.class.getResource("dashboard.fxml"));
                   Scene scene = null;
                   try {
                       scene = new Scene(fxmlLoader.load(), 1500, 670);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
                   stage.setScene(scene);
                   stage.show();

                   Stage currentStage = (Stage) button_login.getScene().getWindow();
                   currentStage.close();
               }else if(dao.check(username_txt.getText(), password_txt.getText()).equals("admin")) {
                   Stage stage = new Stage();
                   FXMLLoader fxmlLoader = new FXMLLoader(dashboard.class.getResource("dashboardAdmin.fxml"));
                   Scene scene = null;
                   try {
                       scene = new Scene(fxmlLoader.load(), 1500, 670);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
                   stage.setScene(scene);
                   stage.show();

                   Stage currentStage = (Stage) button_login.getScene().getWindow();
                   currentStage.close();
               }
               else{
                   Alert alert = new Alert(Alert.AlertType.WARNING);
                   alert.setTitle("Error");
                   alert.setHeaderText("Error with input");
                   alert.setContentText("Username or Password is incorrect");
                   alert.show();
               }
            }
        });

        // Set action for signUpButton where you click on it will check your input and handle
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(dashboard.class.getResource("User-create-account.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 340, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) signUpButton.getScene().getWindow();
                currentStage.close();
            }
        });
    }
}