package gameEngine;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

// This simulates a controller
public class GameApp {

    static double HEIGHT = 700;
    static double WIDTH = 450;
    GraphicsContext graphicsContext;

    public GameApp(Scene scene) {

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        this.graphicsContext = canvas.getGraphicsContext2D();

        StackPane stackPane = (StackPane) scene.getRoot();
        stackPane.getChildren().add(canvas);

        Game game = new Game(graphicsContext);

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
                graphicsContext.setFill(Color.BLACK);
                graphicsContext.fillRect(0, 0, WIDTH, HEIGHT);
                game.checkAndUpdate(timeDifference);
                game.refreshGameElements();

            }
        }.start();

        // TODO: find a place for EventHandler
        // TODO: bug case : continuous space pressed
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(KeyCode.SPACE == key.getCode()) {
                game.registerJump();
            }
        });

    }
}
