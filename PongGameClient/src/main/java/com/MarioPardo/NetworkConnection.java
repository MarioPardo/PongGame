package com.MarioPardo;


import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection
{
    private ConnectionThread connThread = new ConnectionThread();

    private Consumer<Serializable> onReceiveCallback;

    public NetworkConnection(Consumer<Serializable> onReceiveCallback)
    {
        this.onReceiveCallback = onReceiveCallback;
        connThread.setDaemon(true);
    }

    public void startConnection() throws Exception
    {
        connThread.start();
    }

    public void send(Serializable data) throws Exception
    {
        connThread.out.writeObject(data);
    }

    public void closeConnection() throws Exception
    {
        connThread.socket.close();
    }


    private class ConnectionThread extends Thread{

        private Socket socket;
        private ObjectOutputStream out;

        @Override
        public void run()
        {
            try {
                Socket socket = new Socket(Main.clientMenuController.getIP(), 6969); //this is a client socket, so it needs the ip address of the server socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true); //send messages quicker

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main.clientMenuController.updateStatus("Connected!");
                    }
                });

                while(true)
                {
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
                onReceiveCallback.accept("Connection Closed !");

            }
        }
    }

}
