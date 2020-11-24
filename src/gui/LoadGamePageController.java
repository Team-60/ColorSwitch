package gui;

import gameEngine.App;
import gameEngine.GamePlay;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadGamePageController {

    private App app;
    private ArrayList<Button> slotButtons; // buttons inside buttonContainer
    private ArrayList<String> colorScheme;
    private String inactiveColor;
    private HashMap<Button, Boolean> isActive; // slot is active with a game
    final static int NUM_BUTTONS = 6;

    private AudioClip hoverSound;
    private AudioClip clickSound;

    @FXML
    private AnchorPane loadGameRoot;
    @FXML
    private VBox buttonContainer; // load slots on the main page
    @FXML
    private Group goBack;

    public void init(App _app, ArrayList<Boolean> buttonContent) { // Boolean List but actually will be a Game ArrayList (all active and then all inactive)

        this.app = _app; // needed for return back to main page

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.clickSound.setVolume(0.5); // add click sounds
        this.hoverSound.setVolume(0.04);

        this.colorScheme = new ArrayList<>(); // color blues
        this.colorScheme.add("#003B46");
        this.colorScheme.add("#07575B");
        this.colorScheme.add("#66A5AD");
        this.inactiveColor = "#808588";

        assert buttonContent.size() == NUM_BUTTONS;
        ObservableList<Node> buttonList = buttonContainer.getChildren();
        this.slotButtons = new ArrayList<>();
        this.isActive = new HashMap<>();
        for (int i = 0; i < NUM_BUTTONS; ++ i) {
            Button cur = (Button) buttonList.get(i);
            this.slotButtons.add(cur);
            if (buttonContent.get(i)) { // it's active
                this.activateButton(cur);
                this.isActive.put(cur, true);
            }
            else {
                this.deactivateButton(cur);
                this.isActive.put(cur, false);
            }
        }
    }

    private void deactivateButton(Button button) {
        button.setStyle("-fx-background-color: " + this.inactiveColor +"; ");
        button.setOpacity(0.75);
    }

    private void activateButton(Button button) {
        // set status according to the "game" received
        // set text and stuff here
    }

    @FXML
    public void buttonHoverActive(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        if (isActive.get(button)) {
            this.hoverSound.play();
            int idx = this.slotButtons.indexOf(button);
            String color = this.colorScheme.get(idx % this.colorScheme.size());
            button.setStyle("-fx-background-color: " + color + "; -fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 3; -fx-border-style: solid outside;");
        }
        else {
            Scene scene = button.getScene();
            scene.setCursor(new ImageCursor(new Image(new File("src/assets/loadGamePage/cursorBlocked.png").toURI().toString())));
        }
    }

    @FXML
    public void buttonHoverInactive(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        if (isActive.get(button)) {
            int idx = this.slotButtons.indexOf(button);
            String color = this.colorScheme.get(idx % this.colorScheme.size());
            button.setStyle("-fx-background-color: " + color + ";");
        }
        else {
            Scene scene = button.getScene();
            scene.setCursor(new ImageCursor(new Image(new File("src/assets/loadGamePage/cursor.png").toURI().toString())));
        }
    }

    @FXML
    public void goBackPressed() throws IOException {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        Parent mainPageRoot = loader.load();
        MainPageController mainPageController = loader.getController();
        mainPageController.init(this.app);
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().add(mainPageRoot);

        // temp rectangle for fade purpose
        Rectangle fadeR = new Rectangle();
        fadeR.setWidth(GamePlay.WIDTH);
        fadeR.setHeight(GamePlay.HEIGHT);
        fadeR.setFill(Paint.valueOf("#000000"));
        fadeR.setOpacity(0);
        rootContainer.getChildren().add(fadeR);

        mainPageRoot.translateXProperty().set(-GamePlay.WIDTH);
        Timeline timelineSlide = new Timeline();
        KeyValue kvSlide = new KeyValue(mainPageRoot.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfSlide = new KeyFrame(Duration.seconds(1), kvSlide);
        timelineSlide.getKeyFrames().add(kfSlide);
        timelineSlide.setOnFinished(t -> rootContainer.getChildren().remove(this.loadGameRoot));

        Timeline timelineFadeOut = new Timeline();
        KeyValue kvFadeOut = new KeyValue(fadeR.opacityProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfFadeOut = new KeyFrame(Duration.seconds(0.5), kvFadeOut);
        timelineFadeOut.getKeyFrames().add(kfFadeOut);
        timelineFadeOut.setOnFinished(t -> rootContainer.getChildren().remove(fadeR));

        Timeline timelineFadeIn = new Timeline();
        KeyValue kvFadeIn = new KeyValue(fadeR.opacityProperty(), 0.75, Interpolator.EASE_IN);
        KeyFrame kfFadeIn = new KeyFrame(Duration.seconds(1), kvFadeIn);
        timelineFadeIn.getKeyFrames().add(kfFadeIn);
        timelineFadeIn.setOnFinished(t -> timelineFadeOut.play());

        timelineSlide.play();
        timelineFadeIn.play();
    }

    @FXML
    public void goBackHoverActive() {
        Circle circle = (Circle) this.goBack.getChildren().get(0);
        this.hoverSound.play();
        this.goBack.setScaleX(1.1);
        this.goBack.setScaleY(1.1);
        circle.setFill(Color.web("#FFB52E"));
    }

    @FXML
    public void goBackHoverInactive() {
        Circle circle = (Circle) this.goBack.getChildren().get(0);
        this.goBack.setScaleX(1);
        this.goBack.setScaleY(1);
        circle.setFill(Color.YELLOW);
    }
}
