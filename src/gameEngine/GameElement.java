package gameEngine;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameElement {

    private double x;
    private double y;
    private double closestSafeDist;

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

    void applyOffset(double offset) {
        y += offset;
    }

    // TODO: Should GameElement Contain instance of GraphicsContext
    // render == spawn
    public abstract void refresh(GraphicsContext graphicsContext);
    public abstract boolean checkCollision(Ball ball);
    public abstract void destroy();

}
