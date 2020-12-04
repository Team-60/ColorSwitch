package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particle {

    public double x, y;
    private GraphicsContext graphicsContext;
    private double radius;
    private double Vx, Vy;
    private final double gravity = 1700;
    private String color;
    Particle(double x, double y, double radius, double Vx, double Vy, GraphicsContext graphicsContext, String color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.graphicsContext = graphicsContext;
        this.Vx = Vx;
        this.Vy = Vy;
        this.color = color;
    }

    public void refresh() {
        graphicsContext.setFill(Color.web(color));
        graphicsContext.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    public double move(double time) {
        double distanceX = Vx * time;
        double distanceY = Vy * time + (gravity * time * time)/2;
        y += distanceY;
        x += distanceX;
        Vy += gravity * time;
        if (x < radius) {
            x = radius;
            Vx *= -1;
        }else if (x > GamePlay.WIDTH - radius) {
            x = GamePlay.WIDTH - radius;
            Vx *= -1;
        }
        return 0;
    }
}
