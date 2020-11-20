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

        double x, y;
        x = 225;
        y = 350;
        gameElements = new ArrayList<>();
        Obstacle obstacle = new ObsCircle(225, 350, 350, 90, 15);
        GameElement star = new Star(x, y);
        GameElement switchColor = new SwitchColor(x, y - obstacle.getClosestSafeDist()/2);
        gameElements.add(obstacle);
        gameElements.add(star);
        gameElements.add(switchColor);
        ball.setColor(obstacle.getRandomColor());
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
            if (!gameElement.checkCollision(ball) && gameElement.getY() < 1000) {
                gameElementsTemp.add(gameElement);
            }
        }
        gameElements = gameElementsTemp;

        boolean touched = true;
        GameElement prev = null;
        if (gameElements.size() > 0) {
            // find the last obstacle type and assign prev to it
            for (int i = gameElements.size() - 1; i >= 0; --i) {
                if (gameElements.get(i) instanceof Obstacle) {
                    prev = gameElements.get(i);
                    touched = false;
                    break;
                }
            }
        }

        while(gameElements.size() < 16) {
            if (touched) {
                touched = false;
            }else {
                y = prev.getY() - prev.getClosestSafeDist();
            }
            GameElement obsCircle = new ObsCircle(x, y, 350, 90, 15);
            GameElement star = new Star(x, y);
            GameElement switchColor = new SwitchColor(x, y - obsCircle.getClosestSafeDist()/2);
            gameElements.add(obsCircle);
            gameElements.add(star);
            gameElements.add(switchColor);
            prev = obsCircle;
        }
        // give each colorSwitch an Obstacle
        for (GameElement gameElement : gameElements) {
            if (gameElement instanceof SwitchColor) {
                prev = gameElement;
            }else if (gameElement instanceof Obstacle && prev instanceof SwitchColor) {
                ((SwitchColor) prev).setObstacle((Obstacle)gameElement);
            }
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
