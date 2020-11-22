package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Triangle extends Obstacle{

    private double width;
    private double sideLength;
    private double rotationAngle;
    private ArrayList<Color> colors = new ArrayList<>()
    {{
        add(Color.web("F6DF0E"));
        add(Color.web("8E11FE"));
        add(Color.web("32E1F4"));
    }};

    Triangle(double x, double y, double sideLength, double width) {
        super(x, y, sideLength / Math.sqrt(3));
        this.width = width;
        this.sideLength = sideLength;
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        // TODO : make each side below and obove another

        graphicsContext.translate(getX(), getY());
        graphicsContext.rotate(-rotationAngle);

        double topX, topY;
        double bottomLeftX, bottomLeftY;
        topX = 0;
        topY = -sideLength/Math.sqrt(3);
        bottomLeftX = - sideLength/2;
        bottomLeftY = + sideLength/(2 * Math.sqrt(3));

        Renderer renderer = new Renderer(graphicsContext);
        renderer.drawRotatedRoundRect(topX, topY, sideLength, width, width, width, -60, colors.get(0));

        renderer.drawRotatedRoundRect(topX, topY, sideLength, width, width, width, -120, colors.get(1));

        renderer.drawRotatedRoundRect(bottomLeftX + width/2, bottomLeftY - width/2, sideLength, width, width, width, 0, colors.get(2));

        graphicsContext.rotate(rotationAngle);
        graphicsContext.translate(-getX(), -getY());
    }


    @Override
    public void update(double time) {
        rotationAngle += rotationalSpeed * time;
        while(rotationAngle < 0) {
            rotationAngle += 360;
        }
        rotationAngle %= 360;
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
