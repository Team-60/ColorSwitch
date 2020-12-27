package gui;

import gameEngine.App;
import gameEngine.Game;
import gameEngine.GamePlay;
import gameEngine.Player;
import gameEngine.customExceptions.InsufficientStarsException;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class GameOverPageController {

    private App app;
    private Game game; // need for serializing
    private String usernameLB;

    private AudioClip hoverSound;
    private AudioClip clickSound;
    private AudioClip errorSound;

    private RotateTransition rtIconRestartUsingStars;
    private boolean disableRevival;

    private String cantReviveMsg = null; // As player is either on LB, or has used revival

    private ImageView easterEggRing; // null indicates that easter egg is disabled, if revival is disabled, disable easter egg
    private RotateTransition easterEggRt;
    private int countEasterEggClicks;
    private AudioClip[] easterEggClips;
    private Image playImgEasterEgg;

    @FXML
    private AnchorPane gameOverRoot;
    @FXML
    private ImageView logoRingL;
    @FXML
    private ImageView logoRingR;
    @FXML
    private Group iconLB;
    @FXML
    private Group iconRestart;
    @FXML
    private Group iconRestartUsingStars;
    @FXML
    private Label yourScoreLabel;
    @FXML
    private Label bestScoreLabel;
    @FXML
    private Label totalStarsLabel;

    public void init(App _app, Game _game) {
        this.app = _app;
        this.game = _game;
        this.usernameLB = null;

        this.yourScoreLabel.setText(Integer.toString(this.game.getPlayer().getScore()));
        this.bestScoreLabel.setText(Integer.toString(this.app.getHighscore()));

        // reset media with game over in case if returned from game play
        if (App.BgMediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) { // as obstacle collided, already checked in game elements
            Media bgMusic = new Media(new File("src/assets/music/gameOver1.mp3").toURI().toString());
            App.BgMediaPlayer = new MediaPlayer(bgMusic);
            App.BgMediaPlayer.setAutoPlay(true);
            App.BgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            App.BgMediaPlayer.play();
        }

        // add tooltip for revival stars count
        Tooltip tooltip = new Tooltip("Revive using " + App.REVIVAL_STARS + " stars!");
        tooltip.setStyle("-fx-font-style: italic; -fx-font-size: 10;");
        Tooltip.install(iconRestartUsingStars, tooltip);

        // set total stars
        this.totalStarsLabel.setText(Integer.toString(this.app.getTotalStars()));

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.errorSound = new AudioClip(new File("src/assets/music/mouse/error.wav").toURI().toString());
        this.clickSound.setVolume(0.5);
        this.hoverSound.setVolume(0.04);
        this.errorSound.setVolume(0.4);

        RotateTransition rtLogoRingL = new RotateTransition(Duration.millis(15000), logoRingL);
        RotateTransition rtLogoRingR = new RotateTransition(Duration.millis(15000), logoRingR);
        RotateTransition rtIconLB = new RotateTransition(Duration.millis(15000), iconLB);
        RotateTransition rtIconRestart = new RotateTransition(Duration.millis(15000), iconRestart);
        this.rtIconRestartUsingStars = new RotateTransition(Duration.millis(15000), iconRestartUsingStars);
        this.startRotation(rtLogoRingL, -1);
        this.startRotation(rtLogoRingR, -1);
        this.startRotation(rtIconLB, 1);
        this.startRotation(rtIconRestart, 1);

        if (this.game.getPlayer().getHasRevived() || this.app.getPlayerDatabase().getData().contains(this.game.getPlayer())) {
            // deactivate revival if already has revived, or the player exists on leaderboard (different entry point)
            Tooltip.install(this.iconRestartUsingStars, null);
            this.cantReviveMsg = "Revival already used!";
            this.deactivateRevival();
        }
        else
            this.startRotation(rtIconRestartUsingStars, 1);

        // set easter egg, stop rotate transition on L / R
        this.setEasterEgg(rtLogoRingL, rtLogoRingR);
    }

    public void checkForLeaderboard() { // LB stores the best performance of players
        assert (this.game.isGameOver()); // reconfirm

        // check if viable for LB
        ArrayList<Player> players = this.app.getPlayerDatabase().getData();
        System.out.println(this.getClass().toString() + " players database sz: " + players.size());
        System.out.println(this.getClass().toString() + "\n" + this.game.getPlayer());
        boolean madeToLb = true;
        if (players.size() == 6 && players.get(players.size() - 1).compareTo(this.game.getPlayer()) < 0) // last LB player has better score
            madeToLb = false;

        if (madeToLb) {
            // get input from inputPopup
            Scene scene = this.app.getScene();
            StackPane rootContainer = (StackPane) scene.getRoot();
            // temp rectangle for blocking input
            Rectangle tempR = new Rectangle();
            tempR.setWidth(GamePlay.WIDTH);
            tempR.setHeight(GamePlay.HEIGHT);
            tempR.setFill(Paint.valueOf("#000000"));
            tempR.setOpacity(0.75);
            rootContainer.getChildren().add(tempR);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("InputPopup.fxml"));
            Parent inputRoot = null;
            try {
                inputRoot = loader.load();
            } catch (IOException e) {
                System.out.println(this.getClass().toString() + " failed to load input root");
                e.printStackTrace();
            }
            assert (inputRoot != null);

            InputPopupController<GameOverPageController> inputPopupController = loader.getController();
            inputPopupController.init(this.game.getPlayer(), this);

            Scene secondaryScene = new Scene(inputRoot);
            secondaryScene.setCursor(new ImageCursor(new Image(new File("src/assets/inputPopup/cursor.png").toURI().toString())));
            Stage secondaryStage = new Stage(StageStyle.UNDECORATED);
            Stage primaryStage = (Stage) this.app.getScene().getWindow();
            secondaryStage.initOwner(primaryStage); // imp. for them to act as one stage
            secondaryStage.setScene(secondaryScene);
            secondaryStage.setOnHidden(t -> this.processInputLB(rootContainer, inputPopupController, tempR)); // IMP, as stage can only be waited in an event handler
            secondaryStage.show();
            inputRoot.requestFocus(); // IMP, after showing stage
        }
    }

    private void processInputLB(StackPane rootContainer, InputPopupController<GameOverPageController> inputPopupController, Rectangle tempR) {
        assert (inputPopupController.getSaveSuccess() == (this.usernameLB != null));
        rootContainer.getChildren().remove(tempR); // regain focus
        this.gameOverRoot.requestFocus();

        if (inputPopupController.getSaveSuccess()) {
            Tooltip.install(this.iconRestartUsingStars, null);
            this.cantReviveMsg = "Player exists on Leaderboard!";
            this.deactivateRevival(); // made it to LB, deactivate revival now
            this.easterEggRing = null; // deactivate easter egg

            System.out.println(this.getClass().toString() + " " + this.usernameLB + " received");
            this.app.addToLB(this.game.getPlayer(), this.usernameLB); // as the player doesn't exist on LB
        } else { // reset highscore in case this was the current highscore as not saved
            this.app.calcHighscore();
            System.out.println(this.getClass().toString() + " lb entry cancelled");
        }
    }

    private void setEasterEgg(RotateTransition lrl, RotateTransition lrr) {
        this.countEasterEggClicks = 0;

        this.easterEggClips = new AudioClip[3];
        for (int i = 0; i <= 2; ++ i) {
            this.easterEggClips[i] = new AudioClip(new File("src/assets/music/mouse/easterEgg_click" + i + "." + (i == 0 ? "wav" : "mp3")).toURI().toString());
            this.easterEggClips[i].setVolume((i == 2) ? 1 : 0.5);
        }

        this.playImgEasterEgg = new Image(new File("src/assets/gameOverPage/play.png").toURI().toString());

        int rand = (new Random()).nextInt(1000);
        System.out.println(this.getClass().toString() + " rand: " + rand);
        if (rand % 4 == 0) {
            System.out.println(this.getClass().toString() + " created EELRL");
            this.easterEggRing = this.logoRingL;
            this.easterEggRt = lrl;
        } else if (rand % 4 == 2) {
            System.out.println(this.getClass().toString() + " created EELRR");
            this.easterEggRing = this.logoRingR;
            this.easterEggRt = lrr;
        } else {
            this.easterEggRing = null;
        }

        if (this.disableRevival) { // null indicates that easter egg is disabled, if revival is disabled, disable easter egg
            this.easterEggRing = null;
        }
    }

    @FXML
    public void playEasterEggClick(MouseEvent mouseEvent) {
        ImageView img = (ImageView) mouseEvent.getSource();
        assert (img != null); // as null is a checking condition
        if (img != this.easterEggRing) return;

        if (this.countEasterEggClicks == 2) { // init the easter egg
            App.BgMediaPlayer.setVolume(0); // automatically gameplay will overwrite with it's own media
            this.easterEggClips[2].play();

            try { // wait for the audioClip to play
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(this.getClass().toString() + " failed to sleep thread");
            }

            // free revival
            // record previous score & set revival status
            this.game.getPlayer().setHasRevived();
            this.game.getPlayer().setScoreBeforeRevival();

            System.out.println(this.getClass().toString() + " free revival used! Easter egg discovered");
            new Dialog("Free revival used!", (Stage) this.app.getScene().getWindow());

            Scene scene = this.app.getScene();
            StackPane rootContainer = (StackPane) scene.getRoot();
            assert (rootContainer.getChildren().size() == 1);
            rootContainer.getChildren().remove(this.gameOverRoot);
            try {
                this.game.revive(); // refresh game parameters
                new GamePlay(this.app, this.game, true);
            } catch (IOException e) {
                System.out.println(this.getClass().toString() + " New game failed to load!");
                e.printStackTrace();
            }
        } else {
            this.easterEggClips[this.countEasterEggClicks ++].play();
            if (this.countEasterEggClicks == 2) {
                Timeline scaleAnim = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(this.easterEggRing.scaleXProperty(), this.easterEggRing.getScaleX(), Interpolator.EASE_IN), new KeyValue(this.easterEggRing.scaleYProperty(), this.easterEggRing.getScaleY(), Interpolator.EASE_IN)),
                        new KeyFrame(Duration.seconds(0.6), new KeyValue(this.easterEggRing.scaleXProperty(), 1.075, Interpolator.EASE_IN), new KeyValue(this.easterEggRing.scaleYProperty(), 1.075, Interpolator.EASE_IN))
                );
                scaleAnim.setAutoReverse(true);
                scaleAnim.setCycleCount(Animation.INDEFINITE);

                FadeTransition ftIn = new FadeTransition(Duration.seconds(0.5), this.easterEggRing);
                ftIn.setToValue(1);
                ftIn.setOnFinished(t -> scaleAnim.play());

                FadeTransition ftOut = new FadeTransition(Duration.seconds(0.5), this.easterEggRing);
                ftOut.setToValue(0.0);
                ftOut.setOnFinished(t -> {
                    this.easterEggRing.setImage(this.playImgEasterEgg);
                    this.easterEggRt.stop();
                    this.easterEggRing.setRotate(0);
                    ftIn.play();
                });
                ftOut.play();
            }

        }
    }

    private void deactivateRevival() {
        System.out.println(this.getClass().toString() + " deactivated revival");
        this.disableRevival = true;
        if (this.rtIconRestartUsingStars != null)
            this.rtIconRestartUsingStars.stop();
        this.iconRestartUsingStars.setRotate(0);
        Circle circle = (Circle) this.iconRestartUsingStars.getChildren().get(0);
        circle.setFill(Color.web("#808588"));
        this.iconRestartUsingStars.setOpacity(0.75);
    }

    private void startRotation(RotateTransition rt, int dir) {
        rt.setByAngle(dir * 720);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }

    @FXML
    public void iconHoverActive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        if (group == this.iconRestartUsingStars && this.disableRevival) {
            Scene scene = this.app.getScene();
            scene.setCursor(new ImageCursor(new Image(new File("src/assets/gameOverPage/cursorBlocked.png").toURI().toString())));
            return;
        }
        this.hoverSound.play();
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(1.1);
        group.setScaleY(1.1);
        circle.setFill(Color.web("#FFB52E"));
    }

    @FXML
    public void iconHoverInactive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        if (group == this.iconRestartUsingStars && this.disableRevival) {
            Scene scene = this.app.getScene();
            scene.setCursor(new ImageCursor(new Image(new File("src/assets/gameOverPage/cursor.png").toURI().toString())));
            return;
        }
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(1);
        group.setScaleY(1);
        circle.setFill(Color.YELLOW);
    }

    @FXML
    public void iconLBClicked() {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LBPage.fxml"));
        Parent lBRoot = null;
        try {
            lBRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load LB page");
        }
        assert (lBRoot != null);
        LBPageController lbPageController = loader.getController();
        lbPageController.init(this.app, this.game, false);
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().add(lBRoot);

        // temp rectangle for fade purpose
        Rectangle fadeR = new Rectangle();
        fadeR.setWidth(GamePlay.WIDTH);
        fadeR.setHeight(GamePlay.HEIGHT);
        fadeR.setFill(Paint.valueOf("#000000"));
        fadeR.setOpacity(0);
        rootContainer.getChildren().add(fadeR);

        lBRoot.translateXProperty().set(-GamePlay.WIDTH);
        Timeline timelineSlide = new Timeline();
        KeyValue kvSlide = new KeyValue(lBRoot.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfSlide = new KeyFrame(Duration.seconds(1), kvSlide);
        timelineSlide.getKeyFrames().add(kfSlide);
        timelineSlide.setOnFinished(t -> rootContainer.getChildren().remove(this.gameOverRoot));

        Timeline timelineFadeOut = new Timeline();
        KeyValue kvFadeOut = new KeyValue(fadeR.opacityProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfFadeOut = new KeyFrame(Duration.seconds(0.5), kvFadeOut);
        timelineFadeOut.getKeyFrames().add(kfFadeOut);
        timelineFadeOut.setOnFinished(t -> rootContainer.getChildren().remove(fadeR));

        Timeline timelineFadeIn = new Timeline();
        KeyValue kvFadeIn = new KeyValue(fadeR.opacityProperty(), 0.75, Interpolator.EASE_IN);
        KeyFrame kfFadeIn = new KeyFrame(Duration.seconds(1), kvFadeIn);
        timelineFadeIn.getKeyFrames().add(kfFadeIn);
        timelineFadeIn.setOnFinished(t -> timelineFadeOut.play());

        timelineSlide.play();
        timelineFadeIn.play();
    }

    @FXML
    public void returnIconHoverActive(MouseEvent mouseEvent) {
        this.hoverSound.play();
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        circle.setFill(Color.web("#a9a9a9"));
    }

    @FXML
    public void returnIconHoverInactive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        circle.setFill(Color.web("#d3d3d3"));
    }

    @FXML
    public void returnIconClicked() {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        Parent mainPageRoot = null;
        try {
            mainPageRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load mainPageRoot");
            e.printStackTrace();
        }
        assert (mainPageRoot != null);
        MainPageController mainPageController = loader.getController();
        mainPageController.init(this.app);

        // fade down current music
        Timeline timelineMusicFadeOut = new Timeline();
        KeyValue kvMusicFadeOut = new KeyValue(App.BgMediaPlayer.volumeProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfMusicFadeOut = new KeyFrame(Duration.seconds(1), kvMusicFadeOut);
        timelineMusicFadeOut.getKeyFrames().add(kfMusicFadeOut);
        timelineMusicFadeOut.setOnFinished(t -> this.app.addAssets()); // for refreshing bg music

        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().add(mainPageRoot);

        // temp rectangle for fade purpose
        Rectangle fadeR = new Rectangle();
        fadeR.setWidth(GamePlay.WIDTH);
        fadeR.setHeight(GamePlay.HEIGHT);
        fadeR.setFill(Paint.valueOf("#000000"));
        fadeR.setOpacity(0);
        rootContainer.getChildren().add(fadeR);

        mainPageRoot.translateXProperty().set(-GamePlay.WIDTH);
        Timeline timelineSlide = new Timeline();
        KeyValue kvSlide = new KeyValue(mainPageRoot.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfSlide = new KeyFrame(Duration.seconds(1), kvSlide);
        timelineSlide.getKeyFrames().add(kfSlide);
        timelineSlide.setOnFinished(t -> rootContainer.getChildren().remove(this.gameOverRoot));

        Timeline timelineFadeOut = new Timeline();
        KeyValue kvFadeOut = new KeyValue(fadeR.opacityProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfFadeOut = new KeyFrame(Duration.seconds(0.5), kvFadeOut);
        timelineFadeOut.getKeyFrames().add(kfFadeOut);
        timelineFadeOut.setOnFinished(t -> rootContainer.getChildren().remove(fadeR));

        Timeline timelineFadeIn = new Timeline();
        KeyValue kvFadeIn = new KeyValue(fadeR.opacityProperty(), 0.75, Interpolator.EASE_IN);
        KeyFrame kfFadeIn = new KeyFrame(Duration.seconds(1), kvFadeIn);
        timelineFadeIn.getKeyFrames().add(kfFadeIn);
        timelineFadeIn.setOnFinished(t -> timelineFadeOut.play());

        timelineMusicFadeOut.play();
        timelineSlide.play();
        timelineFadeIn.play();
    }

    @FXML
    public void restartIconClicked() {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().remove(this.gameOverRoot);
        try {
            new GamePlay(this.app);
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " New game failed to load!");
            e.printStackTrace();
        }
    }

    @FXML
    public void restartUsingStarsClicked() { // based on score?, keep a total of stars
        if (this.disableRevival) {
            new Dialog(this.cantReviveMsg, (Stage) this.app.getScene().getWindow());
            return;
        }

        try {
            if (!this.app.decTotalStars(App.REVIVAL_STARS)) // check if possible & then decrease current stars & reset label
                throw new InsufficientStarsException(this.app.getTotalStars());
            this.totalStarsLabel.setText(Integer.toString(this.app.getTotalStars()));

            this.clickSound.play();

            // record previous score & set revival status
            this.game.getPlayer().setHasRevived();
            this.game.getPlayer().setScoreBeforeRevival();

            System.out.println(this.getClass().toString() + " restart using stars success");
            new Dialog(App.REVIVAL_STARS + " stars used for revival!", (Stage) this.app.getScene().getWindow());

            Scene scene = this.app.getScene();
            StackPane rootContainer = (StackPane) scene.getRoot();
            assert (rootContainer.getChildren().size() == 1);
            rootContainer.getChildren().remove(this.gameOverRoot);
            try {
                this.game.revive(); // refresh game parameters
                new GamePlay(this.app, this.game, true);
            } catch (IOException e) {
                System.out.println(this.getClass().toString() + " New game failed to load!");
                e.printStackTrace();
            }
        } catch (InsufficientStarsException e) {
            System.out.println(this.getClass().toString() + ": " + e.toString());
            System.out.println(this.getClass().toString() + ": " + e.getMessage() + "\n");

            this.errorSound.play();
            new Dialog("Need " + App.REVIVAL_STARS + " stars!", (Stage) this.app.getScene().getWindow());
        }
    }

    public void setUsernameLB(String name) {
        assert (name != null);
        this.usernameLB = name;
    }
}
