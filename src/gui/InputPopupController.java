package gui;

import gameEngine.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;

public class InputPopupController<T> {

    private T controller;
    private Bloom bloom;
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
        this.scoreLabel.setText("Stars : " + player.getScore());
        this.distLabel.setText("Distance : " + player.getDistance() + "m");
        this.jumpsLabel.setText("Jumps : " + player.getJumps());
        this.bloom = new Bloom(1.0);
        this.saveButton.setEffect(this.bloom);
    }

    @FXML
    public void saveButtonHoverActive() {
        this.hoverSound.play();
        this.bloom.setThreshold(0.75);
    }

    @FXML
    public void saveButtonHoverInactive() {
        this.bloom.setThreshold(1);
    }

    @FXML
    public void buttonPressed() {
        String name = this.inputField.getText();
        if (name == null || name.length() == 0) {
            this.errorSound.play();
            this.inputField.setStyle("-fx-border-width: 2; -fx-border-color: yellow;");
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
