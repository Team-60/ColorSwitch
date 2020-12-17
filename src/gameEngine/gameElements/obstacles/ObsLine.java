package gameEngine.gameElements.obstacles;

import gameEngine.Ball;
import gameEngine.GamePlay;
import gameEngine.Renderer;
import gameEngine.gameElements.Star;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ObsLine extends Obstacle {

    private final double length;
    private double startingPoint = 0;                      // a reference point to assess rotation
    private final double width = GamePlay.WIDTH/4;

    private ArrayList<String> colors = new ArrayList<>()
    {{
        add("F6DF0E");
        add("8E11FE");
        add("32E1F4");
        add("FD0082");
    }};


    public ObsLine(double x, double y, double length) {
        // x can be anything doesn't matter
        // y is the higher part of line
        // safeDist is zero as y is the higher part
        super(x, y, y - length, y + length);
        this.length = length;
        translationSpeed = 180;
        closestStar = 40;
        star = new Star(x, y - 40);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        if (star != null) {
            star.refresh(graphicsContext);
        }
        double Left = startingPoint;
        for (int i = 0; i < 4; ++i) {
            Renderer.drawFoldingRed(Left, getY() - length/2, width, length, Color.web(colors.get(i)));
            Left += width;
            Left %= GamePlay.WIDTH;
        }
    }

    @Override
    public boolean checkCollision(Ball ball) {
        double top = ball.getY() - ball.getRadius();
        double bottom = ball.getY() + ball.getRadius();
        double Left = startingPoint;
        if ((top < getY() + length/2 && bottom > getY() - length/2)) {
            boolean isCollided = false;
            if (ball.getX() + ball.getRadius() > Left && ball.getX() - ball.getRadius() < Left + width) {
                isCollided = checkNotEqual(ball.getColor(), colors.get(0));
            }
            Left += width;
            Left %= GamePlay.WIDTH;
            if (ball.getX() + ball.getRadius() > Left && ball.getX() - ball.getRadius() < Left + width) {
                isCollided |= checkNotEqual(ball.getColor(), colors.get(1));
            }
            Left += width;
            Left %= GamePlay.WIDTH;
            if (ball.getX() + ball.getRadius() > Left && ball.getX() - ball.getRadius() < Left + width) {
                isCollided |= checkNotEqual(ball.getColor(), colors.get(2));
            }
            Left += width;
            Left %= GamePlay.WIDTH;
            if (ball.getX() + ball.getRadius() > Left && ball.getX() - ball.getRadius() < Left + width) {
                isCollided |= checkNotEqual(ball.getColor(), colors.get(3));
            }
            return isCollided;
        }

        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update(double time) {
        startingPoint += translationSpeed * time;
        startingPoint %= GamePlay.WIDTH;
    }

    @Override
    public String getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }
}
