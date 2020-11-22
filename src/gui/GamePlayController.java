package gui;

import gameEngine.GamePlay;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class GamePlayController {
    private GamePlay gamePlay;
    private Boolean paused;
    private AnchorPane pausePane;
    private PauseOverlayController pauseOverlayController;

    @FXML
    private Button button;
    public void init(GamePlay _gamePlay) throws IOException {
        this.gamePlay = _gamePlay;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PauseOverlay.fxml"));
        this.pausePane = loader.load();
        this.pauseOverlayController = loader.getController();

        this.button.setStyle("-fx-background-radius: 200px; -fx-background-color: #808588");
        this.paused = false;
    }
    @FXML
    public void pauseHoverActive() {
        this.button.setScaleX(1.1);
        this.button.setScaleY(1.1);
        this.button.setStyle("-fx-background-radius: 200px; -fx-background-color: #555555");
    }
    @FXML
    public void pauseHoverInactive() {
        this.button.setScaleX(1);
        this.button.setScaleY(1);
        this.button.setStyle("-fx-background-radius: 200px; -fx-background-color: #808588");
    }
    @FXML
    public void pausePressed() { // most probably have to serialize here to pause (Naah but might use if saved from there), focus shifts to button (IMP)
        AnimationTimer animationTimer = this.gamePlay.getAnimationTimer();
        Scene scene = this.button.getScene();
        StackPane rootContainer = (StackPane) scene.getRoot();

        assert (!this.paused);
        System.out.println(this.getClass().toString() + "Pause pressed");
        this.paused = true;
        GamePlay.PreviousFrameTime = -1;
        animationTimer.stop();
        this.gamePlay.getCanvas().removeEventHandler(KeyEvent.KEY_PRESSED, GamePlay.JumpEventHandler);
        rootContainer.getChildren().add(this.pausePane);
        this.pausePane.requestFocus(); // very very important
        this.pauseOverlayController.init(this);
    }

    public void unpause() { // add event handler again, and get focus
        Scene scene = this.button.getScene();
        StackPane rootContainer = (StackPane) scene.getRoot();
        AnimationTimer animationTimer = this.gamePlay.getAnimationTimer();

        this.paused = false;
        rootContainer.getChildren().remove(this.pausePane);
        this.gamePlay.getCanvas().requestFocus();
        this.gamePlay.getCanvas().addEventHandler(KeyEvent.KEY_PRESSED, GamePlay.JumpEventHandler);
        animationTimer.start();
    }
    public GamePlay getGamePlay() {
        return gamePlay;
    }
    public Boolean getPaused() {
        return this.paused;
    }
}