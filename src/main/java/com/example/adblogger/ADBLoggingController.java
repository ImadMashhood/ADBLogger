package com.example.adblogger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ADBLoggingController {
    @FXML
    private JFXTextField deviceIP;
    @FXML
    private JFXTextField saveLocation;
    @FXML
    private JFXButton startButton;
    @FXML
    private JFXButton stopButton;
    @FXML
    private Text loggingStatus;

    @FXML
    protected void onStartButtonClick() {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "adb logcat");
        try {
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            p.destroy();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onStopButtonClick() {

    }
}