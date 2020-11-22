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

// TODO: IMP, ball fall, it doesn't fall down completely

// This simulates a controller
public class GamePlay {

    static double HEIGHT = 700;
    static double WIDTH = 450;
    private final Game game;
    private final GamePlayAnimationTimer animationTimer;

    public GamePlay(Scene scene) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GamePlay.fxml"));
        AnchorPane canvasContainer = loader.load(); // for adding a button and a canvas
        GamePlayController gamePlayController = loader.getController(); // Controller, for handling mouse events

        Canvas canvas = (Canvas) canvasContainer.getChildren().get(0);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        StackPane stackPane = (StackPane) scene.getRoot();
        stackPane.getChildren().add(canvasContainer);

        this.game = new Game(graphicsContext);
        gamePlayController.init(this); // Controller, for referring game

        // TODO: find a place for EventHandler
        // TODO: bug case : continuous space pressed
        canvas.requestFocus(); // very very important
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(KeyCode.SPACE == key.getCode()) {
                this.game.registerJump();
            }
        });

        this.animationTimer = new GamePlayAnimationTimer(graphicsContext, this.game);
        this.animationTimer.start();
    }
    
}

class GamePlayAnimationTimer extends AnimationTimer {

    private long previousFrameTime = -1;
    private final GraphicsContext graphicsContext;
    private final Game game;

    GamePlayAnimationTimer(GraphicsContext _graphicsContext, Game _game) {
        this.graphicsContext = _graphicsContext;
        this.game = _game;
    }

    @Override
    public void handle(long currentNanoTime) {
        if (previousFrameTime == -1) {
            previousFrameTime = currentNanoTime;
            return;
        }
        double timeDifference = (double)(currentNanoTime - previousFrameTime)/1000000000;
        previousFrameTime = currentNanoTime;

        graphicsContext.clearRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);
        graphicsContext.setFill(Color.web("0D152C"));
        graphicsContext.fillRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);
        game.checkAndUpdate(timeDifference);
        game.refreshGameElements();
    }
}
