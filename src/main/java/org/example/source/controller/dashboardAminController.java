package org.example.source.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.example.source.DAO.borrowDataDAO;
import org.example.source.DAO.bookDataDAO;
import org.example.source.DTO.BorrowListDTO;
import org.example.source.DTO.BackBookDTO;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

public class dashboardAminController implements Initializable {

    // Variables in dashboard.fxml file to connect between view and controller
    @FXML
    private TableView<BorrowListDTO> borrowlist_table;

    @FXML
    private TableColumn<BorrowListDTO, Integer> idborrow_col;

    @FXML
    private TableColumn<BorrowListDTO, Integer> iduser_col;

    @FXML
    private TableColumn<BorrowListDTO, Integer> bookid_col;

    @FXML
    private TableColumn<BorrowListDTO, Timestamp> dateborrow_col;

    @FXML
    private TableColumn<BorrowListDTO, String> username_col;

    @FXML
    private TableColumn<BorrowListDTO, String> bookname_col;

    @FXML
    private AnchorPane borrowlist_layout;

    @FXML
    private Button button_back;

    @FXML
    private Button button_borrow;

    @FXML
    private Button button_service;

    @FXML
    private AnchorPane services_layout;

    @FXML
    private AnchorPane backlist_layout;

    @FXML
    private TableView<BackBookDTO> backlist_table;

    @FXML
    private TableColumn<BackBookDTO, Integer> idback_col; // Use BackBookDTO

    @FXML
    private TableColumn<BackBookDTO, String> usernameback_col; // Use BackBookDTO

    @FXML
    private TableColumn<BackBookDTO, Integer> bookidback_col; // Use BackBookDTO

    @FXML
    private TableColumn<BackBookDTO, String> booknameback_col; // Use BackBookDTO

    @FXML
    private TableColumn<BackBookDTO, Timestamp> dateex_col; // Use BackBookDTO

    @FXML
    private TextField update_book_Id;

    @FXML
    private TextField update_book_Image;

    @FXML
    private TextField update_book_Name;

    @FXML
    private TextField update_book_Quantity;

    @FXML
    private Button update_button_book;
    @FXML
    private Button update_insert_button;


    // Variables use for handle data in database
    private borrowDataDAO borrowDataDAO;
    private bookDataDAO bookDataDAO;

    // Variables use for support display data from database on table in .fxml file
    private ObservableList<BorrowListDTO> borrowListData = FXCollections.observableArrayList();
    private ObservableList<BackBookDTO> backBookData = FXCollections.observableArrayList(); // ObservableList for backBook data


    // Variables use for connection between client and sever
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private Socket clientSocket;

