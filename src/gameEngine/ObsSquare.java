package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ObsSquare extends Obstacle{

    private double width;
    private double sideLength;
    private double rotationAngle;
    private ArrayList<Color> colors = new ArrayList<>()
    {{
        add(Color.web("F6DF0E"));
        add(Color.web("8E11FE"));
        add(Color.web("32E1F4"));
        add(Color.web("FD0082"));
    }};

    ObsSquare(double x, double y, double sideLength, double width) {
        super(x, y, sideLength/2 * Math.sqrt(2));
        this.width = width;
        this.sideLength = sideLength;
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        // TODO : make each side below and obove another

        graphicsContext.translate(getX(), getY());
        graphicsContext.rotate(-rotationAngle);

        double topLeftX, topLeftY, topRightX, topRightY;
        double bottomLeftX, bottomRightX, bottomLeftY, bottomRightY;
        topLeftX = - sideLength/2;
        topLeftY = - sideLength/2;
        topRightX = + sideLength/2;
        topRightY = - sideLength/2;
        bottomLeftX = - sideLength/2;
        bottomLeftY = + sideLength/2;

        graphicsContext.setFill(colors.get(0));
        graphicsContext.fillRoundRect(topLeftX, topLeftY, sideLength, width, width, width);

        graphicsContext.setFill(colors.get(1));
        graphicsContext.fillRoundRect(topRightX - width, topRightY, width, sideLength, width, width);

        graphicsContext.setFill(colors.get(2));
        graphicsContext.fillRoundRect(bottomLeftX, bottomLeftY - width, sideLength, width, width, width);

        graphicsContext.setFill(colors.get(3));
        graphicsContext.fillRoundRect(topLeftX, topLeftY, width, sideLength, width, width);


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
