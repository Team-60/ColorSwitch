package GameEngine;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    static double HEIGHT = 700;
    static double WIDTH = 450;
    static GraphicsContext graphicsContext;
    @Override
    public void start(Stage stage) {

        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        graphicsContext = canvas.getGraphicsContext2D();

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

        Game game = new Game(graphicsContext);

        // TODO: find a class to put AnimationTimer in
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

    public static void main(String[] args) {
        launch(args);
    }
}
