package gui;

import gameEngine.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;

public class InputPopupController<T> {

    private T controller;
    private AudioClip hoverSound;
    private AudioClip clickSound;
    private AudioClip errorSound;

    @FXML
    private Button saveButton;
    @FXML
    private TextField inputField;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label distLabel;
    @FXML
    private Label jumpsLabel;


    public void init(Player player, T _controller) {

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.errorSound = new AudioClip(new File("src/assets/music/mouse/error.wav").toURI().toString());
        this.clickSound.setVolume(0.5); // add click sounds
        this.errorSound.setVolume(0.4);
        this.hoverSound.setVolume(0.2);

        this.controller = _controller;
        this.scoreLabel.setText(""+player.getScore());
        this.distLabel.setText(player.getDistance() + "px");
        this.jumpsLabel.setText("" + player.getJumps());
    }

    @FXML
    public void saveButtonHoverActive() {
        this.hoverSound.play();
        this.saveButton.setStyle("-fx-background-color: #daba0a;");
        this.saveButton.setScaleX(1.1);
        this.saveButton.setScaleY(1.1);
    }

    @FXML
    public void saveButtonHoverInactive() {
        this.saveButton.setStyle("-fx-background-color: #ffd300;");
        this.saveButton.setScaleX(1);
        this.saveButton.setScaleY(1);
    }

    @FXML
    public void buttonPressed() {
        String name = this.inputField.getText();
        if (name == null || name.length() == 0) {
            this.errorSound.play();
            this.inputField.setStyle("-fx-border-width: 2; -fx-border-color: #ffd300;");
            System.out.println(this.getClass().toString() + " blank input registered");
            return;
        }

        this.clickSound.play();
        System.out.println(this.getClass().toString() + " " + name + " registered");

        if (this.controller instanceof PauseOverlayController) {
            ((PauseOverlayController) this.controller).setUsernameSave(name);
        }

        Stage stage = (Stage) this.saveButton.getScene().getWindow();
        stage.close();
    }
}
