package gameEngine;

import gameEngine.customExceptions.FallOutException;
import gameEngine.customExceptions.GameOverException;
import gameEngine.customExceptions.ObstacleCollisionException;
import gameEngine.gameElements.*;
import gameEngine.gameElements.obstacles.*;
import gameEngine.swarm.Swarm;
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

public class Game implements Serializable, Comparable<Game> {

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
    private boolean highScoreLineDisplayed;

    private transient Swarm swarm;
    private transient GraphicsContext graphicsContext; // can't serialize this
    private transient AudioClip fallDownClip = new AudioClip(new File("src/assets/music/gameplay/dead.wav").toURI().toString());

    Game(GraphicsContext graphicsContext, Player player, App app) {
        this.graphicsContext = graphicsContext;
        this.player = player;
        this.app = app;
        this.highScoreLineDisplayed = false;
        ball = new Ball(graphicsContext);

        double x = 225;
        double y = 350;
        gameElements = new ArrayList<>();
        Obstacle obstacle = new ObsCircle(225, 350, 90, 15);
        GameElement switchColor = new SwitchColor(x, obstacle.getTopY() - distanceBetweenObstacles / 2);
        gameElements.add(obstacle);
        gameElements.add(switchColor);
        ball.setColor(obstacle.getRandomColor());
        swarm = new Swarm(graphicsContext);
        highScore = app.getHighscore();
    }

    public void reloadParam(GraphicsContext _graphicsContext, App _app) { // after deserializing, game's swarm and graphics context, with app and refresh highscore
        assert (!this.gameOver && this.player != null); // only load when game is active, or it's a revival
        this.graphicsContext = _graphicsContext;
        this.app = _app;
        this.highScore = this.app.getHighscore();
        this.swarm = new Swarm(this.graphicsContext);

        this.ball.setGraphicsContext(this.graphicsContext);
        for (GameElement g : this.gameElements) {
            g.loadAssets();
            if (g instanceof Obstacle) { // as every obstacle has it's own star
                Star star = ((Obstacle) g).getStar();
                if (star != null) star.loadAssets(); // as star could be destroyed to null
            }
        }

        this.fallDownClip = new AudioClip(new File("src/assets/music/gameplay/dead.wav").toURI().toString());
    }

    public void reloadParamRevival(GraphicsContext _graphicsContext) { // for revival, reset graphics context
        assert (this.player != null);
        this.graphicsContext = _graphicsContext;
        this.swarm = new Swarm(this.graphicsContext);
        this.ball.setGraphicsContext(_graphicsContext);
    }

    public void moveScreenRelative(double offset) {
        for (GameElement gameElement : gameElements) {
            gameElement.applyOffset(offset);
        }
    }

    public void checkAndUpdate(double time) throws GameOverException {
        if (gameOver) {
            swarm.update(time / 1.2);
            return;
        }
        double offset = ball.move(time, this.player);
        if (ball.getY() + ball.getRadius() > 700) { // check if game over due to fall down, throw exception, ball shouldn't be visible at all
            gameOver = true;
            swarm.explode(this.ball);
            App.BgMediaPlayer.stop();
            this.fallDownClip.play();
            throw new FallOutException();
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
                if (gameElement instanceof Obstacle) { // game over condition
                    gameOver = true;
                    swarm.explode(this.ball);
                    gameElementsTemp.add(gameElement);
                    throw new ObstacleCollisionException(((Obstacle) gameElement).getClass());
                }
                continue;
            }
            if (gameElement.getTopY() < 1000) {
                if (gameElement instanceof Obstacle) {
                    ((Obstacle) gameElement).update(time);
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

        while (gameElements.size() < 16) {
            Obstacle obstacle = ObstacleFactory.obstacle(obstacleCount, x, y);
            obstacleCount += obstacle.getMaxCount();
            y = obstacle.getTopY();

            if (obstacleCount >= highScore && !highScoreLineDisplayed) {
                GameElement highScoreLine = new HighScoreLine(y - distanceBetweenObstacles / 4);
                gameElements.add(highScoreLine);
                highScoreLineDisplayed = true;
            }

            GameElement switchColor = new SwitchColor(x, y - distanceBetweenObstacles / 2);

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
                ((SwitchColor) prev).setObstacle((Obstacle) gameElement);
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



    public void revive() { // reset parameters after revival
        gameOver = false;
        for (GameElement gameElement : gameElements) {
            if (gameElement instanceof Obstacle) {
                if (!((Obstacle) gameElement).isCrossed()) {
                    ball.setY(gameElement.getBottomY() + distanceBetweenObstacles / 2);
                    break;
                }
            }
        }
    }

    public Ball getBall() {
        return this.ball;
    }

    public Player getPlayer() { // ref. all info attributes from player
        return this.player;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public App getApp() {
        return this.app;
    }

    @Override
    public int compareTo(Game g) { // for saving, based on id
        int idThis = (this.getPlayer().getId() == -1) ? Integer.MAX_VALUE : this.getPlayer().getId();
        int idThat = (g.getPlayer().getId() == -1) ? Integer.MAX_VALUE : g.getPlayer().getId();
        return Integer.compare(idThat, idThis);
    }

    @Override
    public String toString() {
        return this.getPlayer().toString();
    }
}
