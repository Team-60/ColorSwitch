package gameEngine.bubbles;

import gameEngine.GamePlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Bubble {
    private double x, y;
    private final double radius;
    private final GraphicsContext graphicsContext;
    private double Vx, Vy;

    private final Color color;

    Bubble(double x, double y, double radius, double Vx, double Vy, Color color, GraphicsContext graphicsContext) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.Vx = Vx;
        this.Vy = Vy;
        this.graphicsContext = graphicsContext;
        this.color = color;
    }

    public void move(double time) {
        x = x + Vx * time;
        y = y + Vy * time;

        if (x < radius) {
            x = radius;
            Vx *= -1;
        } else if (x > GamePlay.WIDTH - radius) {
            x = GamePlay.WIDTH - radius;
            Vx *= -1;
        }
        if (y < radius) {
            y = radius;
            Vy *= -1;
        } else if (y > GamePlay.HEIGHT - radius) {
            y = GamePlay.HEIGHT - radius;
            Vy *= -1;
        }
    }

    public void clipBubble() {
        graphicsContext.moveTo(x, y);
        graphicsContext.arc(x, y, radius, radius, 0, 360);
    }

    public void color() {
        graphicsContext.setFill(color.deriveColor(1, 1, 1, 0.1));
        graphicsContext.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    public void elasticCollisions(ArrayList<Bubble> fireFlies) {
        for (Bubble bubble : fireFlies) {
            if (bubble == this) continue;
            if (checkCollision(bubble)) {
                double vx = Vx, vy = Vy;
                double v2x = bubble.getVx(), v2y = bubble.getVy();
                double r1 = radius, r2 = bubble.getRadius();
                Vx = (r1 - r2)/(r1 + r2) * vx + 2 * (r2) / (r1 + r2) * v2x;
                bubble.setVx(2 * r1/(r1 + r2) * vx + (r2 - r1)/(r1 + r2) * v2x);

                Vy = (r1 - r2)/(r1 + r2) * vy + 2 * (r2) / (r1 + r2) * v2y;
                bubble.setVy(2 * r1/(r1 + r2) * vy + (r2 - r1)/(r1 + r2) * v2y);
            }
        }
    }


    public boolean checkCollision(Bubble bubble) {
        double distance = (x - bubble.getX()) * (x - bubble.getX())
                + (y - bubble.getY()) * (y - bubble.getY());

        return distance < (radius + bubble.getRadius()) * (radius + bubble.getRadius());
    }

    public void setVx(double vx) {
        Vx = vx;
    }

    public void setVy(double vy) {
        Vy = vy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public double getVx() {
        return Vx;
    }

    public double getVy() {
        return Vy;
    }

    public void debug() {
        graphicsContext.beginPath();
        graphicsContext.setLineWidth(2);
        graphicsContext.arc(x, y, radius, radius, 0, 360);
        graphicsContext.closePath();
        graphicsContext.stroke();
    }

}
