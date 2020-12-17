package gameEngine.gameElements.obstacles;

import gameEngine.Ball;
import gameEngine.gameElements.Star;
import javafx.scene.canvas.GraphicsContext;

public class ObsOscillatingCircle extends Obstacle {

    private final ObsCircle circle;

    public ObsOscillatingCircle(double x, double y, double radius, double width) {
        super(x, y -  radius/2, y - 2 * radius, y + radius);
        circle = new ObsCircle(x, y - radius/2, radius, width);
    }

    @Override
    public void applyOffset(double offset) {
        super.applyOffset(offset);
        circle.applyOffset(offset);
    }


    @Override
    public void refresh(GraphicsContext graphicsContext) {
        circle.refresh(graphicsContext);

    }

    @Override
    public boolean checkCollision(Ball ball) {
        return circle.checkCollision(ball);
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getRandomColor() {
        return circle.getRandomColor();
    }

    @Override
    public void update(double time) {
        circle.update(time);
        double pos = y + Math.sin((2 * Math.PI)/360 * circle.getRotationAngle()) * (circle.getRadius()/2 - 10);
        double difference = pos - circle.getY();
        circle.applyOffset(difference);
        Star star = circle.getStar();
        if (star != null) {
            star.applyOffset(-difference);
        }
    }

    @Override
    public boolean checkCollisionStar(Ball ball) {
        return circle.checkCollisionStar(ball);
    }
}
