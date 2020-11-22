package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class PauseOverlayController {
    @FXML
    private Button unpauseButton;
    public void init() {
        System.out.println(this.getClass().toString() + " instantiated");
    }
    @FXML
    public void unpausePressed() {
        System.out.println(this.getClass().toString() + " Unpause pressed");
    }
}
