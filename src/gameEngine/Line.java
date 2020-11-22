package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Line extends Obstacle {

    private static final double closestSafeDist = 100;     // TODO: not final
    private final double length;                                  // TODO: can we make it static?
    private double startingPoint = 0;                      // a reference point to assess rotation
    private final double width = GamePlay.WIDTH/4;
    Renderer renderer;
    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    private ArrayList<Color> colors = new ArrayList<>()
    {{
        add(Color.web("F6DF0E"));
        add(Color.web("8E11FE"));
        add(Color.web("32E1F4"));
        add(Color.web("FD0082"));
    }};


    Line(double x, double y, double length) {
        // x can be anything doesn't matter
        // y is the higher part of line
        // safeDist is zero as y is the higher part
        super(x, y, length/2);
        this.length = length;
        renderer = null;
        setTranslationSpeed(90);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        // TODO: give every obstacle a graphicsContext in Constructor
        if (renderer == null) {
            renderer = new Renderer(graphicsContext);
        }
        double Left = getStartingPoint();
        for (int i = 0; i < 4; ++i) {
            renderer.drawFoldingRed(Left, getY() - length/2, width, length, colors.get(i));
            Left += width;
            Left %= GamePlay.WIDTH;
        }
    }



    @Override
    public boolean checkCollision(Ball ball) {

        double top = ball.getY() - ball.getRadius();
        double bottom = ball.getY() + ball.getRadius();
        double Left = getStartingPoint();
        if ((top < getY() + length/2 && top > getY() - length/2) || (bottom < getY() + length/2 && bottom > getY() - length/2)) {
            for (int i = 0; i < 4; ++i) {
                if (ball.getX() > Left && ball.getX() < Left + width) {
                    return checkNotEqual(colors.get(i), ball.getColor());
                }
                Left += width;
            }
        }

        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public Color getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }

}
