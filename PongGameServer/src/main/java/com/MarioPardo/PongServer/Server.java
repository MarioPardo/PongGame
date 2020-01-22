package com.MarioPardo.PongServer;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends  NetworkConnection //pretty much copy of the network connection class. This is used instead to make code cleaner
{

    public Server(Consumer<Serializable> onReceiveCallBack)
    {
        super(onReceiveCallBack);
    }


}
