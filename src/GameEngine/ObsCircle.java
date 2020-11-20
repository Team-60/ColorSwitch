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
        Renderer renderer = new Renderer(graphicsContext);
        int angle = 0;
        for (Color color : colors) {
            renderer.drawArc(getX(), getY(), radius, innerRadius, color, color, angle++);
        }
    }


    @Override
    public boolean checkCollision(Ball ball) {
        return false;
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
