package gameEngine;

import gameEngine.bubbles.Bubbles;
import gui.GameOverPageController;
import gui.GamePlayController;
import javafx.animation.*;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

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

    // TODO : assign to users choice in main menu
    public static boolean IS_CLASSIC = true;

    public GamePlay(App _app) throws IOException { // create a new game and a new player, sep. constructor for deserialize, IOException is always managed by caller
        this.app = _app;
        this.scene = this.app.getScene();

        Stage primaryStage = (Stage) this.scene.getWindow();
        primaryStage.requestFocus(); // in case, overshadowed by secondary stage

        this.resetBgMusic();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GamePlay.fxml"));
        this.canvasContainer = loader.load(); // for adding a button and a canvas
        GamePlayController gamePlayController = loader.getController(); // Controller, for handling mouse events
        StackPane rootContainer = (StackPane) this.scene.getRoot();
        assert (rootContainer.getChildren().size() == 0); // as all remove their roots before initiating gameplay
        rootContainer.getChildren().add(canvasContainer);

        this.canvas = (Canvas) canvasContainer.getChildren().get(0);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Renderer.init(graphicsContext); // Each gameplay has it's own renderer

        this.player = new Player();
        this.game = new Game(graphicsContext, this.player, this.app);
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

    // TODO under review
    public GamePlay(App _app, Game _game, boolean isRevival) throws IOException { // in case a game is loaded from gameplay
        assert (_game.getPlayer().getId() != -1 || _game.getPlayer().getHasRevived()); // it's a saved game, or its from revival

        this.app = _app;
        this.scene = this.app.getScene();
        this.game = _game;
        this.player = this.game.getPlayer();

        GamePlay.IS_CLASSIC = this.player.getIsClassicMode(); // set mode
        System.out.println(this.getClass().toString() + "on game reformation, isClassic mode: " + GamePlay.IS_CLASSIC);

        Stage primaryStage = (Stage) this.scene.getWindow();
        primaryStage.requestFocus(); // in case, overshadowed by secondary stage
        this.resetBgMusic();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GamePlay.fxml"));
        this.canvasContainer = loader.load(); // for adding a button and a canvas
        GamePlayController gamePlayController = loader.getController(); // Controller, for handling mouse events
        StackPane rootContainer = (StackPane) this.scene.getRoot();
        assert (rootContainer.getChildren().size() == 0); // as all remove their roots before initiating gameplay
        rootContainer.getChildren().add(canvasContainer);

        this.canvas = (Canvas) canvasContainer.getChildren().get(0);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Renderer.init(graphicsContext);

        // set transient variables & refresh game
        if (!isRevival)
            this.game.reloadParam(graphicsContext, this.app);
        else // reset graphics context
            this.game.reloadParamRevival(graphicsContext);

        gamePlayController.init(this, this.app); // Controller, for referring game, needs to have app reference for actions like exit
        this.animationTimer = new GamePlayAnimationTimer(graphicsContext, this.game, this);

        this.game.getBall().removeGravity(); // else ball will fall down
        double prevVelocity = this.game.getBall().getVelocity(); // store velocity
        this.game.getBall().setVelocity(0);
        // wait for user input
        Circle glowCircle = gamePlayController.getGlowCircle();
        glowCircle.setVisible(true);
        glowCircle.setCenterX(this.game.getBall().getX());
        glowCircle.setCenterY(this.game.getBall().getY());

        Timeline animGlow = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glowCircle.scaleXProperty(), glowCircle.getScaleX(), Interpolator.EASE_IN), new KeyValue(glowCircle.scaleYProperty(), glowCircle.getScaleY(), Interpolator.EASE_IN)),
                new KeyFrame(Duration.seconds(1), new KeyValue(glowCircle.scaleXProperty(), glowCircle.getScaleX() + 0.2, Interpolator.EASE_IN), new KeyValue(glowCircle.scaleYProperty(), glowCircle.getScaleY() + 0.2, Interpolator.EASE_IN))
        );
        animGlow.setAutoReverse(true);
        animGlow.setCycleCount(Animation.INDEFINITE);

        // temp rectangle for blocking input
        Rectangle tempR = new Rectangle();
        tempR.setWidth(GamePlay.WIDTH);
        tempR.setHeight(GamePlay.HEIGHT);
        tempR.setFill(Paint.valueOf("#000000"));
        tempR.setOpacity(0.0);
        rootContainer.getChildren().add(tempR);

        EventHandler<KeyEvent> resumeEventHandler = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                System.out.println(this.getClass().toString() + " game resumed");
                assert (animGlow.getStatus() == Animation.Status.RUNNING);
                animGlow.stop();

                // reset glow circle and event handler and start animation, add gravity
                glowCircle.setCenterX(0);
                glowCircle.setCenterY(0);
                glowCircle.setVisible(false);

                // remove tempR
                rootContainer.getChildren().remove(tempR);

                GamePlay.JumpEventHandler = subKeyEvent -> {
                    if (subKeyEvent.getCode() == KeyCode.SPACE) {
                        game.registerJump();
                    }
                };
                canvas.requestFocus(); // very very important
                canvas.addEventHandler(KeyEvent.KEY_PRESSED, GamePlay.JumpEventHandler);
                this.game.getBall().setVelocity(prevVelocity);
                this.game.getBall().resetGravity();
                game.registerJump(); // as jump should follow while resetting event handler
            }
        };
        tempR.addEventHandler(KeyEvent.KEY_PRESSED, resumeEventHandler);
        tempR.requestFocus(); // for resuming, IMP

        this.animationTimer.start();
        animGlow.play();
    }

    private void resetBgMusic() {
        // fade down current music
//        App.BgMediaPlayer.stop(); // TEMPORARY, TODO: MUSIC DISABLE , (Comment below code)
        Timeline timelineMusicFadeOut = new Timeline();
        KeyValue kvMusicFadeOut = new KeyValue(App.BgMediaPlayer.volumeProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfMusicFadeOut = new KeyFrame(Duration.seconds(0.25), kvMusicFadeOut);
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

        if (this.app.getHighscore() < this.player.getScore()) { // player
            // do some animation maybe?
            System.out.println(this.getClass().toString() + " Highscore beaten!");
            this.app.setHighscore(this.player.getScore()); // in case not saved for LB entry, then reset highscore
        }

        this.animationTimer.stop(); // automatically resets previous time variables

        // remove from database, checks for revival too
        if (this.player.getId() != -1) {
            assert (this.player == this.game.getPlayer()); // just in case
            this.app.removeGame(this.game);
        }

        // update total stars as game is finished, in case of revival it returns 0
        this.app.addTotalStars(this.game.getPlayer().getScore() - this.game.getPlayer().getScoreBeforeRevival());

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
            // check for LB entry
            gameOverPageController.checkForLeaderboard();
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

    private final Rectangle blockTempR; // for blocking input, during game over (during swarm explosion, no input to be registered)
    private final StackPane rootContainer;
    private final Bubbles bubbles;

    GamePlayAnimationTimer(GraphicsContext _graphicsContext, Game _game, GamePlay _gamePlay) {
        this.graphicsContext = _graphicsContext;
        this.game = _game;
        this.gamePlay = _gamePlay;
        this.bubbles = new Bubbles(_graphicsContext);

        Scene scene = this.graphicsContext.getCanvas().getScene();
        rootContainer = (StackPane) scene.getRoot();

        blockTempR = new Rectangle();
        blockTempR.setWidth(GamePlay.WIDTH);
        blockTempR.setHeight(GamePlay.HEIGHT);
        blockTempR.setFill(Paint.valueOf("#000000"));
        blockTempR.setOpacity(0.0);
    }

    @Override
    public void handle(long currentNanoTime) {
        if (GamePlay.PreviousFrameTime == -1) {
            GamePlay.PreviousFrameTime = currentNanoTime;
            return;
        }

        graphicsContext.restore();
        graphicsContext.save();

        graphicsContext.setFill(Color.web("000000"));
        graphicsContext.fillRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);

        double timeDifference = (double) (currentNanoTime - GamePlay.PreviousFrameTime) / 1000000000;
        GamePlay.PreviousFrameTime = currentNanoTime;

        if (!GamePlay.IS_CLASSIC) {
            bubbles.move(timeDifference);
            bubbles.clip();
            if (!game.isGameOver())
                game.getBall().clipBall();
            graphicsContext.clip();
            graphicsContext.closePath();
        }

        if (game.isGameOver()) { // need to check before, as logic is updated but gui also has to be updated IMP, update event handler for canvas
            if (GamePlay.GameOverTime == -1) {
                this.rootContainer.getChildren().add(this.blockTempR);
                GamePlay.GameOverTime = currentNanoTime;
                this.gamePlay.getCanvas().removeEventHandler(KeyEvent.KEY_PRESSED, GamePlay.JumpEventHandler);
            } else {
                double diff = (double) (currentNanoTime - GamePlay.GameOverTime) / 1000000000;
                if (diff > 1.5) {
                    this.rootContainer.getChildren().remove(this.blockTempR);
                    gamePlay.gameOver();
                }
            }
        }

        graphicsContext.clearRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);
        graphicsContext.setFill(Color.web("0D152C"));
        graphicsContext.fillRect(0, 0, GamePlay.WIDTH, GamePlay.HEIGHT);

        game.checkAndUpdate(timeDifference);
        game.refreshGameElements();

        if (!GamePlay.IS_CLASSIC) {
            bubbles.color();
        }

    }

    @Override
    public void stop() { // stop the animation timer and reset values for prev frame and game over time
        super.stop();
        GamePlay.PreviousFrameTime = -1;
        GamePlay.GameOverTime = -1;
    }
}
