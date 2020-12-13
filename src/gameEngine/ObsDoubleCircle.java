package gameEngine;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class ObsDoubleCircle extends ObsCircle {

    private final ObsCircle innerCircle;

    ObsDoubleCircle(double x, double y, double innerRadius, double outerRadius, double width) {
        super(x, y, outerRadius, width);
        super.destroyStar();             // only one star for one two circles
        super.mirrorY();
        innerCircle = new ObsCircle(x, y, innerRadius, width);
        rotationAngle = 45;
        innerCircle.setRotationAngle(45);
        setColors(colors);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        super.refresh(graphicsContext);
        innerCircle.refresh(graphicsContext);
    }

    @Override
    public void setRotationalSpeed(double rotationalSpeed) {
        super.setRotationalSpeed(rotationalSpeed);
        innerCircle.setRotationalSpeed(rotationalSpeed);
    }
    @Override
    public boolean checkCollision(Ball ball) {
        return super.checkCollision(ball) || innerCircle.checkCollision(ball);
    }

    @Override
    public boolean checkCollisionStar(Ball ball) {
        return super.checkCollisionStar(ball) || innerCircle.checkCollisionStar(ball);
    }

    @Override
    public void rotate(double angle) {
        super.rotate(angle);
        innerCircle.rotate(angle);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void destroyStar() {
        super.destroyStar();
        innerCircle.destroyStar();
    }

    @Override
    public void applyOffset(double offset) {
        super.applyOffset(offset);
        innerCircle.applyOffset(offset);
    }

    @Override
    public void update(double time) {
        super.update(time);
        innerCircle.update(-time);
    }

    @Override
    public ObsCircle generateNext() {
        return new ObsDoubleCircle(x, y - 2 * radius - 5, innerCircle.radius, radius, radius - innerRadius);
    }

}
