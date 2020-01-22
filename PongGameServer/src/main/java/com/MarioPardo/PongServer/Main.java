/////////////////////////////////////////////////////////////
///
/// By: Mario Pardo                Jan 21 2020
///
///   TEJ4U                         Mr.Baxter
///
///
/// A Pong Game that can be played by 2 players on the
///             same network
///  This is the server application , the main application
///     that handles all the logic and processing
///
///     This takes the player 2's y position, creates a game
///         and sends it to the client to be drawn
///
///
////////////////////////////////////////////////////////////





package com.MarioPardo.PongServer;





import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.InetAddress;
import java.util.Random;




public class Main extends Application
{


    //objects

    static Stage stage; //this is boiler plate javafx stuff for making the screen
    static Scene menuScene;
    static Parent menuRoot;
    static Canvas gameCanvas;
    static StackPane gamePane;
    static Scene gameScene;

    static GraphicsContext gc; //this is what you draw on
    static Timeline t1;  //what makes the frames run
    static Serial serial;  //serial connection
    static MenuController menuController;  //the controller for the GUI
    static NetworkConnection server;  //network connection


    static Player player1 = new Player(50);
    static Player player2 = new Player(730);
    static Ball ball = new Ball();

    static GameClass Game = new GameClass(player1, player2, ball, false); //this holds the game items and does the logic/processing for the game

    //variables
    static int sceneWidth = 820;
    static int sceneHeight = 800;
    static int canvasWidth = 800;
    static int canvasHeight = 700;

    @Override
    public void start(Stage primaryStage) throws Exception //runs when the application first starts (after main below)
    {
        //boiler playe stuff to start the screen, and make the timeline
        FXMLLoader loader  = new FXMLLoader(Main.class.getResource("Menu.fxml"));
        menuRoot = (Parent) loader.load();
        menuController = loader.getController();
        stage = primaryStage;
        menuScene = new Scene(menuRoot, 820, 850);
        gameCanvas = new Canvas(800,700);
        gamePane = new StackPane(gameCanvas); gameScene = new Scene(gamePane);
        gc = gameCanvas.getGraphicsContext2D();
        t1 = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        t1.setCycleCount(Timeline.INDEFINITE);


        serial = new Serial(); //starts a serial connection

        stage.setTitle("MENU"); //sets the name of the screen, and shows it to user
        stage.setScene(menuScene);
        stage.show();


        server = new Server(data -> //this initiates the server, and tells it what to do when it receives data
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Game.p2.y = Integer.parseInt(data.toString()); //takes the string that the client has sent, and sets as p2 y
                }
            });
        });


        server.startConnection(); //once the server has been initated above, get the network to start doing stuff

    }



    @Override
    public void stop() throws  Exception //runs when the application is closed
    {
        server.closeConnection();
    }


    static public void startGame() //method called when the PLAY button is hit
    {
        stage.setTitle("PONG"); //changes the name of the screen and puts the game scene on, replacing the GUI
        stage.setScene(gameScene);
        stage.show();
        run(gc); //makes the game run one time, to display to the screen
        pause(2000); //wait 2 reconds
        t1.play(); //starts the frames
    }

    public static void main(String[] args) //runs one time when the app starts, before the start above
    {

        launch(args);

    }

    static public void run(GraphicsContext gc) //this is when the timeline calls so what gets run every fram
    {
        Game.getInfo(); //gets the info for the game
        Game.ball.move(); //draws ball, would be inside the game.draw method but moved to here so that the class still works on the server application
        Game.draw(gc);


    }

    public static int rng(int bound) //gets random number from 0 to bound
    {
        Random rand = new Random();
        return rand.nextInt(bound);

    }

    public static void pause(int millis) //pauses for x millis
    {
        t1.stop();
        t1.setDelay(Duration.millis(millis));
        t1.play();
    }


    public static  String getIP() //gets this computer's IP Address
    {
        InetAddress ip;
        try{
            ip = InetAddress.getLocalHost();
            return ip.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return "NULL";
    }


}

