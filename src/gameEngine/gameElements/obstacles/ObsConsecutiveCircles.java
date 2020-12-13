package gameEngine.gameElements.obstacles;

import gameEngine.Ball;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class ObsConsecutiveCircles extends Obstacle {

    private ArrayList<ObsCircle> circles;

    public ObsConsecutiveCircles(ObsCircle obsCircle, int numberOfCircles) {
        super(obsCircle.getX(), obsCircle.getY(), obsCircle.getY() - (2 *  numberOfCircles - 1) * obsCircle.getRadius(), obsCircle.getY() + obsCircle.getRadius());
        circles = new ArrayList<>();
        circles.add(obsCircle);
        for (int i = 0; i < numberOfCircles - 1; ++i) {
            circles.add(circles.get(i).generateNext());
            if (i % 2 == 0) {
                circles.get(i + 1).rotate(180);
            }
        }
    }

    @Override
    public String getRandomColor() {
        return circles.get(0).getRandomColor();
    }

    @Override
    public void update(double time) {
        for (ObsCircle obsCircle : circles) {
            obsCircle.update(time);
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
    public void applyOffset(double offset) {
        super.applyOffset(offset);
        for (ObsCircle obsCircle : circles) {
            obsCircle.applyOffset(offset);
        }
    }

    @Override
    public void destroy() {

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
    public int getMaxCount() {
        return circles.size();
    }
}
