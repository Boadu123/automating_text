module com.example.automating_text {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.automating_text to javafx.fxml;
    exports com.example.automating_text;
    exports com.example.automating_text.controllers;
    opens com.example.automating_text.controllers to javafx.fxml;
}