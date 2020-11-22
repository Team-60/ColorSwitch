package gui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class PauseOverlayController {
    private GamePlayController gamePlayController;
    @FXML
    private Group unpauseGroup;

    public void init(GamePlayController _gamePlayController) {
        System.out.println(this.getClass().toString() + " instantiated");
        this.gamePlayController = _gamePlayController;

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
}
