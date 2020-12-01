package gui;

import gameEngine.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

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
    private Label headingLabel;
    @FXML
    private Label bottomLabel;
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

        this.saveButton.setStyle("-fx-background-color: #ffd300, linear-gradient(yellow 50%, #ffd300 100%), radial-gradient(center 50% -40%, radius 200%, yellow 45%, #ffd300 50%); -fx-background-radius : 20");

        this.controller = _controller;
        if (this.controller instanceof PauseOverlayController) {
            this.headingLabel.setText("Save Game");
            this.bottomLabel.setText("Date: " + LocalDate.now().toString()); // TODO: store date for player also
        } else { // see for leaderboard

        }
        this.scoreLabel.setText("" + player.getScore());
        this.distLabel.setText("" + player.getDistance());
        this.jumpsLabel.setText("" + player.getJumps());
    }

    @FXML
    public void saveButtonHoverActive() {
        this.hoverSound.play();
        this.saveButton.setStyle("-fx-background-color: #ffd300, linear-gradient(yellow 50%, #ffd300 100%), radial-gradient(center 50% -40%, radius 200%, yellow 35%, #ffd300 50%); -fx-background-radius : 20");
    }

    @FXML
    public void saveButtonHoverInactive() {
        this.saveButton.setStyle("-fx-background-color: #ffd300, linear-gradient(yellow 50%, #ffd300 100%), radial-gradient(center 50% -40%, radius 200%, yellow 45%, #ffd300 50%); -fx-background-radius : 20");
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
        } else { // see for leaderboard

        }

        Stage stage = (Stage) this.saveButton.getScene().getWindow();
        stage.close();
    }
}
