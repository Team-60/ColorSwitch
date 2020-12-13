package gameEngine;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("rawtypes")
public class Game implements Serializable, Comparable {

    private static final long serialVersionUID = 2020L;
    public static final String FILE_PATH = "src/data/dataGame.ser";
    private static final int numberOfObstacle = 14;
    private static final double distanceBetweenObstacles = 150;

    private transient App app;
    private final Player player;
    private final Ball ball;
    private ArrayList<GameElement> gameElements;
    private boolean gameOver = false;
    private int obstacleCount = 1;
    private int highScore;

    private transient Swarm swarm;
    private transient GraphicsContext graphicsContext; // can't serialize this
    private transient AudioClip fallDownClip = new AudioClip(new File("src/assets/music/gameplay/dead.wav").toURI().toString());

    Game(GraphicsContext graphicsContext, Player player, App app) {
        this.graphicsContext = graphicsContext;
        this.player = player;
        this.app = app;
        ball = new Ball(graphicsContext);

        double x = 225;
        double y = 350;
        gameElements = new ArrayList<>();
        Obstacle obstacle = new ObsCircle(225, 350, 90, 15);
        GameElement switchColor = new SwitchColor(x, obstacle.getTopY() - distanceBetweenObstacles/2);
        gameElements.add(obstacle);
        gameElements.add(switchColor);
        ball.setColor(obstacle.getRandomColor());
        swarm = new Swarm(graphicsContext);
        highScore = app.getHighscore();
    }

    public void reloadParam(GraphicsContext _graphicsContext, App _app) { // after deserializing, game's swarm and graphics context, with app and refresh highscore
        assert (!this.gameOver); // only load when game is active, or it's a revival
        this.graphicsContext = _graphicsContext;
        this.app = _app;
        this.highScore = this.app.getHighscore();
        this.swarm = new Swarm(this.graphicsContext);

        this.ball.setGraphicsContext(this.graphicsContext);
        for (GameElement g : this.gameElements) {
            g.loadAssets();
        }

        this.fallDownClip = new AudioClip(new File("src/assets/music/gameplay/dead.wav").toURI().toString());
    }

    private void moveScreenRelative(double offset) {
        for (GameElement gameElement : gameElements) {
            gameElement.applyOffset(offset);
        }
    }

    public void checkAndUpdate(double time) {
        if (gameOver) {
            swarm.update(time/1.2);
            return;
        }
        double offset = ball.move(time, this.player);
        if (ball.getY() + ball.getRadius() > 700) { // check if game over due to fall down, throw exception, ball shouldn't be visible at all
            gameOver = true;
            swarm.explode(this.ball);
            App.BgMediaPlayer.stop();
            this.fallDownClip.play();
        }
        moveScreenRelative(offset);

        double y = 350;
        double x = 225;

        // remove unwanted objects
        ArrayList<GameElement> gameElementsTemp = new ArrayList<>();
        for (GameElement gameElement : gameElements) {

            if (gameElement instanceof Obstacle) {
                if (gameElement.getTopY() > ball.getY()) {
                    ((Obstacle) gameElement).setCrossed();
                }
            }

            if (gameElement instanceof Obstacle && ((Obstacle) gameElement).checkCollisionStar(ball)) {
                player.incScore();
                ((Obstacle) gameElement).destroyStar();
            }
            if (gameElement.checkCollision(ball)) {
                gameElement.playSound();
                if (gameElement instanceof Obstacle) {
                    gameOver = true;
                    swarm.explode(this.ball);
                    gameElementsTemp.add(gameElement);
                }
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
                    y = gameElements.get(i).getTopY();
                    y -= distanceBetweenObstacles;
                    break;
                }
            }
        }
        boolean displayed = false;
        while(gameElements.size() < 16) {
            Obstacle obstacle = getRandomObstacle(x, y);
            obstacleCount += obstacle.getMaxCount();
            y = obstacle.getTopY();

            if (obstacleCount >= highScore && !displayed) {
                GameElement highScoreLine = new HighScoreLine(y - distanceBetweenObstacles/4);
                gameElements.add(highScoreLine);
                displayed = true;
            }

            GameElement switchColor = new SwitchColor(x, y - distanceBetweenObstacles/2);

            gameElements.add(obstacle);
            gameElements.add(switchColor);
            y -= distanceBetweenObstacles;
        }

