package gui;

import gameEngine.GamePlay;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

// for popups like confirmations and stuff
public class Dialog {

    public Dialog(String msg, Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"));
        Parent dialogRoot = null;
        try {
            dialogRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load dialog root");
            e.printStackTrace();
        }
        assert (dialogRoot != null);

        Label msgLabel = (Label) dialogRoot.getChildrenUnmodifiable().get(0);
        msgLabel.setText(msg);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(dialogRoot);
        scene.setFill(Color.TRANSPARENT);
        Stage secondaryStage = new Stage(StageStyle.TRANSPARENT);
        secondaryStage.initOwner(primaryStage);
        secondaryStage.setScene(scene);
        secondaryStage.show();
        secondaryStage.setX((primScreenBounds.getWidth() - secondaryStage.getWidth()) / 2);
        secondaryStage.setOpacity(0);
        secondaryStage.setY(GamePlay.HEIGHT + secondaryStage.getHeight() / 3.5);
        Timeline showAnim = new Timeline(
            new KeyFrame(Duration.millis(250), new KeyValue(secondaryStage.opacityProperty(), secondaryStage.getOpacity(), Interpolator.LINEAR)),
            new KeyFrame(Duration.millis(1000), new KeyValue(secondaryStage.opacityProperty(), 1, Interpolator.EASE_BOTH))
        );
        showAnim.setOnFinished(t -> secondaryStage.close());
        showAnim.setCycleCount(2);
        showAnim.setAutoReverse(true);
        showAnim.play();
    }
}
