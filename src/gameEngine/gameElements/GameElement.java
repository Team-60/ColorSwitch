package gameEngine.gameElements;

import gameEngine.App;
import gameEngine.Ball;
import gameEngine.gameElements.obstacles.Obstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.Serializable;

// template and factory pattern ig
public abstract class GameElement implements Serializable {

    private static final long serialVersionUID = 2021L;

    protected double x;
    protected double y;
    protected double topY, bottomY;

    protected String audioClipPath;

    private transient AudioClip audioClip;

    public GameElement(double x, double y, double topY, double bottomY) {
        this.x = x;
        this.y = y;
        this.topY = topY;
        this.bottomY = bottomY;
    }

    public void loadAssets() { // after deserialization
        if (this instanceof HighScoreLine) return;
        assert (this.audioClipPath != null);
        this.audioClip = new AudioClip(new File(this.audioClipPath).toURI().toString());
        this.audioClip.setVolume(0.5);
    }

    public double getTopY() {
        return topY;
    }
    public double getBottomY() {
        return bottomY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void playSound() {
        if (this instanceof Obstacle) // as game over, maybe do this in game over exception
            App.BgMediaPlayer.stop();
        try {
            this.audioClip.play(); // TEMPORARY // TODO: MUSIC DISABLE
        } catch (NullPointerException e) { // TODO: FIX STAR ASSET
            System.out.println(this.getClass().toString() + "Caught asset loading in star, " + e.toString());
            this.audioClip = new AudioClip(new File(this.audioClipPath).toURI().toString());
            this.audioClip.setVolume(0.5);
            this.audioClip.play();
        }
    }

    public void applyOffset(double offset) {
        y += offset;
        topY += offset;
        bottomY += offset;
    }

    public abstract void refresh(GraphicsContext graphicsContext);
    public abstract boolean checkCollision(Ball ball);
    public abstract void destroy();
}
