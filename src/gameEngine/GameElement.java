package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioClip;

public abstract class GameElement {

    protected double x;
    protected double y;
    private double closestSafeDist;
    protected AudioClip audioClip;


    GameElement(double x, double y, double closestSafeDist) {
        this.x = x;
        this.y = y;
        this.closestSafeDist = closestSafeDist;
    }

    public double getClosestSafeDist() {
        return closestSafeDist;
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
         audioClip.play(); // TEMPORARY // TODO: MUSIC DISABLE
    }

    void applyOffset(double offset) {
        y += offset;
    }

    // TODO: Should GameElement Contain instance of GraphicsContext
    public abstract void refresh(GraphicsContext graphicsContext);
    public abstract boolean checkCollision(Ball ball);
    public abstract void destroy();

}
