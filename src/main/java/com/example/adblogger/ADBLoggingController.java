package com.example.adblogger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.text.Text;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ADBLoggingController {
    @FXML
    private JFXTextField deviceIP;
    @FXML
    private JFXTextField fileName;
    @FXML
    private JFXButton startButton;
    @FXML
    private JFXButton stopButton;
    @FXML
    private Text loggingStatus;
    @FXML
    private ProgressIndicator spinner;

    private BufferedReader r;
    private Process p;
    private String ipAddress;
    private String nameFile;
    String line;
    private String command;
    private volatile boolean threadRunning = false;

    @FXML
    protected void onStartButtonClick() throws IOException {
        ipAddress = deviceIP.getText();
        nameFile = fileName.getText();
        if(ipAddress == null || ipAddress.equals("")){
            createDialog("Please Enter Valid IP");
            return;
        }
        if(nameFile == null || nameFile.equals("")){
            createDialog("Please Enter Valid File Name");
            return;
        }
        startServer();
        runCommands("adb logcat -c", "Cleaning up previous logs");
        runCommands("adb connect "+ipAddress, "Connecting to IP Address");
        runCommands("adb logcat > "+nameFile+".txt", "Logging IP: "+ipAddress);
    }

    @FXML
    public void runCommands(String command, String status){
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", command);
        try {
            builder.redirectErrorStream(true);
            p = builder.start();
            r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            spinner.setOpacity(100);
            loggingStatus.setText(status);
            startButton.setDisable(true);
            stopButton.setDisable(false);
            threadRunning = true;
            Thread t = new Thread(() -> {
                while(threadRunning){
                    try {
                        line = r.readLine();
                    }
                    catch (Exception e){
                        System.err.println(e);
                    }
                    if (line == null) {
                        break;
                    }
                }
            });
            t.start();
        }
        catch (Exception e){
            System.err.println(e);
            onStopButtonClick();
        }
    }

    @FXML
    protected void onStopButtonClick(){
        threadRunning = false;
        p.destroy();
        runCommands("adb kill-server", "Killing Server");
        loggingStatus.setText("File has been saved!");
        startButton.setDisable(false);
        stopButton.setDisable(true);
        spinner.setOpacity(0);
    }

    protected void startServer() throws IOException {
        loggingStatus.setText("Starting up ADB Server");
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "adb start-server");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }

    public static void createDialog(String content) {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText(content);
        a.show();
    }
}