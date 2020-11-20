package GameEngine;

import javafx.scene.paint.Color;

public abstract class Obstacle extends GameElement{

    private double translationSpeed;
    private double rotationalSpeed;

    Obstacle(double x, double y, double safeDist) {
        super(x, y, safeDist);
    }

    // TODO: do a deep copy
    public abstract Color getRandomColor();

}
