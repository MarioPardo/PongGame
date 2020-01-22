package com.MarioPardo.PongServer;


import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection
{
    private ConnectionThread connThread = new ConnectionThread(); //stores the network thread

    private Consumer<Serializable> onReceiveCallback; //stores the consumer, consumer is an interface that receives something, and does something with it.

    public NetworkConnection(Consumer<Serializable> onReceiveCallback) //constructor
    {
        this.onReceiveCallback = onReceiveCallback;
        connThread.setDaemon(true); //makes it so it doesnt prevent the program from exiting even if the thread is still running
    }

    public void startConnection() throws Exception
    {
        Main.menuController.updateStatus("CONNECTING");
        connThread.start(); //makes the connection thread start the run method
    }

    public void send(Serializable data) throws Exception
    {
        connThread.out.reset(); //for clearing purposes
        connThread.out.writeObject(data); //sends the data that was passed in
    }

    public void closeConnection() throws Exception
    {
        connThread.socket.close(); //closes the socket connection
    }


    private class ConnectionThread extends Thread{

        private Socket socket;
        private ObjectOutputStream out;

        @Override
        public void run()
        {
            try {
                ServerSocket server = new ServerSocket(6969); //server socket tries to get a connection on port 6969
                Socket socket = server.accept(); //once it does get a connection, it stores the client socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());  //once it does, sets input and output streams
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true); //send messages quicker

                Platform.runLater(new Runnable() { //put in a runnable due to threads and having to use JavaFX thread to update javaFX, otherwise, error
                    @Override
                    public void run() {
                        Main.menuController.updateStatus("CONNECTED"); //updates what is shown in the GUI
                        Main.menuController.enablePlay(); //enables play once the connection has been made
                    }
                });


                while(true)
                {
                    Serializable data = (Serializable) in.readObject(); //gets the data that was sent
                    onReceiveCallback.accept(data); //stores the data in the onReceiveCallBack
                }

            } catch (Exception e) {
                e.printStackTrace();
                onReceiveCallback.accept("Connection Closed !");

            }
        }
    }



}

