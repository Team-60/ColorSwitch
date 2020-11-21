package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Line extends Obstacle {

    private static final double closestSafeDist = 100;     // TODO: not final
    private final double length;                                  // TODO: can we make it static?
    private double startingPoint = 0;                      // a reference point to assess rotation
    private final double width = GameApp.WIDTH/4;
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
        super(x, y, 0);
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
        double topLeft = getStartingPoint();
        for (int i = 0; i < 4; ++i) {
            renderer.drawFoldingRed(topLeft, getY(), width, length, colors.get(i));
            topLeft += width;
            topLeft %= GameApp.WIDTH;
        }
    }



    @Override
    public boolean checkCollision(Ball ball) {
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