package gui;

import gameEngine.App;
import gameEngine.GamePlay;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static gameEngine.App.BgMediaPlayer;

public class LoadAnimationController {

    private App app;
    private AudioClip glowClip;
    private AudioClip jumpClip;

    @FXML
    private AnchorPane loadAnimationRoot;
    @FXML
    private ImageView ringIn;
    @FXML
    private ImageView ringOut;
    @FXML
    private ImageView logoRingL;
    @FXML
    private ImageView logoRingR;
    @FXML
    private ImageView logo;
    @FXML
    private Label labelU;
    @FXML
    private Label labelD;
    @FXML
    private Circle ballAnim;
    @FXML
    private Circle shadow1;
    @FXML
    private Circle shadow2;

    private static final int ballEnd = 352;
    private static final double durationRt = 1800;

    public void init(App _app) { // create and destroy here itself

        this.app = _app;

        // load bg music for App, not calling add assets
        Media bgMusic = new Media(new File("src/assets/music/bg/bg4.mp3").toURI().toString());
        App.BgMediaPlayer = new MediaPlayer(bgMusic);
        App.BgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        this.glowClip = new AudioClip(new File("src/assets/music/glow.mp3").toURI().toString());
        this.jumpClip = new AudioClip(new File("src/assets/music/effectsColorSwitch/jump.wav").toURI().toString());

        try { // wait for files to load, need them for initial timeline
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            System.out.println(this.getClass().toString() + " waiting for file load failed");
        }

        Timeline animBall2 = new Timeline(
                new KeyFrame(Duration.ZERO, e -> this.jumpClip.play()),
                new KeyFrame(Duration.ZERO, new KeyValue(ballAnim.layoutYProperty(), ballAnim.getLayoutY(), Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(250), new KeyValue(ballAnim.layoutYProperty(), ballEnd, Interpolator.EASE_OUT))
        );
        animBall2.setOnFinished(t -> {
            this.shadow1.setVisible(true);
            this.shadow2.setVisible(true);
        });
        animBall2.setCycleCount(1);

        final Integer[] count = {0};
        Timeline animBall1 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(ballAnim.layoutYProperty(), ballAnim.getLayoutY(), Interpolator.LINEAR)),
                new KeyFrame(Duration.ZERO, e -> {
                    if (count[0] % 2 == 0)
                        this.jumpClip.play();
                    count[0] ++;
                }),
                new KeyFrame(Duration.millis(300), new KeyValue(ballAnim.layoutYProperty(), ballAnim.getLayoutY() - 100, Interpolator.EASE_OUT))
        );
        animBall1.setOnFinished(t -> animBall2.play());
        animBall1.setCycleCount(4); // 2 jumps needed
        animBall1.setAutoReverse(true);

        RotateTransition rtLogoL = new RotateTransition(Duration.millis(durationRt), logoRingL);
        rtLogoL.setByAngle(360);
        rtLogoL.setCycleCount(1);
        rtLogoL.setInterpolator(Interpolator.EASE_BOTH);

        RotateTransition rtLogoR = new RotateTransition(Duration.millis(durationRt), logoRingR);
        rtLogoR.setByAngle(360);
        rtLogoR.setCycleCount(1);
        rtLogoR.setInterpolator(Interpolator.EASE_BOTH);

        RotateTransition rtIn = new RotateTransition(Duration.millis(durationRt), ringIn);
        rtIn.setByAngle(360);
        rtIn.setCycleCount(1);
        rtIn.setInterpolator(Interpolator.EASE_BOTH);

        RotateTransition rtOut = new RotateTransition(Duration.millis(durationRt), ringOut);
        rtOut.setByAngle(-360);
        rtOut.setCycleCount(1);
        rtOut.setInterpolator(Interpolator.EASE_BOTH);
        rtOut.setOnFinished(t1 -> {
            assert (rtLogoL.getStatus() == Animation.Status.STOPPED);
            assert (rtLogoR.getStatus() == Animation.Status.STOPPED);
            assert (rtIn.getStatus() == Animation.Status.STOPPED);

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0.1);
            this.ringIn.setEffect(colorAdjust);
            this.ringOut.setEffect(colorAdjust);
            this.logoRingL.setEffect(colorAdjust);
            this.logoRingR.setEffect(colorAdjust);
            this.logo.setEffect(colorAdjust);
            this.labelD.setEffect(colorAdjust);
            this.labelU.setEffect(colorAdjust);
            this.ballAnim.setEffect(colorAdjust);

            AtomicBoolean glowClipPlayed = new AtomicBoolean(false); // to be played only once
            Timeline glowAnim = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.getBrightness(), Interpolator.LINEAR)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.getBrightness() + 0.6, Interpolator.LINEAR), new KeyValue(this.ballAnim.radiusProperty(), this.ballAnim.getRadius() + 2, Interpolator.EASE_IN)),
                    new KeyFrame(Duration.ZERO, e -> { // for auto reverse cases
                        if (!glowClipPlayed.get()) {
                            this.glowClip.play();
                            glowClipPlayed.set(true);
                        }
                    })
            );
            glowAnim.setOnFinished(t2 -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainPage.fxml")); // keep in mind, referencing of loaders & objects
                Parent mainPageRoot = null;
                try {
                    mainPageRoot = loader.load();
                } catch (IOException e) {
                    System.out.println(this.getClass().toString() + " failed to load mainPage");
                    e.printStackTrace();
                }
                assert (mainPageRoot != null);
                MainPageController mainPageController = loader.getController();
                mainPageController.initData(this.app); // first init data, else if done together will cause Exception

                Scene scene = this.loadAnimationRoot.getScene();
                StackPane rootContainer = (StackPane) scene.getRoot();
                assert (rootContainer.getChildren().size() == 1);
                mainPageRoot.setOpacity(0.0);
                rootContainer.getChildren().add(mainPageRoot);
                
                // to remove focus from mainPageRoot 
                Rectangle tempR = new Rectangle();
                tempR.setWidth(GamePlay.WIDTH);
                tempR.setHeight(GamePlay.HEIGHT);
                tempR.setFill(Paint.valueOf("#000000"));
                tempR.setOpacity(0);
                rootContainer.getChildren().add(tempR);

                Timeline showMainRootAnim = new Timeline(
                        new KeyFrame(Duration.seconds(1), new KeyValue(mainPageRoot.opacityProperty(), 1, Interpolator.EASE_IN))
                );
                showMainRootAnim.setOnFinished(t3 -> {
                            rootContainer.getChildren().remove(this.loadAnimationRoot);
                            rootContainer.getChildren().remove(tempR);
                            App.BgMediaPlayer.play(); // as initially it was just loaded
                            mainPageController.initAnim(); // want animation after glow
                            scene.setCursor(new ImageCursor(new Image(new File("src/assets/mainPage/cursor.png").toURI().toString()))); // show cursor
                        }
                );
                showMainRootAnim.play();
            });

            glowAnim.setCycleCount(2); // apparently reverse needs a cycle count
            glowAnim.setAutoReverse(true);
            glowAnim.play();
        });

        animBall1.play();
        rtLogoL.play();
        rtLogoR.play();
        rtIn.play();
        rtOut.play();
    }
}
