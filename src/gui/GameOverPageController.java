package gui;

import gameEngine.App;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;


public class GameOverPageController {

    private AudioClip hoverSound;
    private AudioClip clickSound;
    private AudioClip easterEggClick;

    @FXML
    private ImageView logoRingL;
    @FXML
    private ImageView logoRingR;
    @FXML
    private Group iconLB;
    @FXML
    private Group iconRestart;
    @FXML
    private Group iconReturn;

    public void init() {

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.easterEggClick = new AudioClip(new File("src/assets/music/mouse/easterEgg_click_mainpage.wav").toURI().toString());
        this.clickSound.setVolume(0.5); // add click sounds
        this.hoverSound.setVolume(0.04);
        this.easterEggClick.setVolume(0.5);

        RotateTransition rtLogoRingL = new RotateTransition(Duration.millis(15000), logoRingL);
        RotateTransition rtLogoRingR = new RotateTransition(Duration.millis(15000), logoRingR);
        RotateTransition rtIconLB = new RotateTransition(Duration.millis(15000), iconLB);
        RotateTransition rtIconRestart = new RotateTransition(Duration.millis(15000), iconRestart);
        RotateTransition rtIconReturn = new RotateTransition(Duration.millis(15000), iconReturn);
        this.startRotation(rtLogoRingL, 1);
        this.startRotation(rtLogoRingR, 1);
        this.startRotation(rtIconLB, -1);
        this.startRotation(rtIconRestart, -1);
        this.startRotation(rtIconReturn, -1);
    }

    private void startRotation(RotateTransition rt, int dir) {
        rt.setByAngle(dir * 720);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }

    @FXML
    public void iconHoverActive(MouseEvent mouseEvent) {
        this.hoverSound.play();
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(1.1);
        group.setScaleY(1.1);
        circle.setFill(Color.web("#FFB52E"));
    }

    @FXML
    public void iconHoverInactive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(1);
        group.setScaleY(1);
        circle.setFill(Color.YELLOW);
    }

    @FXML
    public void playEasterEggClick() {
        App.BgMediaPlayer.pause();
        this.easterEggClick.play();
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            System.out.println(this.getClass().toString() + " thread sleep failed");
            e.printStackTrace();
        }
        App.BgMediaPlayer.play();
    }
}
