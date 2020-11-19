package GameEngine;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Game {

    Ball ball;
    ArrayList<GameElement> gameElements;
    GraphicsContext graphicsContext;
    Game(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        ball = new Ball(graphicsContext);
        gameElements = new ArrayList<>();
    }

    private void moveScreenRelative(double offset) {
        for (GameElement gameElement : gameElements) {
            gameElement.applyOffset(offset);
        }
    }

    public void checkAndUpdate(double time) {
        double offset = ball.move(time);
        moveScreenRelative(offset);
        double y = 350;
        double x = 225;

        // remove unwanted objects
        ArrayList<GameElement> gameElementsTemp = new ArrayList<>();
        for (GameElement gameElement : gameElements) {
            if (gameElement.getY() < 1000) {
                gameElementsTemp.add(gameElement);
            }
        }
        gameElements = gameElementsTemp;

        boolean touched = true;
        GameElement prev = null;

        if (gameElements.size() > 0) {
            prev = gameElements.get(gameElements.size() - 1);
            touched = false;
        }

        while(gameElements.size() < 8) {
            if (touched) {
                touched = false;
            }else {
                y = prev.getY() - prev.getClosestSafeDist();
            }
            GameElement obsCircle = new ObsCircle(x, y, 350, 90, 15);
            gameElements.add(obsCircle);
            prev = obsCircle;
        }
    }

    public void registerJump() {
        ball.jump();
    }

    public void refreshGameElements() {

        for (GameElement gameElement : gameElements) {
            gameElement.refresh(graphicsContext);
        }
        ball.refresh();
    }

}
