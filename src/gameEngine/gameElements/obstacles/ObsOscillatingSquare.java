package gameEngine.gameElements.obstacles;

import gameEngine.Ball;
import gameEngine.gameElements.Star;
import javafx.scene.canvas.GraphicsContext;

public class ObsOscillatingSquare extends Obstacle{
    ObsSquare obsSquare;
    public ObsOscillatingSquare(double x, double y, double sideLength, double width) {
        super(x, y, y - sideLength/2 * Math.sqrt(2) - sideLength + 130, y + sideLength/2 * Math.sqrt(2) + sideLength - 130);
        obsSquare = new ObsSquare(x, y, sideLength, width);
    }

    @Override
    public void applyOffset(double offset) {
        super.applyOffset(offset);
        obsSquare.applyOffset(offset);
    }


    @Override
    public void refresh(GraphicsContext graphicsContext) {
        obsSquare.refresh(graphicsContext);

    }

    @Override
    public boolean checkCollision(Ball ball) {
        return obsSquare.checkCollision(ball);
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getRandomColor() {
        return obsSquare.getRandomColor();
    }

    @Override
    public void update(double time) {
        obsSquare.update(time);
        double pos = y + Math.sin((2 * Math.PI)/360 * obsSquare.getRotationAngle()) * (obsSquare.getSideLength() - 130);
        double difference = pos - obsSquare.getY();
        obsSquare.applyOffset(difference);
        Star star = obsSquare.getStar();
        if (star != null) {
            star.applyOffset(-difference);
        }
    }

    @Override
    public boolean checkCollisionStar(Ball ball) {
        return obsSquare.checkCollisionStar(ball);
    }
}
