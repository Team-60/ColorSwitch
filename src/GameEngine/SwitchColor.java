package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;

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
        Image image = new Image(new File("src/assets/gameplay/color_switcher_s.png").toURI().toString());
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
            while (checkEqual(color, prev)) {
                color = obstacle.getRandomColor();
            }
            ball.setColor(color);
            destroy();
            points++;
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }

    public boolean checkEqual(Color a, Color b) {
        return !(a.getRed() != b.getRed() || a.getGreen() != b.getGreen() || a.getBlue() != b.getBlue());
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

}
