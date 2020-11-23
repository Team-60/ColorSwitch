package gameEngine;

import gui.GameOverPageController;
import gui.GamePlayController;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;

// TODO: IMP, ball fall, it doesn't fall down completely

// This simulates a secondary controller
public class GamePlay {

    public static double HEIGHT = 700;
    public static double WIDTH = 450;
    private final Scene scene;
    private final AnchorPane canvasContainer; // root of rootContainer
    private final Canvas canvas;

    private final App app;
    private final Game game;
    private final GamePlayAnimationTimer animationTimer;
    public static long PreviousFrameTime = -1;
    public static EventHandler<KeyEvent> JumpEventHandler; // every game (in case multiple) will have same event handler for Jump

    public GamePlay(App _app) throws IOException {

        this.app = _app;
        this.scene = this.app.getScene();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GamePlay.fxml"));
        this.canvasContainer = loader.load(); // for adding a button and a canvas
        GamePlayController gamePlayController = loader.getController(); // Controller, for handling mouse events
        StackPane stackPane = (StackPane) this.scene.getRoot();
        stackPane.getChildren().add(canvasContainer);

        this.canvas = (Canvas) canvasContainer.getChildren().get(0);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        this.game = new Game(graphicsContext);
        gamePlayController.init(this, this.app); // Controller, for referring game, needs to have app reference for actions like exit

        GamePlay.JumpEventHandler = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                game.registerJump();
            }
        };
        // TODO: bug case : continuous space pressed
        canvas.requestFocus(); // very very important
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, GamePlay.JumpEventHandler);

        this.animationTimer = new GamePlayAnimationTimer(graphicsContext, this.game, this);
        this.animationTimer.start();
    }

    public AnimationTimer getAnimationTimer() {
        return this.animationTimer;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void gameOver() { // imp request focus
        GamePlay.PreviousFrameTime = -1; // IMP for next iteration of game
//        try {
//            Thread.sleep(10);
//        }
//        catch (InterruptedException e) {
//            assert false;
//        }
        this.animationTimer.stop();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            System.out.println(this.getClass().toString() + " Thread sleep failed");
            e.printStackTrace();
        } finally {
            assert (App.BgMediaPlayer.getStatus() == MediaPlayer.Status.STOPPED); // as obstacle collided, already checked in game elements
            Media bgMusic = new Media(new File("src/assets/music/gameOver.mp3").toURI().toString());
            App.BgMediaPlayer = new MediaPlayer(bgMusic);
            App.BgMediaPlayer.setAutoPlay(true);
            App.BgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            App.BgMediaPlayer.play();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GameOverPage.fxml"));
            try {
                AnchorPane gameOverRoot = loader.load(); // TODO: create init for instances, rn only for animations
                GameOverPageController gameOverPageController = loader.getController();
                gameOverPageController.init(this.app); // for purposes such as returning back to main page
                StackPane rootContainer = (StackPane) this.scene.getRoot();
                rootContainer.getChildren().remove(this.canvasContainer);
                rootContainer.getChildren().add(gameOverRoot);
                gameOverRoot.requestFocus(); // IMP, current focus on canvas
            } catch (IOException e) {
                System.out.println(this.getClass().toString() + " Failed to load game over page");
                e.printStackTrace();
            }
        }
    }
}

class GamePlayAnimationTimer extends AnimationTimer {

    private final GraphicsContext graphicsContext;
    private final Game game;
    private final GamePlay gamePlay;

    GamePlayAnimationTimer(GraphicsContext _graphicsContext, Game _game, GamePlay _gamePlay) {
        this.graphicsContext = _graphicsContext;
        this.game = _game;
        this.gamePlay = _gamePlay;
    }

    @Override
    public void handle(long currentNanoTime) {
        if (GamePlay.PreviousFrameTime == -1) {
            GamePlay.PreviousFrameTime = currentNanoTime;
            return;
        }
        if (game.isGameOver()) { // need to check before, as logic is updated but gui also has to be updated IMP
            this.gamePlay.gameOver();
        }
        double timeDifference = (double)(currentNanoTime - GamePlay.PreviousFrameTime)/1000000000;
        GamePlay.PreviousFrameTime = currentNanoTime;

        graphicsContext.clearRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);
        graphicsContext.setFill(Color.web("0D152C"));
        graphicsContext.fillRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);
        game.checkAndUpdate(timeDifference);
        game.refreshGameElements();
    }
}