        GameElement prev = null;
        // give each colorSwitch an Obstacle
        for (GameElement gameElement : gameElements) {
            if (gameElement instanceof SwitchColor) {
                prev = gameElement;
            } else if (gameElement instanceof Obstacle && prev != null) {
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
        this.player.incJumps();
    }

    public void refreshGameElements() {
        swarm.refresh();
        for (GameElement gameElement : gameElements) {
            gameElement.refresh(graphicsContext);
        }
        if (gameOver) return;
        ball.refresh();
    }

    private int getMean() {
        if (player.getScore() >= 13) {
            return 13;
        }else {
            return player.getScore();
        }
    }

    public Obstacle getRandomObstacle(double x, double y) {

//        int randomNumber = (int)((new Random()).nextGaussian() * 2 + getMean());
        // y - safe dist of that specific obstacle
        int randomNumber = 18;
        if (randomNumber < 0) {
            randomNumber = 0;
        }
        if (randomNumber == 0) {
            return (new ObsCircle(x, y - 90, 90, 15));
        } else if (randomNumber == 1) {
            return new ObsLine(x, y - 7.5,15);
        } else if (randomNumber == 2) {
            return new ObsDoubleCircle(x, y - 115, 90, 115, 15);
        } else if (randomNumber == 3) {
            return new ObsSquare(x , y - 85 * Math.sqrt(2), 170, 15);
        } else if (randomNumber == 4) {
            return new ObsTriangle(x , y - 200 / Math.sqrt(3), 200, 15);
        } else if (randomNumber == 5) {
            // smaller square
            Obstacle obstacle = new ObsSquare(x , y - 70 * Math.sqrt(2), 140, 13);
            return obstacle;
        } else if (randomNumber == 6) {
            // faster square with same size
            Obstacle obstacle = new ObsSquare(x , y - 85 * Math.sqrt(2), 170, 15);
            obstacle.setRotationalSpeed(150);
            return obstacle;
        } else if (randomNumber == 7) {
            // smaller circle
            Obstacle obstacle = new ObsCircle(x, y - 75, 75, 10);
            return obstacle;
        } else if (randomNumber == 8) {
            // smaller double circle
            Obstacle obstacle = new ObsDoubleCircle(x, y - 90, 70, 90, 13);
            return obstacle;
        } else if (randomNumber == 9) {
            // faster double circle with same size
            Obstacle obstacle = new ObsDoubleCircle(x, y - 115, 90, 115, 15);
            obstacle.setRotationalSpeed(150);
            return obstacle;
        } else if (randomNumber == 10) {
            // faster circle with same radius
            Obstacle obstacle = new ObsCircle(x, y - 90, 90, 15);
            obstacle.setRotationalSpeed(150);
            return obstacle;
        } else if (randomNumber == 11) {
            // smaller triangle
            Obstacle obstacle = new ObsTriangle(x , y - 190 / Math.sqrt(3), 190, 13);
            return obstacle;
        } else if (randomNumber == 12) {
            // faster triangle with same size
            Obstacle obstacle = new ObsTriangle(x , y - 200 / Math.sqrt(3), 200, 15);
            obstacle.setRotationalSpeed(130);
            return obstacle;
        } else if (randomNumber == 13) {
            // super slow circle
            Obstacle obstacle = new ObsCircle(x, y - 65, 65, 8);
            obstacle.setRotationalSpeed(50);
            return obstacle;
        } else if (randomNumber == 14) {
            ObsCircle circle = new ObsCircle(x, y - 90, 90, 15);
            Obstacle obstacle = new ObsConsecutiveCircles(circle, 5);
            return obstacle;
        } else if (randomNumber == 15) {
            ObsCircle doubleCircle = new ObsDoubleCircle(x, y - 115, 90, 115, 15);
            Obstacle obstacle = new ObsConsecutiveCircles(doubleCircle, 5);
            return obstacle;
        } else if (randomNumber == 16) {
            return new ObsTripleCircle(x, y - 140, 90, 115, 140, 15);
        } else if (randomNumber == 17) {
            ObsCircle obsTripleCircle = new ObsTripleCircle(x, y - 140, 90, 115, 140, 15);
            Obstacle obstacle = new ObsConsecutiveCircles(obsTripleCircle, 5);
            return obstacle;
        } else {
            return new ObsSquareCircle(x , y - 85 * Math.sqrt(2), 200, 15);
        }
    }


    void revive() {
        gameOver = true;
        for (GameElement gameElement : gameElements) {
            if (gameElement instanceof Obstacle) {
                if (!((Obstacle) gameElement).isCrossed()) {
                    ball.setY(gameElement.getBottomY() - distanceBetweenObstacles/2);
                    break;
                }
            }
        }
    }


    public Player getPlayer() { // ref. all info attributes from player
        return this.player;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public int compareTo(Object o) { // for saving, based on id
        assert (o instanceof Game);
        Game g = (Game) o;
        int idThis = (this.getPlayer().getId() == -1) ? Integer.MAX_VALUE : this.getPlayer().getId();
        int idThat = (g.getPlayer().getId() == -1) ? Integer.MAX_VALUE : g.getPlayer().getId();
        return Integer.compare(idThat, idThis);
    }

    @Override
    public String toString() {
        return this.getPlayer().toString();
    }
}
