package com.MarioPardo.PongServer;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Player implements Serializable  //class for the player
{
    //member variables
    public int x;
    public int y = 450;
    public int score = 0;



    public Player(int x) //constructor for the player
    {
        this.x = x;

    }

    public void Draw(GraphicsContext gc) //draws the player
    {

        if(y > Main.canvasHeight - 60)
            y = Main.canvasHeight - 60;

        if(y < (Main.sceneHeight - Main.canvasHeight))
            y = (Main.sceneHeight - Main.canvasHeight);

        gc.setFill(Color.GREENYELLOW);
        gc.fillRect(x, y, 20, 60);
    }


}
