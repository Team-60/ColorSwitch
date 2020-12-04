package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ObsCircle extends Obstacle{

    private final double radius;
    private final double innerRadius;
    protected double rotationAngle;

    private ArrayList<String> colors = new ArrayList<>()
    {{
        add("F6DF0E");
        add("8E11FE");
        add("32E1F4");
        add("FD0082");
    }};

    ObsCircle(double x, double y, double radius, double width) {
        super(x, y, radius);
        this.radius = radius;
        this.innerRadius = radius - width;
        rotationAngle = 0;
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {

        graphicsContext.translate(getX(), getY());
        graphicsContext.rotate(-rotationAngle);
        int angle = 0;
        for (String color : colors) {
            Renderer.drawArc(0, 0, radius, innerRadius, Color.web(color), Color.web(color), angle++);
        }
        graphicsContext.rotate(rotationAngle);
        graphicsContext.translate(-getX(), -getY());
    }


    @Override
    public void update(double time) {
        rotationAngle += rotationalSpeed * time;
        while(rotationAngle < 0) {
            rotationAngle += 360;
        }
        rotationAngle %= 360;
    }

    @Override
    public boolean checkCollision(Ball ball) {

        double top = ball.getY() - ball.getRadius();
        double bottom = ball.getY() + ball.getRadius();
        int angle = (int)rotationAngle % 360;
        boolean isCollided = false;

        if ((top < getY() + radius && bottom > getY() + innerRadius)) {

            if (angle > 355 || (angle > 0 && angle < 95)) {
                isCollided = checkNotEqual(colors.get(2), ball.getColor());
            }
            if (angle > 85 && angle < 185) {
                isCollided |= checkNotEqual(colors.get(1), ball.getColor());
            }
            if (angle > 175 && angle < 275) {
                isCollided |= checkNotEqual(colors.get(0), ball.getColor());
            }
            if ((angle > 265) || (angle < 5)) {
                isCollided |= checkNotEqual(colors.get(3), ball.getColor());
            }
        } else if ((top < getY() - innerRadius && bottom > getY() - radius)) {

            if (angle > 355 || (angle > 0 && angle < 95)) {
                isCollided = checkNotEqual(colors.get(0), ball.getColor());
            }
            if (angle > 85 && angle < 185) {
                isCollided |= checkNotEqual(colors.get(3), ball.getColor());
            }
            if (angle > 175 && angle < 275) {
                isCollided |= checkNotEqual(colors.get(2), ball.getColor());
            }
            if ((angle > 265) || (angle < 5)) {
                isCollided |= checkNotEqual(colors.get(1), ball.getColor());
            }
        }

        return isCollided;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }
}
