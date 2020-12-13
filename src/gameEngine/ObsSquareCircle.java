package gameEngine;

import javafx.scene.canvas.GraphicsContext;

public class ObsSquareCircle extends Obstacle {

    ObsSquare square;
    ObsCircle circle;

    ObsSquareCircle(double x, double y, double sideLength, double width) {
        super(x, y, y - sideLength/2 * Math.sqrt(2), y + sideLength/2 * Math.sqrt(2));
        square = new ObsSquare(x, y, sideLength, width);
        circle = new ObsCircle(x, y, (sideLength - 2 * width)/2 - 3 , width);
        circle.destroyStar();
        circle.mirrorY();
        circle.rotate(45);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        square.refresh(graphicsContext);
        circle.refresh(graphicsContext);
    }

    @Override
    public boolean checkCollision(Ball ball) {
        return square.checkCollision(ball) || circle.checkCollision(ball);
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
        circle.update(-time);
        square.update(time);
    }

    @Override
    public void applyOffset(double offset) {
        circle.applyOffset(offset);
        square.applyOffset(offset);
    }

    @Override
    public boolean checkCollisionStar(Ball ball) {
        return square.checkCollisionStar(ball);
    }
}
