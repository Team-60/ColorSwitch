package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SwitchColor extends GameElement{

    // TODO: why is this useful??
    private static final int closestSafeDist = 100;
    private Obstacle obstacle;

    private static int points = 0;

    SwitchColor(double x, double y) {
        super(x, y, closestSafeDist);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        Image image = new Image("file:/home/zyrch/IdeaProjects/ColorSwitch/Assets/color_switcher_s.png");
        double x = getX();
        double y = getY();
        x -= image.getWidth()/2;
        y -= image.getHeight()/2;
        graphicsContext.drawImage(image, x, y);
    }

    @Override
    public boolean checkCollision(Ball ball) {
        if (ball.getY() < getY() + 18) {
            Color prev = ball.getColor();
            // If the number of colors in ball is small enough (== 1 or 2) infinite loop may occur
            Color color = prev;
            while(color == prev) {
                color = obstacle.getRandomColor();
            }
            ball.setColor(obstacle.getRandomColor());
            destroy();
            points++;
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

}
