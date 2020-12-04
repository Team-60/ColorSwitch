package gameEngine;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable {

    public static final String FILE_PATH = "src/data/dataGame.ser";
    private static final int numberOfObstacle = 5;
    private static final double distanceBetweenObstacles = 150;

    private final Player player;
    private final Ball ball;
    private ArrayList<GameElement> gameElements;
    private boolean gameOver = false;

    private final transient GraphicsContext graphicsContext; // can't serialize this

    Game(GraphicsContext graphicsContext, Player player) {
        this.graphicsContext = graphicsContext;
        this.player = player;

        ball = new Ball(graphicsContext);

        double x = 225;
        double y = 350;
        gameElements = new ArrayList<>();
        Obstacle obstacle = new ObsCircle(225, 350, 90, 15);
        GameElement star = new Star(x, y);
        GameElement switchColor = new SwitchColor(x, y - obstacle.getClosestSafeDist() - distanceBetweenObstacles/2);
        gameElements.add(obstacle);
        gameElements.add(star);
        gameElements.add(switchColor);
        ball.setColor(obstacle.getRandomColor());
        Renderer.init(graphicsContext);
    }

    private void moveScreenRelative(double offset) {
        for (GameElement gameElement : gameElements) {
            gameElement.applyOffset(offset);
        }
    }

    public void checkAndUpdate(double time) {

        double offset = ball.move(time);
        if (ball.getY() - ball.getRadius() > 700) { // check if game over due to fall down, throw exception, ball shouldn't be visible at all
            gameOver = true;
        }
        moveScreenRelative(offset);
        double y = 350;
        double x = 225;

        // remove unwanted objects
        ArrayList<GameElement> gameElementsTemp = new ArrayList<>();
        for (GameElement gameElement : gameElements) {
            // TODO : this can give null pointer error is all the elements are not sorted acc to Y coordinates
            if (gameElement.checkCollision(ball)) {
                gameElement.playSound();
                if (gameElement instanceof Star) player.incScore();
                else if (gameElement instanceof Obstacle) gameOver = true;
//                else if (gameElement instanceof Obstacle) {
//                    try {
//                        Thread.sleep(500);
//                    }catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
                continue;
            }
            if (gameElement.getY() < 1000) {
                if (gameElement instanceof Obstacle) {
                    ((Obstacle)gameElement).update(time);
                }
                gameElementsTemp.add(gameElement);
            }
        }
        gameElements = gameElementsTemp;

        if (gameElements.size() > 0) {
            // find the last obstacle type and assign prev to it
            for (int i = gameElements.size() - 1; i >= 0; --i) {
                if (gameElements.get(i) instanceof Obstacle) {
                    y = gameElements.get(i).getY() - gameElements.get(i).getClosestSafeDist();
                    y -= distanceBetweenObstacles;
                    break;
                }
            }
        }

        while(gameElements.size() < 16) {
            Obstacle obstacle = getRandomObstacle(x, y);
            y -= obstacle.getClosestSafeDist();

            y -= obstacle.getClosestStar();
            GameElement star = new Star(x, y);                  // TODO : getStar() gives star location based on type of obstacle
            y += obstacle.getClosestStar();

            y -= obstacle.getClosestSafeDist();
            GameElement switchColor = new SwitchColor(x, y - distanceBetweenObstacles/2);

            gameElements.add(obstacle);
            gameElements.add(star);
            gameElements.add(switchColor);
            y -= distanceBetweenObstacles;
        }

        GameElement prev = null;
        // give each colorSwitch an Obstacle
        for (GameElement gameElement : gameElements) {
            if (gameElement instanceof SwitchColor) {
                prev = gameElement;
            }else if (gameElement instanceof Obstacle && prev != null) {
                ((SwitchColor) prev).setObstacle((Obstacle)gameElement);
            }
        }

        // displayScore
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setTextBaseline(VPos.CENTER);
        graphicsContext.setFont(new Font("Monospaced", 60));
        graphicsContext.fillText(Integer.toString(this.player.getScore()), 50, 60);
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

    public Obstacle getRandomObstacle(double x, double y) {

        int randomNumber = (new Random()).nextInt(numberOfObstacle);
        // y - safe dist of that specific obstacles
//        int randomNumber = 5;
        if (randomNumber == 0) {
            return (new ObsCircle(x, y - 90, 90, 15));
        }else if (randomNumber == 1) {
            return new Line(x, y - 7.5,15);
        }else if (randomNumber == 3) {
            return new ObsDoubleCircle(x, y - 115, 90, 115, 15);
        }else if (randomNumber == 4) {
            return new ObsSquare(x , y - 85 * Math.sqrt(2), 170, 15);
        }else {
            return new ObsTriangle(x , y - 200 / Math.sqrt(3), 200, 15);
        }
    }

    public int getScore() {
        return this.player.getScore();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
