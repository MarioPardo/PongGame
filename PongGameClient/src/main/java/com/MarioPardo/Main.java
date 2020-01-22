/////////////////////////////////////////////////////////////
///
/// By: Mario Pardo                Jan 21 2020
///
///   TEJ4U                         Mr.Baxter
///
///
/// A Pong Game that can be played by 2 players on the
///             same network
///  This is the client application , which simply
///     sends the player 2 y coordinate, and
///        receives a game object to display
///
///  //Disclaimer! Majority of the code is same as the server code, so that's why its not documented here
///
////////////////////////////////////////////////////////////



package com.MarioPardo;

import com.MarioPardo.PongServer.Ball;
import com.MarioPardo.PongServer.GameClass;
import com.MarioPardo.PongServer.Player;
import com.MarioPardo.PongServer.Serial;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;


public class Main extends Application
{



    //objects
    static Stage stage;
    static Scene menuScene;
    static Parent menuRoot;
    static Canvas gameCanvas;
    static StackPane gamePane;
    static Scene gameScene;
    static GraphicsContext gc;
    static Timeline t1;
    static Serial serial;
    static ClientMenuController clientMenuController;
    static NetworkConnection client;


    static Player player1;
    static Player player2;
    static Ball ball;
    static GameClass Game;




    @Override
    public void start(Stage primaryStage) throws Exception
    {

        FXMLLoader loader  = new FXMLLoader(Main.class.getResource("Menu.fxml"));
        menuRoot = (Parent) loader.load();
       clientMenuController = loader.getController();
        stage = primaryStage;
        menuScene = new Scene(menuRoot, 820, 850);
        gameCanvas = new Canvas(800,700);
        gamePane = new StackPane(gameCanvas); gameScene = new Scene(gamePane);
        gc = gameCanvas.getGraphicsContext2D();
        t1 = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        t1.setCycleCount(Timeline.INDEFINITE);


        serial = new Serial();


        stage.setTitle("MENU");
        stage.setScene(menuScene);
        stage.show();


        client = new Client(data -> //What to do when data is received
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run()
                {
                        Game = (GameClass) data; //the server sends the game object, so we take that object and set it as our game object

                }
            });
        } );



    }

    static public void run(GraphicsContext gc) {

        if(Game != null) //if there is a game (the server application has started playing)
        {
            serial.listenSerial();
            Game.p2.y = serial.currentValue;

            Game.draw(gc);

            try {
                client.send(Game.p2.y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else //if there is no game (server application hasn't started playing
        {
            System.out.println("No Game Yet, Please Start the Game on the Server Application");
        }


    }

    static public void startGame()
    {

        stage.setTitle("PONG");
        stage.setScene(gameScene);
        stage.show();
        run(gc);
        t1.play();
    }


    public static void main( String[] args )
    {
        launch(args);
    }
}

