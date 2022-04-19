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
    protected void onStartButtonClick() {
        ipAddress = deviceIP.getText();
        nameFile = fileName.getText();
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "adb logcat > "+nameFile+".txt");
        try {
            builder.redirectErrorStream(true);
            p = builder.start();
            r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            loggingStatus.setText("Logging IdP: "+ ipAddress);
            spinner.setOpacity(100);
            startButton.setDisable(true);
            stopButton.setDisable(false);
            Thread t = new Thread(new Runnable(){
                @Override
                public void run(){
                    threadRunning = true;
                    while(threadRunning){
                        while (true) {
                            try {
                                line = r.readLine();
                            }
                            catch (Exception e){
                                System.out.println(e);
                            }
                            if (line == null) {
                                break;
                            }
                        }
                    }
                }
            });
            t.start();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onStopButtonClick(){
        threadRunning = false;
        p.destroy();
        loggingStatus.setText("File has been saved!");
        startButton.setDisable(false);
        stopButton.setDisable(true);
        spinner.setOpacity(0);
    }

    public static void createDialog(String content) {
        Alert a = new Alert(Alert.AlertType.NONE);

        EventHandler<ActionEvent> event1 = new
                EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        a.setAlertType(Alert.AlertType.ERROR);
                        a.setContentText(content);
                        a.show();
                    }
                };

    }
}