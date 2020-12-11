package gameEngine;

import javafx.scene.PointLight;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ObsSquare extends Obstacle{

    private double width;
    private double sideLength;
    private double rotationAngle;
    private ArrayList<String> colors = new ArrayList<>()
    {{
        add("F6DF0E"); // yellow
        add("8E11FE"); // purple
        add("32E1F4"); // cyan
        add("FD0082"); // pink
    }};

    ObsSquare(double x, double y, double sideLength, double width) {
        super(x, y, y - sideLength/2 * Math.sqrt(2), y + sideLength/2 * Math.sqrt(2));
        this.width = width;
        this.sideLength = sideLength;
        star = new Star(x, y);
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        if (star != null) {
            star.refresh(graphicsContext);
        }
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

        graphicsContext.setFill(Color.web(colors.get(0)));
        graphicsContext.fillRoundRect(topLeftX, topLeftY, sideLength, width, width, width);

        graphicsContext.setFill(Color.web(colors.get(1)));
        graphicsContext.fillRoundRect(topRightX - width, topRightY, width, sideLength, width, width);

        graphicsContext.setFill(Color.web(colors.get(2)));
        graphicsContext.fillRoundRect(bottomLeftX, bottomLeftY - width, sideLength, width, width, width);

        graphicsContext.setFill(Color.web(colors.get(3)));
        graphicsContext.fillRoundRect(topLeftX, topLeftY, width, sideLength, width, width);

        graphicsContext.setFill(Color.web(colors.get(0)));
        graphicsContext.fillRoundRect(topLeftX, topLeftY, sideLength/2, width, width, width);


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

        double X = getX();
        double Y = getY();

        double signX = -1, signY = -1;
        ArrayList<Pair> points = new ArrayList<>();

        for (int i = 0; i < 4; ++i) {
            Pair point = new Pair(0, 0);
            point.first = Renderer.rotateX(signX * sideLength/2, signY * sideLength/2, rotationAngle) + X;
            point.second = Renderer.rotateY(signX * sideLength/2, signY * sideLength/2, rotationAngle) + Y;
            points.add(point);
            if (i % 2 == 0) {
                signX *= -1;
            }else {
                signY *= -1;
            }
        }

        boolean insideBigger = false;

        double p1, p2;
        for (int i = 0; i < 360; i+=2) {
            p1 = Renderer.rotateX(0, -ball.getRadius(), i) + ball.getX();
            p2 = Renderer.rotateY(0, -ball.getRadius(), i) + ball.getY();
            insideBigger |= Renderer.checkInside(points, p1, p2);
        }

        points = new ArrayList<>();
        signX = -1; signY = -1;
        for (int i = 0; i < 4; ++i) {
            Pair point = new Pair(0, 0);
            point.first = Renderer.rotateX(signX * (sideLength/2 - width), signY * (sideLength/2 - width), rotationAngle) + X;
            point.second = Renderer.rotateY(signX * (sideLength/2 - width), signY * (sideLength/2 - width), rotationAngle) + Y;
            points.add(point);
            if (i % 2 == 0) {
                signX *= -1;
            }else {
                signY *= -1;
            }
        }

        boolean insideSmaller = true;

        for (int i = 0; i < 360; i+=2) {
            p1 = Renderer.rotateX(0, ball.getRadius(), i) + ball.getX();
            p2 = Renderer.rotateY(0, ball.getRadius(), i) + ball.getY();
            insideSmaller &= Renderer.checkInside(points, p1, p2);
        }
        boolean isCollided = false;
        if (insideBigger && !insideSmaller) {
            // TODO : mutually exclusive ?
            if (rotationAngle < 45 || rotationAngle > 315) {
                if (ball.getY() < getY()) {
                    // above
                    isCollided = checkNotEqual(colors.get(0), ball.getColor());
                }else {
                    isCollided = checkNotEqual(colors.get(2), ball.getColor());
                }
            }
            if (rotationAngle > 45 && rotationAngle < 135) {
                if (ball.getY() < getY()) {
                    // above
                    isCollided = checkNotEqual(colors.get(1), ball.getColor());
                }else {
                    isCollided = checkNotEqual(colors.get(3), ball.getColor());
                }
            }
            if (rotationAngle > 135 && rotationAngle < 225) {
                if (ball.getY() < getY()) {
                    // above
                    isCollided = checkNotEqual(colors.get(2), ball.getColor());
                }else {
                    isCollided = checkNotEqual(colors.get(0), ball.getColor());
                }
            }

            if (rotationAngle > 225 && rotationAngle < 315) {
                if (ball.getY() < getY()) {
                    // above
                    isCollided = checkNotEqual(colors.get(3), ball.getColor());
                }else {
                    isCollided = checkNotEqual(colors.get(1), ball.getColor());
                }
            }
        }
        return isCollided;
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }

}
