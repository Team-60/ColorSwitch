package gui;

import gameEngine.Game;
import gameEngine.GamePlay;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class GamePlayController {
    private GamePlay gamePlay;
    private Boolean paused;
    @FXML
    private Button button;
    public void init(GamePlay _gamePlay) {
        this.gamePlay = _gamePlay;
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
    public void pausePressed() { // most probably have to serialize here to pause, focus shifts to button (IMP)
        System.out.println("Pause Pressed Gameplay");
        AnimationTimer animationTimer = this.gamePlay.getAnimationTimer();
        if (this.paused) {
            System.out.println("paused again");
            this.paused = false;
            this.gamePlay.getCanvas().requestFocus();
            animationTimer.start();
        }
        else {
            System.out.println("play again");
            this.paused = true;
            GamePlay.PreviousFrameTime = -1;
            animationTimer.stop();
        }
    }
}
