package org.example.source.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.source.DAO.userDataDAO;
import org.example.source.view.dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class signupController implements Initializable {
    // Variables for .fxml file
    @FXML
    private ImageView Image_Password_closeEye;

    @FXML
    private ImageView Image_Password_openEye;

    @FXML
    private TextField Pass_text_password;

    @FXML
    private TextField email_txt;

    @FXML
    private Button loginOption_button;

    @FXML
    private TextField nameofuser_txt;

    @FXML
    private PasswordField password_txt;

    @FXML
    private TextField username_txt;

    @FXML
    private Button signup_button;
    // Variables for handle data
    private userDataDAO userData;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization variable of data
        userData = new userDataDAO();

        // Set action for signup button
        signup_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(validateController.isAlphanumeric(username_txt.getText()) && validateController.isValidGmail(email_txt.getText()) && validateController.checkPassword(password_txt.getText())){
                    userData.insertUser(generateRandomNumber(), username_txt.getText(), email_txt.getText(), password_txt.getText(), nameofuser_txt.getText());
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
}
