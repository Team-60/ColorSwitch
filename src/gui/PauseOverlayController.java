package gui;

import gameEngine.App;
import gameEngine.GamePlay;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;


public class PauseOverlayController { // TODO maybe for restart here?

    private App app;

    private GamePlayController gamePlayController;
    private AudioClip hoverSound;
    private AudioClip clickSound;

    private String usernameSave; // will be used to check if any username received from input popup

    @FXML
    private AnchorPane pauseOverlayRoot;
    @FXML
    private Group unpauseGroup;

    public void init(GamePlayController _gamePlayController, App _app) {

        this.usernameSave = null;

        System.out.println(this.getClass().toString() + " instantiated");
        this.app = _app;
        this.gamePlayController = _gamePlayController;

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.clickSound.setVolume(0.5);
        this.hoverSound.setVolume(0.04);

        Timeline animationUnpause = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(unpauseGroup.scaleXProperty(), unpauseGroup.getScaleX(), Interpolator.EASE_IN), new KeyValue(unpauseGroup.scaleYProperty(), unpauseGroup.getScaleY(), Interpolator.EASE_IN)),
                new KeyFrame(Duration.seconds(0.6), new KeyValue(unpauseGroup.scaleXProperty(), unpauseGroup.getScaleX() + 0.1, Interpolator.EASE_IN), new KeyValue(unpauseGroup.scaleYProperty(), unpauseGroup.getScaleY() + 0.1, Interpolator.EASE_IN))
        );
        animationUnpause.setAutoReverse(true);
        animationUnpause.setCycleCount(Animation.INDEFINITE);
        animationUnpause.play();
    }

    public void setUsernameSave(String name) {
        assert (name != null);
        this.usernameSave = name;
    }

    @FXML
    public void unpauseHoverActive() {
        this.hoverSound.play();
        Circle circle = (Circle) this.unpauseGroup.getChildren().get(0);
        circle.setFill(Paint.valueOf("#07575B"));
    }
    @FXML
    public void unpauseHoverInactive() {
        Circle circle = (Circle) this.unpauseGroup.getChildren().get(0);
        circle.setFill(Paint.valueOf("#66A5AD"));
    }
    @FXML
    public void unpausePressed() {
        System.out.println(this.getClass().toString() + " Unpause pressed");
        this.clickSound.play();
        assert (this.gamePlayController.getPaused());
        this.gamePlayController.unpause();
    }
    @FXML
    public void iconHoverActive(MouseEvent mouseEvent) {
        this.hoverSound.play();
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(0.9);
        group.setScaleY(0.9);
        circle.setFill(Color.web("#fa6602"));
    }
    @FXML
    public void iconHoverInactive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        Circle circle = (Circle) group.getChildren().get(0);
        group.setScaleX(0.8); // already scaled down in fxml
        group.setScaleY(0.8);
        circle.setFill(Color.web("#de7a22"));
    }
    @FXML
    public void returnIconClicked() {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        Parent mainPageRoot = null;
        try {
            mainPageRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load mainPageRoot");
            e.printStackTrace();
        }
        assert (mainPageRoot != null);
        MainPageController mainPageController = loader.getController();
        mainPageController.init(this.app);
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 2); // as I have pause overlay along with game play
        rootContainer.getChildren().add(mainPageRoot);
        mainPageRoot.requestFocus(); // IMP as pause overlay has focus right now

        // fade down current music
        Timeline timelineMusicFadeOut = new Timeline();
        KeyValue kvMusicFadeOut = new KeyValue(App.BgMediaPlayer.volumeProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfMusicFadeOut = new KeyFrame(Duration.seconds(1), kvMusicFadeOut);
        timelineMusicFadeOut.getKeyFrames().add(kfMusicFadeOut);
        timelineMusicFadeOut.setOnFinished(t -> this.app.addAssets()); // for refreshing bg music

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
        timelineSlide.setOnFinished((t) -> {
            rootContainer.getChildren().remove(this.pauseOverlayRoot);
            rootContainer.getChildren().remove(this.gamePlayController.getGamePlayRoot()); // as pause is an overlay and this will be active gameplay alongside
        });

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
        timelineMusicFadeOut.play();
    }

    @FXML
    public void saveIconClicked() {
        // ask for username
        this.clickSound.play();

        // check if the player has already saved
        if (this.gamePlayController.getGamePlay().getGame().getPlayer().getId() == -1) {
            this.newSave(); // ask for username
        } else {
            this.app.overwriteGame(this.gamePlayController.getGamePlay().getGame());
        }
    }

    private void newSave() {
        Scene scene = this.app.getScene();
        StackPane rootContainer = (StackPane) scene.getRoot();
        // temp rectangle for blocking input
        Rectangle tempR = new Rectangle();
        tempR.setWidth(GamePlay.WIDTH);
        tempR.setHeight(GamePlay.HEIGHT);
        tempR.setFill(Paint.valueOf("#000000"));
        tempR.setOpacity(0.75);
        rootContainer.getChildren().add(tempR);
        App.BgMediaPlayer.setVolume(0.05);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("InputPopup.fxml"));
        Parent inputRoot = null;
        try {
            inputRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load input root");
            e.printStackTrace();
        }
        assert (inputRoot != null);
        inputRoot.requestFocus(); // IMP

        GamePlay gamePlay = this.gamePlayController.getGamePlay();
        InputPopupController<PauseOverlayController> inputPopupController = loader.getController();
        inputPopupController.init(gamePlay.getPlayer(), this);

        Scene secondaryScene = new Scene(inputRoot);
        secondaryScene.setCursor(new ImageCursor(new Image(new File("src/assets/inputPopup/cursor.png").toURI().toString())));
        Stage secondaryStage = new Stage(StageStyle.UNDECORATED);
        Stage primaryStage = (Stage) this.app.getScene().getWindow();
        secondaryStage.initOwner(primaryStage); // imp. for them to act as one stage
        secondaryStage.setScene(secondaryScene);
        secondaryStage.showAndWait();

        assert (inputPopupController.getSaveSuccess() == (this.usernameSave != null));
        App.BgMediaPlayer.setVolume(1);
        rootContainer.getChildren().remove(tempR); // regain focus
        this.pauseOverlayRoot.requestFocus();

        if (inputPopupController.getSaveSuccess()) {
            System.out.println(this.getClass().toString() + " " + this.usernameSave + " received");
            this.app.saveGame(this.gamePlayController.getGamePlay().getGame(), this.usernameSave);
        } else {
            System.out.println(this.getClass().toString() + " save cancelled");
        }
    }
}
