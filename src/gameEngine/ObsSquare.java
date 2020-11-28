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

        graphicsContext.setFill(colors.get(3));
        graphicsContext.fillRoundRect(topLeftX, topLeftY, width, sideLength, width, width);

        graphicsContext.setFill(colors.get(0));
        graphicsContext.fillRoundRect(topLeftX, topLeftY, sideLength/2, width, width, width);


        graphicsContext.rotate(rotationAngle);
        graphicsContext.translate(-getX(), -getY());
    }

    private boolean checkInside(double a1, double a2, double b1, double b2, double c1, double c2, double d1, double d2, double p1, double p2) {

        // a -- b
        // |    |
        // c -- d
        double vector1x = a1 - b1;
        double vector1y = a2 - b2;

        double vector2x = a1 - c1;
        double vector2y = a2 - c2;

        double vector3x = c1 - d1;
        double vector3y = c2 - d2;

        double vector4x = d1 - b1;
        double vector4y = d2 - b2;

        double area = crossProduct(vector1x, vector1y, vector2x, vector2y);

        double areaByPoint = 0;
        areaByPoint += crossProduct(vector1x, vector1y, a1 - p1, a2 - p2);
        areaByPoint += crossProduct(vector2x, vector2y, a1 - p1, a2 - p2);
        areaByPoint += crossProduct(vector3x, vector3y, c1 - p1, c2 - p2);
        areaByPoint += crossProduct(vector4x, vector4y, d1 - p1, d2 - p2);

        return Math.abs(areaByPoint - area) < 0.0001;
    }

    private double crossProduct(double x, double y, double a, double b) {
        return Math.abs(x * b - y * a);
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

//        double leftX, leftY, left

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
