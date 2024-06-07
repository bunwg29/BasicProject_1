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
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class loginController implements Initializable {

    private Socket clientSocket;
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
    private String password;
    private String username;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    // Handle event
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            // Xử lý lỗi
        }

        // Initialization variables data
        dao = new userDataDAO();

        // Set action for button_login where you click on it will check you username and password and login
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                username = username_txt.getText();
                String password = pass.isVisible() ? pass.getText() : password_txt.getText();
                if (dao.check(username, password).equals("user")) {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        // Gửi username đến ServerProcess
                        out.println("login:" + username);

                        // Nhận phản hồi từ ServerProcess
                        String response = in.readLine();
                        if (response.equals("login:success")) {
                            // Tạo scene mới cho dashboard
                            Stage stage = new Stage();
                            FXMLLoader fxmlLoader = new FXMLLoader(dashboard.class.getResource("dashboard.fxml"));
                            Scene scene = null;
                            try {
                                scene = new Scene(fxmlLoader.load(), 1500, 670);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // Truyền socket và username vào dashboardController
                            dashboardController dashboardController = fxmlLoader.getController();
                            dashboardController.setUsername(username);
                            dashboardController.setClientSocket(clientSocket);

                            stage.setScene(scene);
                            stage.show();

                            // Đóng cửa sổ login
                            Stage currentStage = (Stage) button_login.getScene().getWindow();
                            currentStage.close();
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (dao.check(username_txt.getText(), password_txt.getText()).equals("admin")) {
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
                } else {
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
        password = password_txt.getText();
        pass.setText(password);
    }

    @FXML
    void ShowPasswordOnAction(KeyEvent event) {
        password = pass.getText();
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