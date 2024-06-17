package org.example.source.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.source.DAO.*;
import org.example.source.model.blogModel;
import org.example.source.model.bookModel;
import org.example.source.model.borrowModel;
import org.example.source.view.dashboard;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class dashboardController implements Initializable {
    // Variable for handle socket process
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    // Variable for handle database process
    private static bookDataDAO bookDataDAO;
    // Variables for fxml file
    @FXML
    private Button button_Home;
    @FXML
    private AnchorPane layout_Home;
    @FXML
    private GridPane bookContainer;
    @FXML
    private HBox hbox_Blog;
    @FXML
    private AnchorPane layout_Borrow;
    @FXML
    private AnchorPane layout_update;
    @FXML
    private Button button_Borrow;
    @FXML
    private Button button_update;
    @FXML
    private TableView<borrowModel> borrowListTable;
    @FXML
    private TableColumn<borrowModel, Integer> idborrow;
    @FXML
    private TableColumn<borrowModel, Integer> bookId;
    @FXML
    private TableColumn<borrowModel, Timestamp> databorrow;
    @FXML
    private TextField email_update_txt;
    @FXML
    private TextField name_update_txt;
    @FXML
    private TextField password_update_txt;
    @FXML
    private Button update_info_button;
    @FXML
    private Button button_signout;
    private List<blogModel> blogs;
    private List<bookModel> books;
    private List<HBox> blogCards;
    private borrowDataDAO borrowDataDAO;
    private userDataDAO userDataDAO;
    private borrowModel borrowModel;
    // Variable for handle thread
    private ExecutorService executor;
    private Socket clientSocket;
    private blogController blogController;
    private bookController currentBookController;

    // Check connection between client and sever to display application
    public boolean connect() {
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            return false;
        }
    }

    // Function close connection if necessary
    private void closeConnection() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
                System.out.println("Connection closed.");
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Send message to server function and print on client console
    private void sendMessageToServer(String message) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
            outputStreamWriter.write(message + "\n");
            outputStreamWriter.flush();
            System.out.println("Client on actioning: " + message);
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    // Handler for action button in each row of table view
    private void handleAction(ActionEvent event) {
        // Get borrowModel corresponding to the clicked "Action" button
        Button button = (Button) event.getSource();
        TableView.TableViewSelectionModel<borrowModel> selectionModel = borrowListTable.getSelectionModel();
        borrowModel selectedBorrow = selectionModel.getSelectedItem();

        // Check if a row is selected
        if (selectedBorrow != null) {

            // Handle action based on borrowModel
            // Example: display borrowModel information in a dialog, delete borrowModel from database, etc.
            System.out.println("Borrow ID: " + selectedBorrow.getIdborrow());
            System.out.println("Book ID: " + selectedBorrow.getBookId());
            System.out.println("Borrow Date: " + selectedBorrow.getDataborrow());
            int bookId = Integer.parseInt(selectedBorrow.getBookId());


                if (borrowDataDAO.checkRequestBack(bookId)) {
                    if(borrowDataDAO.checkExpiration(loginController.usernameLogin, bookId) != 0) {

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("INFORM");
                        alert.setHeaderText("You had expiration");
                        alert.setContentText("You have to pay for us: " + borrowDataDAO.checkExpiration(loginController.usernameLogin, bookId) + "VNĐ");

                        alert.show();

                    }
                    borrowDataDAO.backBook(userDataDAO.getUserName(loginController.usernameLogin), bookId, borrowDataDAO.findNameBook(bookId));
                    sendMessageToServer("Request to back book to library with ID: " + selectedBorrow.getIdborrow());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("SUCCESSFULLY");
                    alert.setHeaderText("BACK BOOK");
                    alert.setContentText("You request back book successfully");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("INFORM");
                    alert.setHeaderText("CONFLICT");
                    alert.setContentText("You have already sent a request to back book to our");
                    alert.show();

                }


        } else {
            // Show an error message or handle the case where no row is selected
            System.err.println("No row selected in the table.");
            // You can show a popup or alert to the user here
        }
    }

    // Controller for action on dashboard
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            closeConnection();
        }
        executor = Executors.newFixedThreadPool(10);
        blogs = new ArrayList<>(blog());
        books = new ArrayList<>(book());
        blogCards = new ArrayList<>();
        borrowModel = new borrowModel();
        borrowDataDAO = new borrowDataDAO();
        userDataDAO = new userDataDAO();

        // Set action for button of home page in dashboard
        button_Home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(loginController.usernameLogin);
                sendMessageToServer("Access home page");
                // Refresh data when Home button is clicked
                refreshData();

                // Visible for home in dashboard
                layout_Home.setVisible(true);
                layout_Borrow.setVisible(false);
                layout_update.setVisible(false);
            }
        });

        // Variable to store the list of borrow books
        ObservableList<borrowModel> borrowModels = FXCollections.observableArrayList();
        borrowDataDAO = new borrowDataDAO(); // Initialize borrowDataDAO

        // Set event for Borrow button
        button_Borrow.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                sendMessageToServer("Access to borrow book");
                layout_Home.setVisible(false);
                layout_Borrow.setVisible(true);
                layout_update.setVisible(false);
                updateBorrowList(); // Update the list of borrow books when the Borrow button is clicked
            }
        });

        // Initialize the configuration for the columns of the borrowListTable
        loadListBorrow();
        borrowListTable.setItems(borrowModels); // Display the initial list of borrow books

        // Create the "ACTION" column with a button in each row
        TableColumn<borrowModel, Void> actionColumn = new TableColumn<>("ACTION");
        actionColumn.setCellFactory(param -> new TableCell<borrowModel, Void>() {
            private final Button actionButton = new Button("BACK");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    actionButton.setOnAction(dashboardController.this::handleAction);
                    setGraphic(actionButton);
                }
            }
        });

        // Add the "ACTION" column to the TableView
        borrowListTable.getColumns().add(actionColumn);

        button_update.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                sendMessageToServer("Access to update information of book");
                layout_Home.setVisible(false);
                layout_Borrow.setVisible(false);
                layout_update.setVisible(true);
            }
        });

        update_info_button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if (validateController.isValidGmail(email_update_txt.getText()) && validateController.checkPassword(password_update_txt.getText()) && name_update_txt.getText() != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INFORM");
                    alert.setHeaderText("SUCCESSFULLY");
                    alert.setContentText("You updated information sucessfully");
                    alert.showAndWait();
                    sendMessageToServer("Update info successfully");
                    userDataDAO.updateUser(loginController.usernameLogin, email_update_txt.getText(), password_update_txt.getText(), name_update_txt.getText());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("INFORM");
                    alert.setHeaderText("NOT CORRECT INPUT");
                    alert.setContentText("Your email or password is wrong");
                    alert.showAndWait();
                }
            }
        });

        button_signout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                alert.setHeaderText("Do you want continue?");
                alert.setContentText("Chose yes to continue and no to cancel");

                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeNo = new ButtonType("No");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                // Display alert and wait user choose
                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                // Handle result
                if (result == buttonTypeYes) {
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

                    Stage currentStage = (Stage) button_signout.getScene().getWindow();
                    currentStage.close();
                }
            }
        });

    }

    private void handleBorrowButtonClick(ActionEvent event, bookController bookController) {
        String bookId = bookController.getBookId();
        int bookIdInt = Integer.parseInt(bookId);
        if (borrowDataDAO.checkBookQuantity(bookIdInt) > 0) {
            // Send request to server
            sendMessageToServer("borrow:" + bookId);
            // Add book borrow record to database
            int newIdBorrow = borrowDataDAO.insertBookBorrow(loginController.usernameLogin, bookIdInt);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESSFULLY");
            alert.setHeaderText("You have to return book on:" + LocalDateTime.now().plusDays(7));
            alert.setContentText("Borrowed " + newIdBorrow + " successfully");
            alert.show();

            // Refresh data after borrowing a book
            refreshData();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("UNSUCCESSFULLY");
            alert.setHeaderText("BORROW");
            alert.setContentText("The quantity of books you want to borrow is over");
            alert.show();
        }
    }

    private void loadListBorrow() {
        idborrow.setCellValueFactory(new PropertyValueFactory<>("idborrow"));
        bookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        databorrow.setCellValueFactory(new PropertyValueFactory<>("databorrow"));
    }

    // Functions of get data from database are served in home dashboard
    public ArrayList<bookModel> book() {
        bookDataDAO = new bookDataDAO();
        dataBookFetcher dataFetcher = new dataBookFetcher(bookDataDAO);
        try {
            Future<ArrayList<bookModel>> bookFuture = executor.submit(dataFetcher);
            return bookFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<blogModel> blog() {
        blogDataDAO blogDataDAO = new blogDataDAO();
        dataBlogFetcher dataFetcher = new dataBlogFetcher(blogDataDAO);
        try {
            Future<ArrayList<blogModel>> blogFuture = executor.submit(dataFetcher);
            return blogFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Update the borrow list from the database
    private void updateBorrowList() {
        ObservableList<borrowModel> borrowModels = FXCollections.observableArrayList();
        borrowModels.addAll(borrowDataDAO.listBorrow(loginController.usernameLogin));
        borrowListTable.setItems(borrowModels);
    }

    // Refresh and reload data for both blogs and books
    private void refreshData() {
        // Update books and blogs lists
        books = new ArrayList<>(book());
        blogs = new ArrayList<>(blog());

        // Clear existing cards
        bookContainer.getChildren().clear();
        hbox_Blog.getChildren().clear();

        try {
            // Reload blog data (You should implement this part based on your logic)
            int colBlog = 0;
            int rowBlog = 1;
            for (int i = 0; i < blogs.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/org/example/source/view/blog.fxml"));
                HBox blogCard = fxmlLoader.load();
                blogCards.add(blogCard);
                blogController = fxmlLoader.getController();
                blogController.setData(blog().get(i));
                Button visitButton = blogController.getVisitButton();
                int finalI = i;
                visitButton.setOnAction(event -> {
                    try {
                        Desktop.getDesktop().browse(new URI(blogs.get(finalI).getBlogLink()));
                        sendMessageToServer("client is accessing: " + blogs.get(finalI).getBlogLink());
                    } catch (Exception e) {
                        System.err.println("Error open browser" + e.getMessage());
                    }
                });
                hbox_Blog.getChildren().add(blogCard);
            }

            // Reload book data
            int colBook = 0;
            int rowBook = 1;
            for (bookModel book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/org/example/source/view/book.fxml"));
                VBox cardBook = fxmlLoader.load();

                currentBookController = fxmlLoader.getController();
                currentBookController.setData(book);

                bookController cardBookController = currentBookController;
                cardBookController.button_borrow.setOnAction(event -> {
                    handleBorrowButtonClick(event, cardBookController);
                });

                if (colBook == 6) {
                    colBook = 0;
                    ++rowBook;
                }
                bookContainer.add(cardBook, colBook++, rowBook);
                GridPane.setMargin(cardBook, new Insets(10));
            }

            StringBuilder bookData = new StringBuilder();
            StringBuilder blogData = new StringBuilder();
            for (bookModel book : books) {
                bookData.append(book.toString()).append("\n");
            }

            for (blogModel blog : blogs) {
                blogData.append(blog.toString()).append("\n");
            }
            sendMessageToServer("Press home button");
            sendMessageToServer(bookData.toString());
            sendMessageToServer(blogData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}