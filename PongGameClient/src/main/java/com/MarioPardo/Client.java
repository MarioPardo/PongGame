package com.MarioPardo;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection //copy of the network connection class
{

    public Client(Consumer<Serializable> onReceiveCallBack)
    {
        super(onReceiveCallBack);
    }


}

