package gameEngine;

import gui.GameOverPageController;
import gui.GamePlayController;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.util.Duration;

import java.awt.desktop.AppReopenedEvent;
import java.io.File;
import java.io.IOException;

// TODO: IMP, ball fall, it doesn't fall down completely

// GamePlay comprises games and player, a player can have multiple games, during deserialization, require a player, and a game for the player
// Game associates with a Player
// each player is anonymous at start, unless chooses a save

// only add to highscore if saved ? o/w how will we retrieve details?

// This simulates a secondary controller
public class GamePlay {

    public static double HEIGHT = 700;
    public static double WIDTH = 450;
    private final Scene scene;
    private final AnchorPane canvasContainer; // root of rootContainer
    private final Canvas canvas;

    private final App app;
    private final Game game;
    private final Player player;
    private final GamePlayAnimationTimer animationTimer;

    public static long PreviousFrameTime = -1;
    public static long GameOverTime = -1;
    public static EventHandler<KeyEvent> JumpEventHandler; // every game (in case multiple) will have same event handler for Jump

    public GamePlay(App _app) throws IOException { // create a new game and a new player, sep. constructor for deserialize

        this.app = _app;
        this.scene = this.app.getScene();

        this.resetBgMusic();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GamePlay.fxml"));
        this.canvasContainer = loader.load(); // for adding a button and a canvas
        GamePlayController gamePlayController = loader.getController(); // Controller, for handling mouse events
        StackPane rootContainer = (StackPane) this.scene.getRoot();
        assert (rootContainer.getChildren().size() == 0); // as all remove their roots before initiating gameplay
        rootContainer.getChildren().add(canvasContainer);

        this.canvas = (Canvas) canvasContainer.getChildren().get(0);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        this.player = new Player();
        this.game = new Game(graphicsContext, this.player);
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

    private void resetBgMusic() {
        // fade down current music
//        App.BgMediaPlayer.stop(); // TEMPORARY, TODO: MUSIC DISABLE , (Comment below code)
        Timeline timelineMusicFadeOut = new Timeline();
        KeyValue kvMusicFadeOut = new KeyValue(App.BgMediaPlayer.volumeProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfMusicFadeOut = new KeyFrame(Duration.seconds(0.5), kvMusicFadeOut);
        timelineMusicFadeOut.getKeyFrames().add(kfMusicFadeOut);
        timelineMusicFadeOut.setOnFinished((t) -> {
            App.BgMediaPlayer.stop();
            Media bgMusic = new Media(new File("src/assets/music/gameplay/bg3.mp3").toURI().toString());
            App.BgMediaPlayer = new MediaPlayer(bgMusic);
            App.BgMediaPlayer.setAutoPlay(true);
            App.BgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            App.BgMediaPlayer.play();
        });
        timelineMusicFadeOut.play();
    }

    public void gameOver() { // imp request focus
        assert (this.game.isGameOver()); // only single entry point, just in case

        // check for highscore (IMP may not be the only checking point)
        if (this.app.getHighscore() < this.player.getScore()) { // player or game?
            // do some animation maybe?
            System.out.println(this.getClass().toString() + " Highscore beaten!");
            this.app.setHighscore(this.player.getScore());
        }

        this.animationTimer.stop(); // automatically resets previous time variables

        // remove from database
        if (this.player.getId() != -1) {
            assert (this.player == this.game.getPlayer()); // just in case
            this.app.removeGame(this.game);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GameOverPage.fxml"));
        try {
            AnchorPane gameOverRoot = loader.load(); // TODO: create init for instances, rn only for animations
            GameOverPageController gameOverPageController = loader.getController();
            gameOverPageController.init(this.app, this.game); // for purposes such as returning back to main page
            StackPane rootContainer = (StackPane) this.scene.getRoot();
            assert (rootContainer.getChildren().size() == 1);
            rootContainer.getChildren().remove(this.canvasContainer);
            rootContainer.getChildren().add(gameOverRoot);
            gameOverRoot.requestFocus(); // IMP, current focus on canvas
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " Failed to load game over page");
            e.printStackTrace();
        }
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public AnimationTimer getAnimationTimer() {
        return this.animationTimer;
    }

    public Canvas getCanvas() {
        return canvas;
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
            if (GamePlay.GameOverTime == -1) {
                GamePlay.GameOverTime = currentNanoTime;
            } else {
                double diff = (double)(currentNanoTime - GamePlay.GameOverTime)/1000000000;
                if (diff > 1.5) {
                    gamePlay.gameOver();
                }
            }
        }

        double timeDifference = (double) (currentNanoTime - GamePlay.PreviousFrameTime) / 1000000000;
        GamePlay.PreviousFrameTime = currentNanoTime;

        graphicsContext.clearRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);
        graphicsContext.setFill(Color.web("0D152C"));
        graphicsContext.fillRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);
        game.checkAndUpdate(timeDifference);
        game.refreshGameElements();
    }

    @Override
    public void stop() { // stop the animation timer and reset values for prev frame and game over time
        super.stop();
        GamePlay.PreviousFrameTime = -1;
        GamePlay.GameOverTime = -1;
    }
}
