package gameEngine;

import gui.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
TODO: Add game buttons on pause game screen, find suitable return to main screen icon
TODO: Implement restart on pause and game over
TODO: Implement Easter egg2 secret restart
TODO: Implement Player Class
*/

public class App extends Application {

    public static MediaPlayer BgMediaPlayer; // for easy referencing
    private Scene scene;

    private void addAssets() {
        Media bgMusic = new Media(new File("src/assets/music/bg1.mp3").toURI().toString());
        BgMediaPlayer = new MediaPlayer(bgMusic);

        BgMediaPlayer.setAutoPlay(true);
        BgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        BgMediaPlayer.play();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.addAssets();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainPage.fxml")); // keep in mind, referencing of loaders & objects
        Parent root = loader.load();
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

    public Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        new App();
        launch(args);
    }
}
