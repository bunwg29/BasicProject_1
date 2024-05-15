module org.example.final_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.source.view to javafx.fxml;
    exports org.example.source.view;
    exports org.example.source.controller;
    opens org.example.source.controller to javafx.fxml;
//    exports org.example.final_project.View;
//    opens org.example.final_project.View to javafx.fxml;
}