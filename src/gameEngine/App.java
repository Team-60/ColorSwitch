package gameEngine;

import gui.LoadAnimationController;
import gui.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

/*
Divyansh's:
TODO: Beware of file changes and refactoring, especially in file paths while making jar
TODO: Implement Easter egg2 secret restart
TODO: Implement Player Class, Database class
TODO: add debug options for everywhere with fxml loader
TODO: restart on game over page implement (maybe see for reference keeping?)
TODO: add score on game over page, set highscore
TODO: see when all spots for game over (currently missing fall down, calibrate media player for the same)
TODO: throw game over exceptions/ fall down exceptions
TODO: implement Leaderboard screen comparators
TODO: implement Serializable, ensure every asset is reloaded after deserializing, might need to create init?
*/

/*
Serialization notes:
Score (Player)
 */

public class App extends Application {

    public static MediaPlayer BgMediaPlayer = null; // for easy referencing
    private static final Boolean startWithAnimation = true;

    private int highscore;
    private Scene scene;

    public App() {
        this.highscore = -1; // need to gain this via serializing
    }

    public void addAssets() {
        if (BgMediaPlayer != null && BgMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) { // as others switch media like gameover, gameplay
            BgMediaPlayer.stop();
        }
        Media bgMusic = new Media(new File("src/assets/music/bg/bg4.mp3").toURI().toString());
        BgMediaPlayer = new MediaPlayer(bgMusic);

        BgMediaPlayer.setAutoPlay(true);
        BgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        BgMediaPlayer.play();
    }

    @Override
    public void start(Stage primaryStage) {

        if (startWithAnimation) {
            this.showLoadAnimation(primaryStage);
        }
        else {
            this.addAssets();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainPage.fxml")); // keep in mind, referencing of loaders & objects
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                System.out.println(this.getClass().toString() + " failed to load mainPage");
                e.printStackTrace();
            }
            assert (root != null);
            MainPageController mainPageController = loader.getController();
            mainPageController.init(this);

            StackPane rootContainer = new StackPane(root); // roots of this stack pane will be switched
            rootContainer.setStyle("-fx-background-color :  #0D152C;"); // for pixel based positioning issues
            primaryStage.initStyle(StageStyle.UNDECORATED);
            this.scene = new Scene(rootContainer); // scene's root is the rootContainer (stackPane) whose root is our switching panes
            this.scene.setCursor(new ImageCursor(new Image(new File("src/assets/mainPage/cursor.png").toURI().toString())));
            primaryStage.setScene(this.scene);
            primaryStage.show();
        }
    }

    private void showLoadAnimation(Stage primaryStage) { // for start animation
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoadAnimation.fxml")); // keep in mind, referencing of loaders & objects
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load mainPage");
            e.printStackTrace();
        }
        assert (root != null);
        LoadAnimationController loadAnimationController = loader.getController();

        StackPane rootContainer = new StackPane(root); // roots of this stack pane will be switched
        rootContainer.setStyle("-fx-background-color :  #0D152C;"); // for pixel based positioning issues
        primaryStage.initStyle(StageStyle.UNDECORATED);
        this.scene = new Scene(rootContainer); // scene's root is the rootContainer (stackPane) whose root is our switching panes
        this.scene.setCursor(Cursor.NONE);
        primaryStage.setScene(this.scene);
        primaryStage.show();

        loadAnimationController.init(this);
    }

    public Scene getScene() {
        return scene;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public static void main(String[] args) {
        new App();
        launch(args);
    }

    public void saveGame(Game game, String name) {
        // modify player here, save data also
    }

    private int giveId() {
        return -1;
    }
}