    // method use for check connection usually in dashboard in view package to decide display dashboard
    public boolean connect() {
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            return false;
        }
    }


    // Method close connection between client and sever
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

    // Method send message to sever
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

    // Handle event
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization variable
        borrowDataDAO = new borrowDataDAO();
        bookDataDAO = new bookDataDAO();
        try {
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server: " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            closeConnection();
        }

        /*
            Add column has title is ACTION and each row in column will have
            a button to handle data at this row like: logic in database, borrow and back
        */
        TableColumn<BorrowListDTO, String> actionCol = new TableColumn<>("ACTION"); // create actionCol
        actionCol.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        actionCol.setCellFactory(param -> new TableCell<BorrowListDTO, String>() {
            private final Button actionButton = new Button("APPROVE");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    actionButton.setOnAction(event -> handleAction(event));
                    setGraphic(actionButton);
                }
            }
        });

        borrowlist_table.getColumns().add(actionCol); // Add column in to tabletView

        /*
            Add column has title is ACTION and each row in column will have
            a button to handle data at this row like: logic in database, borrow and back
        */
        TableColumn<BackBookDTO, String> actionColBack = new TableColumn<>("ACTION");
        actionColBack.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        actionColBack.setCellFactory(param -> new TableCell<BackBookDTO, String>() {
            private final Button actionButton = new Button("Accept");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    actionButton.setOnAction(event -> handleBackBookAction(event));
                    setGraphic(actionButton);
                }
            }
        });
        backlist_table.getColumns().add(actionColBack);

        // Set action for button_borrow where when you click on it the list of borrow will display
        button_borrow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borrowlist_layout.setVisible(true);
                backlist_layout.setVisible(false);
                services_layout.setVisible(false);
                borrowListData.clear();
                borrowListData.addAll(borrowDataDAO.getBorrowListData());
                initTableView();
            }
        });

        // Set action for button_borrow where when you click on it the list of return book request will display
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borrowlist_layout.setVisible(false);
                backlist_layout.setVisible(true);
                services_layout.setVisible(false);
                backBookData.clear();
                backBookData.addAll(borrowDataDAO.getBackBookData());
                initTableBackView();
            }
        });

        // Set action for button_borrow where when you click on it then you can insert information of book, user,...
        button_service.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borrowlist_layout.setVisible(false);
                backlist_layout.setVisible(false);
                services_layout.setVisible(true);
            }
        });

        //Set action for update button book use for update info relative to book
        update_button_book.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int bookId = Integer.parseInt(update_book_Id.getText());
                int bookQuantity = Integer.parseInt(update_book_Quantity.getText());
                if(update_book_Id.getText() != null && update_book_Image.getText() != null && update_book_Name.getText() != null && update_book_Quantity.getText() != null) {
                    bookDataDAO.updateBook(bookId, update_book_Image.getText(), update_book_Name.getText(), bookQuantity);
                }
            }
        });

        //Set action for update button book use for insert info relative to book
        update_insert_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(bookDataDAO.findBookId(Integer.parseInt(update_book_Id.getText())) && bookDataDAO.finLinkImage(update_book_Image.getText())) {
                    bookDataDAO.insertBook(Integer.parseInt(update_book_Id.getText()), update_book_Image.getText(), update_book_Name.getText(), Integer.parseInt(update_book_Quantity.getText()));
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("Input of book ID exist in database");
                    alert.showAndWait();
                }
            }
        });
    }

    // Method use for add data in to table list of borrow book
    private void initTableView() {
        idborrow_col.setCellValueFactory(new PropertyValueFactory<>("idBorrow"));
        iduser_col.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        bookid_col.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        dateborrow_col.setCellValueFactory(new PropertyValueFactory<>("dateBorrow"));
        username_col.setCellValueFactory(new PropertyValueFactory<>("userName"));
        bookname_col.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        borrowlist_table.setItems(borrowListData);
    }

    // Handle between app and database with borrow book list
    private void handleAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        TableCell<BorrowListDTO, String> cell = (TableCell) button.getParent();
        BorrowListDTO borrowItem = cell.getTableView().getItems().get(cell.getIndex());

        if (borrowItem != null) {
            System.out.println("Borrow ID: " + borrowItem.getIdBorrow());
            System.out.println("User ID: " + borrowItem.getIdUser());
            System.out.println("Book ID: " + borrowItem.getBookId());
            System.out.println("Date Borrow: " + borrowItem.getDateBorrow());
            System.out.println("User Name: " + borrowItem.getUserName());
            System.out.println("Book Name: " + borrowItem.getBookName());
            borrowDataDAO.insertToBorrowListTotal(borrowItem.getIdBorrow(),  borrowItem.getIdUser(), borrowItem.getBookId(), borrowItem.getDateBorrow(), borrowItem.getUserName(), borrowItem.getBookName());
        } else {
            System.err.println("No row selected in the table.");
        }
    }

    // Method use for add data in to table list of return book
    private void initTableBackView() {
        idback_col.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        usernameback_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        bookidback_col.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        booknameback_col.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        dateex_col.setCellValueFactory(new PropertyValueFactory<>("dateBack"));

        backlist_table.setItems(backBookData);
    }
    // Handle between app and database with return book list
    private void handleBackBookAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        TableCell<BackBookDTO, String> cell = (TableCell) button.getParent();
        BackBookDTO backBookItem = cell.getTableView().getItems().get(cell.getIndex());

        if (backBookItem != null) {
            // Get data from backBookItem
            borrowDataDAO.inserttoBackListTotal(backBookItem.getBorrowId(), backBookItem.getName(), backBookItem.getBookId(), backBookItem.getBookName());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("INFORM");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Customer returned book successfully");
            Optional<ButtonType> result = alert.showAndWait();
            // ... Your logic to handle the action here
        } else {
            System.err.println("No row selected in backlist_table.");
        }
    }
}