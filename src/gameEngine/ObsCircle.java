package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ObsCircle extends Obstacle{

    private static final double closestSafeDist = 100;     // TODO: not final
    private final double radius;
    private final double innerRadius;

    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    private ArrayList<Color> colors = new ArrayList<>()
    {{
        add(Color.web("F6DF0E"));
        add(Color.web("8E11FE"));
        add(Color.web("32E1F4"));
        add(Color.web("FD0082"));
    }};           // Note : differs from UML

    ObsCircle(double x, double y, double closestSafeDist, double radius, double width) {
        super(x, y, closestSafeDist);
        this.radius = radius;
        this.innerRadius = radius - width;
    }

    ObsCircle(double x, double y, double radius, double width) {
        super(x, y, closestSafeDist);
        this.radius = radius;
        this.innerRadius = radius - width;
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {

        graphicsContext.translate(getX(), getY());
        graphicsContext.rotate(-getRotationAngle());
        Renderer renderer = new Renderer(graphicsContext);
        int angle = 0;
        for (Color color : colors) {
            renderer.drawArc(0, 0, radius, innerRadius, color, color, angle++);
        }

        graphicsContext.rotate(getRotationAngle());
        graphicsContext.translate(-getX(), -getY());
    }



    @Override
    public boolean checkCollision(Ball ball) {

        double top = ball.getY() - ball.getRadius();
        double bottom = ball.getY() + ball.getRadius();
        int posAngle = (int)getRotationAngle();
        while(posAngle < 0) posAngle += 360;
        int angle = posAngle % 360;
        boolean isCollided = false;

        if ((top < getY() + radius && top > getY() + innerRadius) || (bottom < getY() + radius && bottom > getY() + innerRadius)) {

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
        } else if ((top > getY() - radius && top < getY() - innerRadius) || (bottom > getY() - radius && bottom < getY() - innerRadius) ) {

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

    @Override
    public void destroy() {

    }

    @Override
    public Color getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }
}
