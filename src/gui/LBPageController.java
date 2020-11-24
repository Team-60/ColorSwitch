package gui;

import gameEngine.App;
import gameEngine.Game;
import gameEngine.GamePlay;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class LBPageController {

    private App app;
    private Game game;
    private AudioClip hoverSound;
    private AudioClip clickSound;
    private Boolean fromMainPage; // IMP to remember to which page do we have to return (game over or main page)

    @FXML
    private AnchorPane lBRoot;
    @FXML
    private ImageView crownImg;
    @FXML
    private Group goBackGroup;

    public void init(App _app, Game _game, Boolean _fromMainPage) {

        this.app = _app;
        this.game = _game; // for referencing in case user comes from game over
        this.fromMainPage = _fromMainPage;
        assert (!this.fromMainPage || (this.game == null)); // if comes from main page, no game needs to be instantiated

        Glow glow = new Glow(0.5);
        crownImg.setEffect(glow);

        RotateTransition rt = new RotateTransition(Duration.millis(15000), goBackGroup);
        rt.setByAngle(-720);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.clickSound.setVolume(0.5);
        this.hoverSound.setVolume(0.04);

        Timeline animationCrown = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(crownImg.scaleXProperty(), crownImg.getScaleX(), Interpolator.EASE_IN), new KeyValue(crownImg.scaleYProperty(), crownImg.getScaleY(), Interpolator.EASE_IN), new KeyValue(glow.levelProperty(), glow.getLevel(), Interpolator.EASE_IN)),
                new KeyFrame(Duration.seconds(0.6), new KeyValue(crownImg.scaleXProperty(), crownImg.getScaleX() + 0.05, Interpolator.EASE_IN), new KeyValue(crownImg.scaleYProperty(), crownImg.getScaleY() + 0.05, Interpolator.EASE_IN), new KeyValue(glow.levelProperty(), glow.getLevel() + 0.4, Interpolator.EASE_IN))
        );
        animationCrown.setAutoReverse(true);
        animationCrown.setCycleCount(Animation.INDEFINITE);
        animationCrown.play();
    }

    @FXML
    public void goBackHoverActive() {
        this.hoverSound.play();
        this.goBackGroup.setScaleX(1);
        this.goBackGroup.setScaleY(1);
        Circle circle = (Circle) this.goBackGroup.getChildren().get(0);
        circle.setFill(Color.web("#FFB52E"));
    }

    @FXML
    public void goBackHoverInactive() {
        this.goBackGroup.setScaleX(0.9);
        this.goBackGroup.setScaleY(0.9);
        Circle circle = (Circle) this.goBackGroup.getChildren().get(0);
        circle.setFill(Color.YELLOW);
    }

    @FXML
    public void goBackPressed() {
        this.clickSound.play();
        if (this.fromMainPage) {
            this.goBackPressedMainPage();
        } else {
            this.goBackPressedGameOverPage();
        }
    }

    private void goBackPressedGameOverPage() {
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOverPage.fxml"));
        Parent gameOverRoot = null;
        try {
            gameOverRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load game over page");
            e.printStackTrace();
        }
        assert (gameOverRoot != null);
        GameOverPageController gameOverPageController = loader.getController();
        gameOverPageController.init(this.app, this.game);
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().add(gameOverRoot);

        // temp rectangle for fade purpose
        Rectangle fadeR = new Rectangle();
        fadeR.setWidth(GamePlay.WIDTH);
        fadeR.setHeight(GamePlay.HEIGHT);
        fadeR.setFill(Paint.valueOf("#000000"));
        fadeR.setOpacity(0);
        rootContainer.getChildren().add(fadeR);

        gameOverRoot.translateXProperty().set(GamePlay.WIDTH);
        Timeline timelineSlide = new Timeline();
        KeyValue kvSlide = new KeyValue(gameOverRoot.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfSlide = new KeyFrame(Duration.seconds(1), kvSlide);
        timelineSlide.getKeyFrames().add(kfSlide);
        timelineSlide.setOnFinished(t -> rootContainer.getChildren().remove(this.lBRoot));

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

    private void goBackPressedMainPage() {
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        Parent mainPageRoot = null;
        try {
            mainPageRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load main page");
            e.printStackTrace();
        }
        assert (mainPageRoot != null);
        MainPageController mainPageController = loader.getController();
        mainPageController.init(this.app);
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

        mainPageRoot.translateXProperty().set(GamePlay.WIDTH);
        Timeline timelineSlide = new Timeline();
        KeyValue kvSlide = new KeyValue(mainPageRoot.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfSlide = new KeyFrame(Duration.seconds(1), kvSlide);
        timelineSlide.getKeyFrames().add(kfSlide);
        timelineSlide.setOnFinished(t -> rootContainer.getChildren().remove(this.lBRoot));

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
}
