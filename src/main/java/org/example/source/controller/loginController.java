package org.example.source.controller;

import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.source.DAO.userDataDAO;
import org.example.source.view.dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class loginController implements Initializable {

    // Variables for .fxml file
    @FXML
    private Button button_login;

    @FXML
    private ImageView close_eye;

    @FXML
    private ImageView open_eye;

    @FXML
    private TextField pass;

    @FXML
    private PasswordField password_txt;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField username_txt;
    // Variables for handle data
    private userDataDAO dao;
    public static String usernameLogin;
    private String password ;

    // Handle event
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization variables data
        dao = new userDataDAO();

        // Set action for button_login where you click on it will check you username and password and login
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = username_txt.getText() ;
                String password = pass.isVisible() ? pass.getText() : password_txt.getText();
               if(dao.check(username, password).equals("user")) {
                   usernameLogin = username;
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
    @FXML
    void HidePasswordOnAction(KeyEvent event) {
        password = password_txt.getText() ;
        pass.setText(password);
    }

    @FXML
    void ShowPasswordOnAction(KeyEvent event) {
        password = pass.getText() ;
        password_txt.setText(password);
    }

    @FXML
    void close_eye_OnAction(MouseEvent event) {
        password_txt.setVisible(false);
        close_eye.setVisible(false);
        pass.setVisible(true);
        open_eye.setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> open_eye_OnAction(null));
            }
        }, 1000);
    }

    @FXML
    void open_eye_OnAction(MouseEvent event) {
        password_txt.setVisible(true);
        close_eye.setVisible(true);
        pass.setVisible(false);
        open_eye.setVisible(false);
    }
}