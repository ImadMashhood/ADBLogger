package com.example.adblogger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ADBController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}