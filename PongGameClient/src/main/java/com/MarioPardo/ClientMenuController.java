package com.MarioPardo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ClientMenuController //controls the GUI
{

    //stores components of the GUI
    @FXML
    private Label statusLabel;

    @FXML
    private TextField ipTextfield;

    @FXML
    private Button ConnectButton;


    @FXML
    public void connectButtonPress() //what to do when the connect button is pressed
    {
        statusLabel.setText("CONNECTING");

        try {  //try to connect
            Main.client.startConnection();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void playButtonPressed()
    {
        Main.startGame();
    } //when the play cutton is pressed, start the game


    public void updateStatus(String str)
    {
        statusLabel.setText(str);
    } //update connection status

     public String getIP()
    {
        return ipTextfield.getText();
    } //gets the ip address that was passed in


}
