package GameEngine;

import javafx.scene.paint.Color;

public abstract class Obstacle extends GameElement{

    private double translationSpeed;
    private double rotationalSpeed;
    private double rotationAngle;

    Obstacle(double x, double y, double safeDist) {
        super(x, y, safeDist);
        rotationalSpeed = 90;
        rotationAngle = 0;
    }

    // TODO: do a deep copy
    public abstract Color getRandomColor();

    public double getRotationAngle() {
        return rotationAngle;
    }

    void setRotationAngle(double time) {
        rotationAngle += rotationalSpeed * time;
    }

    public boolean checkNotEqual(Color a, Color b) {
        return a.getRed() != b.getRed() || a.getGreen() != b.getGreen() || a.getBlue() != b.getBlue();
    }
}
