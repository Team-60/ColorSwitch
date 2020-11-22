package gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadGamePageController {

    private ArrayList<Button> slotButtons; // buttons inside buttonContainer
    private ArrayList<String> colorScheme;
    private String inactiveColor;
    private HashMap<Button, Boolean> isActive; // slot is active with a game
    final static int numButtons = 6;

    @FXML
    private VBox buttonContainer; // load slots on the main page
    @FXML
    private Group goBack;

    public void init(ArrayList<Boolean> buttonContent) { // Boolean List but actually will be a Game ArrayList (all active and then all inactive)

        this.colorScheme = new ArrayList<>(); // color blues
        this.colorScheme.add("#003B46");
        this.colorScheme.add("#07575B");
        this.colorScheme.add("#66A5AD");
        this.inactiveColor = "#808588";

        assert buttonContent.size() == numButtons;
        ObservableList<Node> buttonList = buttonContainer.getChildren();
        this.slotButtons = new ArrayList<>();
        this.isActive = new HashMap<>();
        for (int i = 0; i < numButtons; ++ i) {
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
            int idx = this.slotButtons.indexOf(button);
            String color = this.colorScheme.get(idx % this.colorScheme.size());
            button.setStyle("-fx-background-color: " + color + "; -fx-border-color: white; -fx-border-width: 2 2 2 2; -fx-border-radius: 0;");
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
}
