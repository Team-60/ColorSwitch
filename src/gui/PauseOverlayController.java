package gui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;


public class PauseOverlayController {
    private GamePlayController gamePlayController;
    private AudioClip hoverSound;
    private AudioClip clickSound;

    @FXML
    private Group unpauseGroup;
    @FXML
    private Group returnGroup;
    @FXML
    private Group saveGroup;

    public void init(GamePlayController _gamePlayController) {
        System.out.println(this.getClass().toString() + " instantiated");
        this.gamePlayController = _gamePlayController;

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.clickSound.setVolume(0.5); // TODO: add click sounds
        this.hoverSound.setVolume(0.04);

        Timeline animationUnpause = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(unpauseGroup.scaleXProperty(), unpauseGroup.getScaleX(), Interpolator.EASE_IN), new KeyValue(unpauseGroup.scaleYProperty(), unpauseGroup.getScaleY(), Interpolator.EASE_IN)),
                new KeyFrame(Duration.seconds(0.6), new KeyValue(unpauseGroup.scaleXProperty(), unpauseGroup.getScaleX() + 0.1, Interpolator.EASE_IN), new KeyValue(unpauseGroup.scaleYProperty(), unpauseGroup.getScaleY() + 0.1, Interpolator.EASE_IN))
        );
        animationUnpause.setAutoReverse(true);
        animationUnpause.setCycleCount(Animation.INDEFINITE);
        animationUnpause.play();
    }
    @FXML
    public void unpauseHoverActive() {
        Circle circle = (Circle) this.unpauseGroup.getChildren().get(0);
        circle.setFill(Paint.valueOf("#07575B"));
    }
    @FXML
    public void unpauseHoverInactive() {
        Circle circle = (Circle) this.unpauseGroup.getChildren().get(0);
        circle.setFill(Paint.valueOf("#66A5AD"));
    }
    @FXML
    public void unpausePressed() {
        System.out.println(this.getClass().toString() + " Unpause pressed");
        assert (this.gamePlayController.getPaused());
        this.gamePlayController.unpause();
    }
    @FXML
    public void iconHoverActive(MouseEvent mouseEvent) {
        this.hoverSound.play();
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(0.9);
        group.setScaleY(0.9);
        circle.setFill(Color.web("#fa6602"));
    }
    @FXML
    public void iconHoverInactive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(0.8); // already scaled down in fxml
        group.setScaleY(0.8);
        circle.setFill(Color.web("#de7a22"));
    }
}
