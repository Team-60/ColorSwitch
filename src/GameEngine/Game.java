package GameEngine;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Game {

    private Ball ball;
    private boolean gameOver = false;
    private int score;
    private ArrayList<GameElement> gameElements;
    private GraphicsContext graphicsContext;
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
        if (gameOver) return;
        double offset = ball.move(time);
        moveScreenRelative(offset);
        double y = 350;
        double x = 225;

        // remove unwanted objects
        ArrayList<GameElement> gameElementsTemp = new ArrayList<>();
        for (GameElement gameElement : gameElements) {
            if (gameElement.checkCollision(ball)) {
                if (gameElement instanceof Star) score++;
                if (gameElement instanceof Obstacle) gameOver = false;
                continue;
            }
            if (gameElement.getY() < 1000) {
                if (gameElement instanceof Obstacle) {
                    ((Obstacle)gameElement).updateRotationAngle(time);
                }
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
            GameElement obsCircle = new ObsDoubleCircle(x, y, 380 , 115, 90, 15);
//            GameElement obsCircle = new ObsCircle(x, y, 350, 90, 15);
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

        // displayScore
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);
        graphicsContext.setFont(new Font("Monospaced", 60));
        graphicsContext.fillText(Integer.toString(score), 50, 60);

    }

    public void registerJump() {
        ball.jump();
    }

    public void refreshGameElements() {

        if (gameOver) {
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.setTextBaseline(VPos.CENTER);
            graphicsContext.setFont(new Font("Monospaced", 60));
            graphicsContext.fillText("Game Over", 225, 350);
            graphicsContext.fillText("Game Over", 225, 350);
            return;
        }
        for (GameElement gameElement : gameElements) {
            gameElement.refresh(graphicsContext);
        }
        ball.refresh();
    }



}
