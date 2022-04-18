module com.example.adblogger {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;


    opens com.example.adblogger to javafx.fxml;
    exports com.example.adblogger;
}