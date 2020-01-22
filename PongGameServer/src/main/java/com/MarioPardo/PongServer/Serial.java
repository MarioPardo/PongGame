package com.MarioPardo.PongServer;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

public class Serial  //class that handles the serial input from the arduion
{

    static public  SerialPort ports[]; //stores all open ports
    private SerialPort Activeport; //stores the current port
    static public int currentValue = 100;  //stores the value from the potentiometer


    public Serial()
    {
        //automatically selects the available serial port
        ports = SerialPort.getCommPorts(); //gets the available ports
        for(SerialPort port : ports) //iterates through every port
        {
            System.out.println(port.getSystemPortName());
            boolean usable = true; //assumes that the port is good

            try { //This tries opening the port, if it throws an exception it means its bad and cannot be used
                port.openPort();
                port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            }catch(Exception e) //if it throws an exception
            {
                usable = false; //the port is bad

            }
            if(usable) //if the port works, set it as the active port
                Activeport = port;

        }


        Activeport.openPort(); //opens the good port
        Activeport.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0); //sets settings on it

        listenSerial(); //gets the first serial value

    }

    public void listenSerial()
    {
        Scanner scanner = new Scanner(Activeport.getInputStream()); //makes a scanner that reads from serial
        String s = scanner.nextLine(); //read from scanner
        try { //this is because values are somewhat inconsistent, to simply getting an int from the read value would throw null pointer exception
            int val = Integer.parseInt(s);
            if (val >= 100) //if the value is greater than 100, it's valid, else, it's not useful
                currentValue = val;

        } catch (Exception e) { } //catches the exception but does nothing so that the program wont crash

    }






}


