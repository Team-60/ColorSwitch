package gameEngine.gameElements.obstacles;

import gameEngine.Ball;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class ObsCircleTriangle extends Obstacle {


    private final ObsTriangle triangle;
    private final ObsCircle circle;

    public ObsCircleTriangle(double x, double y, double sideLength, double width) {
        super(x, y, y - sideLength / Math.sqrt(3), y + sideLength/Math.sqrt(3));
        triangle = new ObsTriangle(x, y, sideLength, width);
        circle = new ObsCircle(x, y, sideLength / (2 * Math.sqrt(3)) - width, width/2);
        circle.destroyStar();
        circle.rotate(80);
        circle.mirrorY();
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        triangle.refresh(graphicsContext);
        circle.refresh(graphicsContext);
    }

    @Override
    public boolean checkCollision(Ball ball) {
        return circle.checkCollision(ball) || triangle.checkCollision(ball);
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getRandomColor() {
        Random random = new Random();
        return triangle.getColors().get(random.nextInt(triangle.getColors().size() - 1));
    }

    @Override
    public void applyOffset(double offset) {
        circle.applyOffset(offset);
        triangle.applyOffset(offset);
    }

    @Override
    public boolean checkCollisionStar(Ball ball) {
        return triangle.checkCollisionStar(ball);
    }

    @Override
    public void update(double time) {
        circle.update(-time);
        triangle.update(time);
    }

}
