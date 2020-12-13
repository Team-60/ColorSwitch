package gameEngine;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class ObsTripleCircle extends ObsCircle {


    private final ArrayList<ObsCircle> circles;

    ObsTripleCircle(double x, double y, double innerInnerRadius, double innerRadius,  double outerRadius, double width) {
        super(x, y, outerRadius, width);
        super.destroyStar();
        circles = new ArrayList<>();
        circles.add(new ObsCircle(x, y, outerRadius, width));
        circles.add(new ObsCircle(x, y, innerRadius, width));
        circles.add(new ObsCircle(x, y, innerInnerRadius, width));
        circles.get(0).destroyStar();
        circles.get(1).destroyStar();
        circles.get(1).mirrorY();
        for (ObsCircle circle : circles) {
            circle.setRotationAngle(45);
        }
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        for (ObsCircle obsCircle : circles) {
            obsCircle.refresh(graphicsContext);
        }
    }

    @Override
    public boolean checkCollision(Ball ball) {
        boolean isCollided = false;
        for (ObsCircle obsCircle : circles) {
            isCollided |= obsCircle.checkCollision(ball);
        }
        return isCollided;
    }

    @Override
    public void rotate(double angle) {
        for (ObsCircle circle : circles) {
            circle.rotate(angle);
        }
    }

    @Override
    public boolean checkCollisionStar(Ball ball) {
        boolean isCollided = false;
        for (ObsCircle obsCircle : circles) {
            isCollided |= obsCircle.checkCollisionStar(ball);
        }
        return isCollided;
    }

    @Override
    public void applyOffset(double offset) {
        for (ObsCircle obsCircle : circles) {
            obsCircle.applyOffset(offset);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getRandomColor() {
        return circles.get(0).getRandomColor();
    }

    @Override
    public void update(double time) {
        for (ObsCircle obsCircle : circles) {
            obsCircle.update(time);
            time *= -1;
        }
    }

    @Override
    public ObsCircle generateNext() {
        return new ObsTripleCircle(x, y - 2 * radius - 5, circles.get(2).radius, circles.get(1).radius, circles.get(0).radius, radius - innerRadius);
    }

}
