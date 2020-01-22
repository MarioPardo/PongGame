package com.MarioPardo.PongServer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.Serializable;

public class GameClass implements Serializable {  //serializable makes it so that java can turn into bytestream and send over network. This is the same for the player and ball

     public Player p1; //stores the game components
     public Player p2;
     public Ball ball;
     

     public Boolean darkMode; //wether game is in dark mode or not

    static int sceneWidth = 820;
    static int sceneHeight = 800;
    static int canvasWidth = 800;
    static int canvasHeight = 700;
     
     
    
    public GameClass(Player pl1, Player pl2, Ball b, boolean dMode) //constructor, sets all the member variables to what was passed in
    { 
        this.p1 = pl1;
        this.p2 = pl2;
        this.ball = b;
        darkMode = dMode;
    }

    public void getInfo() //gets the games info
    {
        Main.serial.listenSerial(); // gets the value from serial
        p1.y = Main.serial.currentValue; //sets it as p1 y value

        checkWallCollision(); //checks if the ball collides with wall
        checkPlayerCollision(); //checks if ball collides with player

        try {
            Main.server.send(this); //sends this game object to the client so that it can render it
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void GoalScored(int player) //called when a goal is scored
    {

        if (player == 1) { //if player 1 scored
            p1.score++;
            ball.x = 600; //set the ball in the opposite side
            ball.y = 350;
            ball.xspeed = -2; //make the ball go towards the left
            int yspeed;
            ball.yspeed = Main.rng(2) == 1 ? 2 : -2; //make the y speed random (add 1 or 2)

        }
        if (player == 2) { //if player 2 scored
            p2.score++;
            ball.x = 200; //sets the ball in the opposite side
            ball.y = 350;
            ball.xspeed = 2; //make the ball go towards the right
            int yspeed;
            ball.yspeed = Main.rng(2) == 1 ? 2 : -2;
        }

        Main.pause(2000); //wait 2 seconds pefore continuing


    }

    public void draw(GraphicsContext gc) //draws the game
    {

        if(darkMode) //if in darkmode
        {
            //draw background
            gc.setFill(Color.GREY);
            gc.fillRect(0,0,sceneHeight,sceneWidth);
            gc.setFill(Color.BLACK);
            gc.fillRect(0,(sceneHeight - canvasHeight), canvasWidth, canvasHeight);

            //draw outline
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(10);
            gc.strokeLine(0,sceneHeight - canvasHeight,canvasWidth,sceneHeight - canvasHeight); //top line
            gc.strokeLine(0, canvasHeight - 5,canvasWidth, canvasHeight- 5);//bottom line
            gc.setStroke(Color.RED);
            gc.strokeLine(0,sceneHeight - canvasHeight, 0,canvasHeight- 5  );
            gc.strokeLine(canvasWidth,sceneHeight - canvasHeight, canvasWidth,canvasHeight- 5  );


            //draw scores
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Times New Roman", 40));
            gc.fillText("P1:" + p1.score + "", 180, 35);
            gc.fillText("P2:" + p2.score + "", 600, 35);

        }else //if not in darkmode
        {
            //draw background
            gc.setFill(Color.GREY);
            gc.fillRect(0,0,sceneHeight,sceneWidth);
            gc.setFill(Color.WHITE);
            gc.fillRect(0,(sceneHeight - canvasHeight), canvasWidth, canvasHeight);

            //draw outline
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(10);
            gc.strokeLine(0,sceneHeight - canvasHeight,canvasWidth,sceneHeight - canvasHeight); //top line
            gc.strokeLine(0, canvasHeight - 5,canvasWidth, canvasHeight- 5);//bottom line
            gc.setStroke(Color.RED);
            gc.strokeLine(0,sceneHeight - canvasHeight, 0,canvasHeight- 5  );
            gc.strokeLine(canvasWidth,sceneHeight - canvasHeight, canvasWidth,canvasHeight- 5  );


            //draw scores
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Times New Roman", 40));
            gc.fillText("P1:" + p1.score + "", 180, 35);
            gc.fillText("P2:" + p2.score + "", 600, 35);
        }


        p1.Draw(gc);
        p2.Draw(gc);
        ball.Draw(gc);


    }

    public void checkWallCollision()
    {
        if(ball.y >= (sceneHeight - canvasHeight))
            ball.yspeed = -ball.yspeed;

        if(ball.y <= canvasHeight - 30)
            ball.yspeed = -ball.yspeed;

        if(ball.x <= 0) //if ball collides with left wall, p2 has scored
            GoalScored(2);

        if(ball.x >= canvasWidth - 30) //if ball collides with right wall p1 has scored

            GoalScored(1);

    }

    public void checkPlayerCollision()
    {

        if ((ball.x <= p1.x + 20) && (ball.y > p1.y - 15 && ball.y < p1.y + 75)) {
            CollidedwPlayer(1);

        }

        {
            if ((ball.x >= p2.x - 30)  && (ball.y > p2.y - 15 && ball.y < p2.y + 75)) {
                CollidedwPlayer(2);

            }
        }
    }

    public void CollidedwPlayer(int side)
    {
        if(Main.rng(2) == 1)
            ball.yspeed = -ball.yspeed;

        //for x speed
        int xrand = Main.rng(3);
        if(ball.xspeed < 0) //if collided with player on the left
        {
            ball.x += 5 ; //make it so it wont collide again
            ball.xspeed = 2;
            ball.xspeed += xrand; //adds random to x speed
        }else //if is positive, meaning it collided w player on the right
        {
            ball.x -= 5; //make it so it wont collide again
            ball.xspeed = -2;
            ball.xspeed -= xrand;
        }

        //for y speed
        int yrand = Main.rng(3);
        if(ball.yspeed < 0)
        {
            ball.yspeed = 2;
            ball.yspeed += yrand;
        }else
        {
            ball.yspeed = -2;
            ball.yspeed += yrand;
        }
    }





}




