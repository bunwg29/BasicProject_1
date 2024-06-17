package org.example.source.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.source.DAO.bookDataDAO;
import org.example.source.DAO.borrowDataDAO;
import org.example.source.DTO.BackBookDTO;
import org.example.source.DTO.BorrowListDTO;
import org.example.source.view.dashboard;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

public class dashboardAminController implements Initializable {

    // Variables use for connection between client and sever
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    // Variables in dashboard.fxml file to connect between view and controller

    // FXML Variable for borrow list TABLE
    @FXML
    private TableView<BorrowListDTO> borrowlist_table;
    @FXML
    private TableColumn<BorrowListDTO, Integer> idborrow_col;
    @FXML
    private TableColumn<BorrowListDTO, Integer> iduser_col;
    @FXML
    private TableColumn<BorrowListDTO, Integer> bookid_col;
    @FXML
    private TableColumn<BorrowListDTO, String> username_col;
    @FXML
    private TableColumn<BorrowListDTO, Integer> bookname_col;
    @FXML
    private TableColumn<BorrowListDTO, Timestamp> dayExpiration;
    @FXML
    private TableColumn<BorrowListDTO, Timestamp> dayBorrow;

    // FXML Variable for back list TABLE
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
    private TableColumn<BackBookDTO, Timestamp> date_ReturnEx;

    // FXML Variable for overdue list TABLE
    @FXML
    private AnchorPane overdue_Layout_1;
    @FXML
    private TableView<BorrowListDTO> overdue_List;
    @FXML
    private TableColumn<BorrowListDTO, Integer> id_Borrow_Overdue;
    @FXML
    private TableColumn<BorrowListDTO, String> username_Overdue;
    @FXML
    private TableColumn<BorrowListDTO, Integer> bookid_Overdue;
    @FXML
    private TableColumn<BorrowListDTO, Timestamp> borrow_Date_Overdue;
    @FXML
    private TableColumn<BorrowListDTO, Timestamp> expiration_Date_Overdue;
    @FXML
    private TableColumn<BorrowListDTO, Integer> tax_Overdue_1;

    // FXML Variable for overdue list TABLE when user request back book but not return date exactly
    @FXML
    private AnchorPane overdue_Layout_11;
    @FXML
    private TableView<BackBookDTO> overdue_List1;
    @FXML
    private TableColumn<BackBookDTO, Integer> id_Borrow_Overdue1;
    @FXML
    private TableColumn<BackBookDTO, String> username_Overdue1;
    @FXML
    private TableColumn<BackBookDTO, Integer> bookid_Overdue1;
    @FXML
    private TableColumn<BackBookDTO, String> bookname_Overdue_1;
    @FXML
    private TableColumn<BackBookDTO, Timestamp> expiration_Date_Overdue1;
    @FXML
    private TableColumn<BackBookDTO, Integer> tax_Overdue_11;

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
    @FXML
    private Button button_export_borrow;
    @FXML
    private Button button_export_return;
    @FXML
    private Button button_signout;
    @FXML
    private Button overdue_Button_1;
    @FXML
    private Button overdue_Button_2;

