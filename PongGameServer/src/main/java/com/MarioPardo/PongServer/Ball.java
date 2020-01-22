package com.MarioPardo.PongServer;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;



public class Ball implements Serializable //class for the ball
{

    //member variables
    public int x, y;
     int xspeed = 2;
     int yspeed = 2;


     //constructor
    public Ball()
    {
        this. x = 200; this.y = 350;

    }


    //moves the ball by it's set speed
    public void move()
    {
        x += xspeed;
        y += yspeed;

    }




    //draws the ball on the screen
    public void Draw(GraphicsContext gc)
    {

        gc.setFill(Color.RED);
        gc.fillOval(x, y, 30, 30);

    }
}

