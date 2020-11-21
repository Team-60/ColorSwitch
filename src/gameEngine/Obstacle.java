package gameEngine;

import javafx.scene.paint.Color;

public abstract class Obstacle extends GameElement{

    private double translationSpeed;
    private double rotationalSpeed;
    private double rotationAngle;
    private double startingPoint = 0;

    Obstacle(double x, double y, double safeDist) {
        super(x, y, safeDist);
        rotationalSpeed = 90;
        rotationAngle = 0;
        startingPoint = 0;
    }

    // TODO: do a deep copy
    public abstract Color getRandomColor();

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void addRotationalAngle(double rotationalAngle) {
        this.rotationAngle += rotationalAngle;
    }

    public double getRotationalSpeed() {
        return rotationalSpeed;
    }

    void updateRotationAngle(double time) {
        rotationAngle += rotationalSpeed * time;
    }

    public boolean checkNotEqual(Color a, Color b) {
        return a.getRed() != b.getRed() || a.getGreen() != b.getGreen() || a.getBlue() != b.getBlue();
    }

    public void setTranslationSpeed(double translationSpeed) {
        this.translationSpeed = translationSpeed;
    }

    public double getStartingPoint() {
        return startingPoint;
    }

    public void updateStartingPoint(double time) {
        startingPoint += translationSpeed * time;
        startingPoint %= GameApp.WIDTH;
    }

}
