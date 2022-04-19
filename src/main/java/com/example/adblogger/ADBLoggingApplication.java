package com.example.adblogger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ADBLoggingApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ADBLoggingApplication.class.getResource("adb-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 150);
        stage.setTitle("ADB Logger");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}