package gameEngine;

import gui.GamePlayController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;

// This simulates a controller
public class GamePlay {

    static double HEIGHT = 700;
    static double WIDTH = 450;
    GraphicsContext graphicsContext;

    public GamePlay(Scene scene) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GamePlay.fxml"));
        AnchorPane canvasContainer = loader.load(); // for adding a button and a canvas
        GamePlayController gamePlayController = loader.getController(); // Controller, for handling mouse events

        Canvas canvas = (Canvas) canvasContainer.getChildren().get(0);
        this.graphicsContext = canvas.getGraphicsContext2D();

        StackPane stackPane = (StackPane) scene.getRoot();
        stackPane.getChildren().add(canvasContainer);

        Game game = new Game(graphicsContext);
        gamePlayController.init(game); // Controller, for referring game

        // TODO: find a place for EventHandler
        // TODO: bug case : continuous space pressed
        canvas.requestFocus(); // very very important
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(KeyCode.SPACE == key.getCode()) {
                game.registerJump();
            }
        });

        //comTODO: find a class to put AnimationTimer in
        new AnimationTimer() {
            long previousFrameTime = -1;
            @Override
            public void handle(long currentNanoTime) {
                if (previousFrameTime == -1) {
                    previousFrameTime = currentNanoTime;
                    return;
                }
                double timeDifference = (double)(currentNanoTime - previousFrameTime)/1000000000;
                previousFrameTime = currentNanoTime;

                graphicsContext.clearRect(0, 0, WIDTH, HEIGHT);
                graphicsContext.setFill(Color.web("0D152C"));
                graphicsContext.fillRect(0, 0, WIDTH, HEIGHT);
                game.checkAndUpdate(timeDifference);
                game.refreshGameElements();
            }
        }.start();
    }
    
}
