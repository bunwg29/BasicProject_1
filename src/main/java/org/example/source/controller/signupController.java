package org.example.source.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.source.DAO.userDataDAO;
import org.example.source.view.dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class signupController implements Initializable {
    // Variables for .fxml file
    @FXML
    private Button back_button;


    @FXML
    private ImageView close_eye;


    @FXML
    private TextField email_txt;

    @FXML
    private Button loginOption_button;

    @FXML
    private TextField nameofuser_txt;

    @FXML
    private ImageView open_eye;

    @FXML
    private TextField pass;

    @FXML
    private PasswordField password_txt;

    @FXML
    private Button signup_button;

    @FXML
    private TextField username_txt;
    // Variables for handle data
    private userDataDAO userData;
    private String password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization variable of data
        userData = new userDataDAO();


        // Set action for signup button
        signup_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = username_txt.getText() ;
                String email = email_txt.getText() ;
                String password = pass.isVisible() ? pass.getText() : password_txt.getText() ;
                String nameOfUser = nameofuser_txt.getText() ;
                if(validateController.isAlphanumeric(username) && validateController.isValidGmail(email) && validateController.checkPassword(password)){
                    userData.insertUser(generateRandomNumber(), username, email, password, nameOfUser);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("SUCESSFULLY");
                    alert.setHeaderText("Congratulations!");
                    alert.setContentText("Please, back to login stage to login your account");
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Your input of value is not according to the request");
                    alert.setContentText("Please enter all the fields correctly");
                    alert.showAndWait();
                }
            }
        });

        loginOption_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(dashboard.class.getResource("User.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 340, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) loginOption_button.getScene().getWindow();
                currentStage.close();
            }
        });
    }
    public static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(100000 - 1 + 1) + 1;
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
