package gui;
import com.sun.javafx.css.StyleCache;
import gameEngine.Game;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GamePlayController {
    private Game game;
    public void init(Game _game) {
        this.game = _game;
    }
    @FXML
    public void pausePressed(MouseEvent mouseEvent) { // most probably have to serialize here to pause
        System.out.println("hey mouse pressed");
    }
}
