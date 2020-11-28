package gui;

import gameEngine.App;
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
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadAnimationController {

    private App app;
    private AudioClip glowClip;

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

    private static final double durationRt = 2000;

    public void init(App _app) { // create and destroy here itself

        this.app = _app;
        this.app.addAssets(); // how does it affect glowClip?

        this.glowClip = new AudioClip(new File("src/assets/music/glow.mp3").toURI().toString());

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

//            Glow glow = new Glow(0.0); FOR GLOW
//            this.ringIn.setEffect(glow);
//            this.ringOut.setEffect(glow);

            AtomicBoolean glowClipPlayed = new AtomicBoolean(false); // memory inconsistency, to be played only once
            Timeline glowAnim = new Timeline(
//                    new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), glow.getLevel(), Interpolator.EASE_IN)), FOR GLOW
//                    new KeyFrame(Duration.seconds(1), new KeyValue(glow.levelProperty(), glow.getLevel() + 0.75, Interpolator.EASE_IN))
                    new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.getBrightness(), Interpolator.LINEAR)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.getBrightness() + 0.6, Interpolator.LINEAR)),
                    new KeyFrame(Duration.ZERO, e -> {
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

                Timeline showMainRootAnim = new Timeline(
                        new KeyFrame(Duration.seconds(1), new KeyValue(mainPageRoot.opacityProperty(), 1, Interpolator.EASE_IN))
                );
                showMainRootAnim.setOnFinished(t3 -> {
                            rootContainer.getChildren().remove(this.loadAnimationRoot);
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

        rtLogoL.play();
        rtLogoR.play();
        rtIn.play();
        rtOut.play();
    }
}
