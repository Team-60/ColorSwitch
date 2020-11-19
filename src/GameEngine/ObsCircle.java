package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

public class ObsCircle extends Obstacle{

    public static double closestSafeDist = 100;     // TODO: not final
    public double radius;
    public double innerRadius;
    public double width;
    public Renderer renderer;

    ObsCircle(double x, double y, double closestSafeDist, double radius, double width) {
        super(x, y, closestSafeDist);
        this.radius = radius;
        this.width = width;
        this.innerRadius = radius - width;
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        renderer = new Renderer(graphicsContext);
        Color Yellow = Color.web("F6DF0E");
        Color Purple = Color.web("8E11FE");
        Color Cyan = Color.web("32E1F4");
        Color Pink = Color.web("FD0082");
        renderer.drawArc(getX(), getY(), radius, innerRadius, Yellow, Yellow, 0);
        renderer.drawArc(getX(), getY(), radius, innerRadius, Cyan, Cyan, 1);
        renderer.drawArc(getX(), getY(), radius, innerRadius, Pink, Pink, 2);
        renderer.drawArc(getX(), getY(), radius, innerRadius, Purple, Purple, 3);
    }


    @Override
    public void checkCollision(Ball ball) {

    }

    @Override
    public void destroy() {

    }

}
