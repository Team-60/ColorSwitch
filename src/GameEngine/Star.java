package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Star extends GameElement{

    // TODO: why is this useful??
    private static int closestSafeDist = 100;

    private static int points = 0;

    Star(double x, double y) {
        super(x, y, closestSafeDist);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        Image image = new Image("file:/home/zyrch/IdeaProjects/ColorSwitch/Assets/star.png");
        double x = getX();
        double y = getY();
        x -= image.getWidth()/2;
        y -= image.getHeight()/2;
        graphicsContext.drawImage(image, x, y);
    }

    @Override
    public boolean checkCollision(Ball ball) {
        if (ball.getY() < getY() + 20) {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }

}
