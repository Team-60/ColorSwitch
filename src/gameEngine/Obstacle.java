package gameEngine;

import javafx.scene.paint.Color;

public abstract class Obstacle extends GameElement{

    protected double translationSpeed;
    protected double rotationalSpeed;
    protected double closestStar = 0;

    public double getClosestStar() {
        return closestStar;
    }

    Obstacle(double x, double y, double safeDist) {
        super(x, y, safeDist);
        rotationalSpeed = 90;
    }

    // TODO: do a deep copy
    public abstract Color getRandomColor();
    public abstract void update(double time);

    public boolean checkNotEqual(Color a, Color b) {
        return a.getRed() != b.getRed() || a.getGreen() != b.getGreen() || a.getBlue() != b.getBlue();
    }


}
