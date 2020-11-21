package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ObsDoubleCircle extends ObsCircle {

    private final ObsCircle innerCircle;
    private final ArrayList<Color> colors = new ArrayList<>()
    {{
        add(Color.web("32E1F4"));
        add(Color.web("FD0082"));
        add(Color.web("F6DF0E"));
        add(Color.web("8E11FE"));
    }};           // Note : differs from UML

    ObsDoubleCircle(double x, double y, double closestSafeDist, double innerRadius, double outerRadius, double width) {
        super(x, y, closestSafeDist, outerRadius, width);
        innerCircle = new ObsCircle(x, y, closestSafeDist, innerRadius, width);
        addRotationalAngle(45);
        innerCircle.addRotationalAngle(45);
        setColors(colors);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        super.refresh(graphicsContext);
        innerCircle.refresh(graphicsContext);
    }

    @Override
    public boolean checkCollision(Ball ball) {

        return super.checkCollision(ball) || innerCircle.checkCollision(ball);
    }

    @Override
    public void destroy() {

    }

    @Override
    void applyOffset(double offset) {
        super.applyOffset(offset);
        innerCircle.applyOffset(offset);
    }

    @Override
    void updateRotationAngle(double time) {
        addRotationalAngle(getRotationalSpeed() * time);
        innerCircle.addRotationalAngle(-getRotationalSpeed() * time);
    }


}
