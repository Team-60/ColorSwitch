package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class PauseOverlayController {
    private GamePlayController gamePlayController;

    @FXML
    private Button unpauseButton;
    public void init(GamePlayController _gamePlayController) {
        System.out.println(this.getClass().toString() + " instantiated");
        this.gamePlayController = _gamePlayController;
        this.unpauseButton.setStyle("-fx-background-radius: 200px; -fx-background-color: #66A5AD");
    }
    @FXML
    public void unpausePressed() {
        System.out.println(this.getClass().toString() + " Unpause pressed");
        assert (this.gamePlayController.getPaused());
        this.gamePlayController.unpause();
    }
}
