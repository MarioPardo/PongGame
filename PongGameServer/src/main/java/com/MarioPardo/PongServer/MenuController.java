package com.MarioPardo.PongServer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable //class that controls the GUI
{

    //stores the elements of the GUI
    @FXML
    public Button playButton;

    @FXML
    public Label ipAddress;

    @FXML
    public Label status;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    } //when it first runs (boilerplate)

    public void init() //when it is initialized, display the IP Address
    {
    ipAddress.setText(Main.getIP());

    }


    public void updateStatus(String msg)
    {
        status.setText(msg);
    } //update the connection status
    public void enablePlay() { playButton.setDisable(false);}



    @FXML
    public void playButtonPressed() //what happens  when the play button is pressed
    {
        Main.startGame();

    }




}
