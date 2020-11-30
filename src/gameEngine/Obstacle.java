package gameEngine;

import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.io.File;

public abstract class Obstacle extends GameElement {

    protected double translationSpeed;
    protected double rotationalSpeed;
    protected double closestStar = 0;

    public double getClosestStar() {
        return closestStar;
    }

    Obstacle(double x, double y, double safeDist) {
        super(x, y, safeDist);
        rotationalSpeed = 90;
        audioClip = new AudioClip(new File("src/assets/music/gameplay/dead.wav").toURI().toString());
        audioClip.setVolume(0.5);
    }

    public abstract String getRandomColor();

    public abstract void update(double time);

    public boolean checkNotEqual(String a, String b) {
        return a != b;
    }
}
