package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ObsCircle extends Obstacle{

    private static final double closestSafeDist = 100;     // TODO: not final
    private final double radius;
    private final double innerRadius;
    private final ArrayList<Color> colors = new ArrayList<>()
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
        if (top < getY() + radius && top > getY() + innerRadius) {
            int angle = (int) (getRotationAngle() % 360);
            boolean isCollided = false;
            if (angle > 0 && angle < 90) {
                isCollided = !checkEqual(colors.get(2), ball.getColor());
            }
            if (angle > 90 && angle < 180) {
                isCollided |= !checkEqual(colors.get(1), ball.getColor());
            }
            if (angle > 180 && angle < 270) {
                isCollided |= !checkEqual(colors.get(0), ball.getColor());
            }
            if (angle > 270 && angle < 360) {
                isCollided |= !checkEqual(colors.get(3), ball.getColor());
            }

            return isCollided;

        }else if (top > getY() - radius && top < getY() - innerRadius) {

            int angle = (int) (getRotationAngle() % 360);
            boolean isCollided = false;
            if (angle > 0 && angle < 90) {
                isCollided = !checkEqual(colors.get(0), ball.getColor());
            }
            if (angle > 90 && angle < 180) {
                isCollided |= !checkEqual(colors.get(3), ball.getColor());
            }
            if (angle > 180 && angle < 270) {
                isCollided |= !checkEqual(colors.get(2), ball.getColor());
            }
            if (angle > 270 && angle < 360) {
                isCollided |= !checkEqual(colors.get(1), ball.getColor());
            }

            return isCollided;

        }

        return false;
    }

    public boolean checkEqual(Color a, Color b) {
        return a.getRed() == b.getRed() && a.getGreen() == b.getGreen() && a.getBlue() == b.getBlue();
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
