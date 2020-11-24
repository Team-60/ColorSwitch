package gui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LBPageController { // IMP to remember to which page do we have to return (game over or main page)

    @FXML
    private ImageView crownImg;
    @FXML
    private Group goBackGroup;

    public void init() {

        Glow glow = new Glow(0.5);
        crownImg.setEffect(glow);

        RotateTransition rt = new RotateTransition(Duration.millis(15000), goBackGroup);
        rt.setByAngle(720);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

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

    }
}
