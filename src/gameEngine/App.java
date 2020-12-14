package gameEngine;

import gui.Dialog;
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

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

/*
Divyansh's:
TODO: Beware of file changes and refactoring, especially in file paths while making jar
TODO: Implement Easter egg2 secret restart
TODO: Implement Player Class, Database class
TODO: restart on game over page & on pause implement (maybe see for reference keeping?), pause delay
TODO: add score on game over page, set highscore, highscore line
TODO: throw game over exceptions/ fall down exceptions
TODO: threads while drawing?

TODO: revive using stars!!!!!!, added global currency, has revived ~~~~, need to add net diff to global
*/

/*
Serialization notes:
Score (Player)
 */

public class App extends Application {

    public static MediaPlayer BgMediaPlayer = null; // for easy referencing
    public static int REVIVAL_STARS = 5;
    private static int TOTAL_STARS = 0;
    private static final String pathTotalStars = "src/data/dataTotalStars.ser";
    private static final Boolean startWithAnimation = true;

    private final Database<Game> gameDatabase;
    private final Database<Player> playerDatabase;

    private int highscore;
    private Scene scene;

    public App() { // instance is automatically created by launch of JavaFX
        this.gameDatabase = new Database<>();
        this.playerDatabase = new Database<>();
        this.gameDatabase.form(Game.FILE_PATH);
        this.playerDatabase.form(Player.FILE_PATH);
        this.highscore = 0;
        this.calcHighscore();
        TOTAL_STARS = 0;
        this.readTotalStars();

//        this.eraseDatabaseAndExit(); UNCOMMENT TO ERASE DATABASE
    }

    @SuppressWarnings("unused")
    private void eraseDatabaseAndExit() { // in order to refresh the database
        System.out.println(this.getClass().toString() + " database refreshed");
        this.gameDatabase.reset(Game.FILE_PATH);
        this.playerDatabase.reset(Player.FILE_PATH);
        System.exit(0);
    }

    private void readTotalStars() {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(pathTotalStars));
            TOTAL_STARS = dis.readInt();
            System.out.println(this.getClass().toString() + " read TOTAL_STARS: " + TOTAL_STARS);
            dis.close();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to read total stars");
            System.out.println(e.toString());
        }
    }

    private void saveTotalStars() {
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(pathTotalStars));
            dos.writeInt(TOTAL_STARS);
            System.out.println(this.getClass().toString() + " saved TOTAL_STARS: " + TOTAL_STARS);
            dos.close();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to write total stars");
            System.out.println(e.toString());
        }
    }

    public void addAssets() {
        if (BgMediaPlayer != null && BgMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) { // as others switch media like gameOver, gameplay
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

    public void saveGame(Game game, String name) { // maybe ask for a slot from the player? Implement on the oldest basis
        // modify player here, save data also
        assert (game.getPlayer().getId() == -1 && !game.isGameOver()); // only incomplete & anonymous games can be stored
        game.getPlayer().setName(name);
        game.getPlayer().setId(this.giveId());
        game.getPlayer().setDate(LocalDate.now().toString());
        boolean overwrite = this.gameDatabase.update(game, Game.FILE_PATH);
        if (overwrite) new Dialog("Oldest load slot overwritten! Game saved!", (Stage) this.scene.getWindow());
        else new Dialog("Game saved!", (Stage) this.scene.getWindow());
        System.out.println(this.getClass().toString() + " save success");
    }

    public void overwriteGame(Game game) { // overwrites, if player already exists
        assert (game.getPlayer().getId() != -1 && !game.isGameOver()); // only incomplete games
        game.getPlayer().setDate(LocalDate.now().toString());
        ArrayList<Game> games = this.gameDatabase.getData();
        boolean found = false;
        for (int i = 0; i < games.size(); ++ i) {
            if (games.get(i).getPlayer().getId() == game.getPlayer().getId()) {
                // player found
                found = true;
                games.set(i, game);
                break;
            }
        }
        if (!found) {
            assert (game.getPlayer().getHasRevived());
            this.gameDatabase.update(game, Game.FILE_PATH);
        } else {
            // save into file
            this.gameDatabase.save(Game.FILE_PATH);
        }
        new Dialog("Game overwritten for " + game.getPlayer().getName() + "!", (Stage) this.scene.getWindow());
        System.out.println(this.getClass().toString() + " overwrite game success");
    }

    public void removeGame(Game game) { // after game over remove from database
        assert (game.getPlayer().getId() != -1);
        ArrayList<Game> games = this.gameDatabase.getData();
        int idxRm = -1;
        for (int i = 0; i < games.size(); ++ i) {
            if (games.get(i).getPlayer().getId() == game.getPlayer().getId()) {
                assert (idxRm == -1);
                idxRm = i;
            }
        }
        if (idxRm == -1 && game.getPlayer().getHasRevived()) return; // can have an id if saved, then revived and then didn't save on leaderboard
        assert (idxRm != -1 || game.getPlayer().getHasRevived());
        games.remove(idxRm);
        this.gameDatabase.save(Game.FILE_PATH);
        new Dialog("Game data erased for " + game.getPlayer().getName() + ".", (Stage) this.scene.getWindow());
        System.out.println(this.getClass().toString() + " erase success");
    }

    public void addToLB(Player player, String name) {
        // Name of player can be reset as player won't ever make to load game, game over is true
        player.setName(name);
        player.setDate(LocalDate.now().toString());
        // set ID in case player is unidentified
        if (player.getId() == -1) {
            System.out.println(this.getClass().toString() + " anonymous player registered");
            player.setId(this.giveId());
        }
        this.playerDatabase.update(player, Player.FILE_PATH);
        this.calcHighscore(); // update highscore
        new Dialog("Leaderboard entry added!", (Stage) this.scene.getWindow());
        System.out.println(this.getClass().toString() + " lb entry added");
    }

    private int giveId() { // return a fresh Id for game, max from game & player Database, player might make it to LB and not save game
        // calc. max id and return +1
        int id = -1;
        ArrayList<Game> games = this.gameDatabase.getData();
        for (Game g : games)
            id = Math.max(id, g.getPlayer().getId());
        ArrayList<Player> players = this.playerDatabase.getData();
        for (Player p : players)
            id = Math.max(id, p.getId());
        return id + 1;
    }

    public void calcHighscore() { // calc highscore from all players in LB
        int prevHigh = this.highscore;
        ArrayList<Player> players = this.playerDatabase.getData();
        for (Player p : players)
            this.highscore = Math.max(this.highscore, p.getScore());
        if (prevHigh != this.highscore)
            System.out.println(this.getClass().toString() + " highscore updated");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Scene getScene() {
        return scene;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int _highscore) {
        this.highscore = _highscore;
    }

    public Database<Game> getGameDatabase() {
        return this.gameDatabase;
    }

    public Database<Player> getPlayerDatabase() {
        return this.playerDatabase;
    }

    public int getTotalStars() {
        return TOTAL_STARS;
    }

    public void addTotalStars(int up) { // add only when finished game
        TOTAL_STARS += up;
        this.saveTotalStars();
    }

    public void decTotalStars(int dec) {
        assert (dec <= TOTAL_STARS);
        TOTAL_STARS -= dec;
        System.out.println(this.getClass().toString() + " TOTAL_STARS after dec.: " + TOTAL_STARS);
    }
}
