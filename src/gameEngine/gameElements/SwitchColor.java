package gameEngine.gameElements;

import gameEngine.Ball;
import gameEngine.gameElements.obstacles.Obstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class SwitchColor extends GameElement {

    private Obstacle obstacle;

    private static final int closestSafeDist = 100;
    private static int points = 0;

    private static final Image image = new Image(new File("src/assets/gameplay/color_switcher_s.png").toURI().toString());

    public SwitchColor(double x, double y) {
        super(x, y, y, y);
        audioClipPath = "src/assets/music/gameplay/colorSwitch.wav";
        this.loadAssets(); // need to ensure that audio clip path has been set
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        double x = getX();
        double y = getY();
        x -= image.getWidth()/2;
        y -= image.getHeight()/2;
        graphicsContext.drawImage(image, x, y);
    }

    @Override
    public boolean checkCollision(Ball ball) {
        if (ball.getY() < getY() + 18) {
            String prev = ball.getColor();
            // If the number of colors in ball is small enough (== 1 or 2) infinite loop may occur
            String color = prev;
            while (color.equals(prev)) {
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

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

}
