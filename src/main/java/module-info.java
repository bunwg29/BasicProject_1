module org.example.final_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.final_project.View to javafx.fxml;
    exports org.example.final_project.View;
    exports org.example.final_project.Controller;
    opens org.example.final_project.Controller to javafx.fxml;
//    exports org.example.final_project.View;
//    opens org.example.final_project.View to javafx.fxml;
}