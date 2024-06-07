module org.example.source {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.json;
    requires com.google.gson;


    opens org.example.source.view to javafx.fxml;
    exports org.example.source.view;
    opens org.example.source.model;
    exports org.example.source.model to javafx.fxml;
    exports org.example.source.controller;
    opens org.example.source.controller to javafx.fxml;
    opens org.example.source.database;
    opens org.example.source.DAO;
    opens org.example.source.DTO;
    exports org.example.source.database to javafx.fxml;
    exports org.example.source.DTO to javafx.fxml;
}