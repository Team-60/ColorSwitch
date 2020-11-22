package gui;

import gameEngine.Game;
import gameEngine.GamePlay;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class GamePlayController {
    private GamePlay gamePlay;
    @FXML
    private Button button;
    public void init(GamePlay _gamePlay) {
        this.gamePlay = _gamePlay;
        this.button.setStyle("-fx-background-radius: 200px; -fx-background-color: #808588");
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

    }
}