    // Variables use for handle data in database
    private borrowDataDAO borrowDataDAO;
    private bookDataDAO bookDataDAO;
    // Variables use for support display data from database on table in .fxml file
    private final ObservableList<BorrowListDTO> borrowListData = FXCollections.observableArrayList();
    private final ObservableList<BorrowListDTO> overdue_Book_Firstlist = FXCollections.observableArrayList();// ObservableList for backBook data
    private final ObservableList<BackBookDTO> backBookData = FXCollections.observableArrayList();
    private final ObservableList<BackBookDTO> overdue_BackBook = FXCollections.observableArrayList();
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
        ExportExelController exportExelController = new ExportExelController();
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
                sendMessageToServer("Access borrow list of user");
                borrowlist_layout.setVisible(true);
                backlist_layout.setVisible(false);
                services_layout.setVisible(false);
                overdue_Layout_1.setVisible(false);
                overdue_Layout_11.setVisible(false);
                borrowListData.clear();
                borrowListData.addAll(borrowDataDAO.getBorrowListData());
                initTableView();
            }
        });

        // Set action for button_borrow where when you click on it the list of return book request will display
        button_back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                sendMessageToServer("Access return book list of user");
                borrowlist_layout.setVisible(false);
                backlist_layout.setVisible(true);
                services_layout.setVisible(false);
                overdue_Layout_1.setVisible(false);
                overdue_Layout_11.setVisible(false);
                backBookData.clear();
                backBookData.addAll(borrowDataDAO.getBackBookData());
                initTableBackView();
            }
        });

        // Set action for button_borrow where when you click on it then you can insert information of book, user,...
        button_service.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sendMessageToServer("Access to change info of book");
                borrowlist_layout.setVisible(false);
                backlist_layout.setVisible(false);
                services_layout.setVisible(true);
                overdue_Layout_1.setVisible(false);
                overdue_Layout_11.setVisible(false);
            }
        });

        //Set action for update button book use for update info relative to book
        update_button_book.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sendMessageToServer("Update information of book");
                int bookId = Integer.parseInt(update_book_Id.getText());
                int bookQuantity = Integer.parseInt(update_book_Quantity.getText());
                if (update_book_Id.getText() != null && update_book_Image.getText() != null && update_book_Name.getText() != null && update_book_Quantity.getText() != null) {
                    bookDataDAO.updateBook(bookId, update_book_Image.getText(), update_book_Name.getText(), bookQuantity);
                }
            }
        });

        //Set action for update button book use for insert info relative to book
        update_insert_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sendMessageToServer("Insert book to database");
                if (bookDataDAO.findBookId(Integer.parseInt(update_book_Id.getText())) && bookDataDAO.finLinkImage(update_book_Image.getText())) {
                    bookDataDAO.insertBook(Integer.parseInt(update_book_Id.getText()), update_book_Image.getText(), update_book_Name.getText(), Integer.parseInt(update_book_Quantity.getText()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("Input of book ID exist in database");
                    alert.showAndWait();
                }
            }
        });

        button_export_borrow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Create file chooser
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Exel");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Excel Files", "*.xls"),
                        new FileChooser.ExtensionFilter("All Files", "*.*")
                );

                // Open File Chooser
                File selectedFile = fileChooser.showSaveDialog(button_export_borrow.getScene().getWindow());

                // Check did choose file
                if (selectedFile != null) {
                    exportExelController.exportborrowExel(selectedFile);
                }
            }
        });

        button_export_return.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Create file chooser
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Exel");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Excel Files", "*.xls"),
                        new FileChooser.ExtensionFilter("All Files", "*.*")
                );

                // Open File Chooser
                File selectedFile = fileChooser.showSaveDialog(button_export_borrow.getScene().getWindow());

                // Check did choose file
                if (selectedFile != null) {
                    exportExelController.exportbackExel(selectedFile);
                }
            }
        });

        button_signout.setOnAction(actionEvent -> {
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
        });

        overdue_Button_1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borrowlist_layout.setVisible(false);
                backlist_layout.setVisible(false);
                services_layout.setVisible(false);
                overdue_Layout_1.setVisible(true);
                overdue_Layout_11.setVisible(false);
                overdue_Book_Firstlist.clear();
                overdue_Book_Firstlist.addAll(borrowDataDAO.getOverdueList1());
                initOverdueTableView1();
            }
        });
        overdue_Button_2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borrowlist_layout.setVisible(false);
                backlist_layout.setVisible(false);
                services_layout.setVisible(false);
                overdue_Layout_1.setVisible(false);
                overdue_Layout_11.setVisible(true);
                overdue_BackBook.clear();
                overdue_BackBook.addAll(borrowDataDAO.getOverdueList2());
                initTableBackOverdueView();
            }
        });
    }

    // Method use for add data in to table list of borrow book
    private void initTableView() {
        idborrow_col.setCellValueFactory(new PropertyValueFactory<>("idBorrow"));
        iduser_col.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        bookid_col.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        username_col.setCellValueFactory(new PropertyValueFactory<>("userName"));
        bookname_col.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        dayExpiration.setCellValueFactory(new PropertyValueFactory<>("dayEx"));
        dayBorrow.setCellValueFactory(new PropertyValueFactory<>("dateBorrow"));
        borrowlist_table.setItems(borrowListData);
    }

    // Method use for add data in to table list of return book
    private void initTableBackView() {
        idback_col.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        usernameback_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        bookidback_col.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        booknameback_col.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        date_ReturnEx.setCellValueFactory(new PropertyValueFactory<>("dateBack"));
        backlist_table.setItems(backBookData);
    }

    private void initTableBackOverdueView() {
        id_Borrow_Overdue1.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        username_Overdue1.setCellValueFactory(new PropertyValueFactory<>("name"));
        bookid_Overdue1.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookname_Overdue_1.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        expiration_Date_Overdue1.setCellValueFactory(new PropertyValueFactory<>("dateBack"));
        tax_Overdue_11.setCellValueFactory(new PropertyValueFactory<>("taxes_Late"));
        overdue_List1.setItems(overdue_BackBook);
    }
    private void initOverdueTableView1() {
        id_Borrow_Overdue.setCellValueFactory(new PropertyValueFactory<>("idBorrow"));
        username_Overdue.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        bookid_Overdue.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        borrow_Date_Overdue.setCellValueFactory(new PropertyValueFactory<>("dateBorrow"));
        expiration_Date_Overdue.setCellValueFactory(new PropertyValueFactory<>("dayEx"));
        tax_Overdue_1.setCellValueFactory(new PropertyValueFactory<>("overduetax"));
        overdue_List.setItems(overdue_Book_Firstlist);
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
            System.out.println("Expiration Date: " + borrowItem.getDateBorrow());
            borrowDataDAO.insertToBorrowListTotal(borrowItem.getIdBorrow(), borrowItem.getIdUser(), borrowItem.getBookId(), borrowItem.getDateBorrow(), borrowItem.getUserName(), borrowItem.getBookName());
        } else {
            System.err.println("No row selected in the table.");
        }
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