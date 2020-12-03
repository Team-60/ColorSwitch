package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("ALL")
public class ObsTriangle extends Obstacle{
    private final double width;
    private final double sideLength;
    private double rotationAngle;
    private final ArrayList<String> colors = new ArrayList<>()
    {{
        add("F6DF0E");
        add("8E11FE");
        add("32E1F4");
    }};

    ObsTriangle(double x, double y, double sideLength, double width) {
        super(x, y, sideLength / Math.sqrt(3));
        this.width = width;
        this.sideLength = sideLength;
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {

        graphicsContext.translate(getX(), getY());
        graphicsContext.rotate(-rotationAngle);

        double topX, topY;
        double bottomLeftX, bottomLeftY;
        topX = 0;
        topY = -sideLength/Math.sqrt(3);
        bottomLeftX = -sideLength/2;
        bottomLeftY = +sideLength/(2 * Math.sqrt(3));

        Renderer renderer = new Renderer(graphicsContext);
        renderer.drawRotatedRoundRect(topX, topY, 5, 0, sideLength - 10, width, width, width, -60, Color.web(colors.get(0)));

        renderer.drawRotatedRoundRect(topX, topY, 5, -width, sideLength - 10, width, width, width, -120, Color.web(colors.get(1)));

        renderer.drawRotatedRoundRect(bottomLeftX, bottomLeftY, 5, -width, sideLength - 10, width, width, width, 0, Color.web(colors.get(2)));

        renderer.drawRotatedRoundRect(topX, topY, sideLength/2, 0, sideLength/2 - 5, width, width, width, -60, Color.web(colors.get(0)));


        // for debugging
//        double xx = Renderer.rotateX(topX, topY, 120);
//        double yy = Renderer.rotateY(topX, topY, 120);
//        graphicsContext.strokeLine(topX, topY, xx, yy);
//        topX = xx; topY = yy;
//
//        xx = Renderer.rotateX(topX, topY, 120);
//        yy = Renderer.rotateY(topX, topY, 120);
//        graphicsContext.strokeLine(topX, topY, xx, yy);
//
//        topX = xx; topY = yy;
//        xx = Renderer.rotateX(topX, topY, 120);
//        yy = Renderer.rotateY(topX, topY, 120);
//        graphicsContext.strokeLine(topX, topY, xx, yy);
        // end debugging


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

        double X = 0;
        double Y = -sideLength/(Math.sqrt(3));

        double angle = rotationAngle;
        ArrayList<Pair> points = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            Pair point = new Pair(0, 0);
            point.first = Renderer.rotateX(X, Y, angle) + getX();
            point.second = Renderer.rotateY(X, Y, angle) + getY();
            points.add(point);
            angle += 120;
            angle %= 360;
        }

        boolean insideBigger = false;

        double p1, p2;
        for (int i = 0; i < 360; i += 2) {
            p1 = Renderer.rotateX(0, -ball.getRadius(), i) + ball.getX();
            p2 = Renderer.rotateY(0, -ball.getRadius(), i) + ball.getY();
            insideBigger |= Renderer.checkInside(points, p1, p2);
        }

        points = new ArrayList<>();

        X = 0;
        Y = -sideLength/(Math.sqrt(3)) + width * 2;
        for (int i = 0; i < 3; ++i) {
            Pair point = new Pair(0, 0);
            point.first = Renderer.rotateX(X, Y, angle) + getX();
            point.second = Renderer.rotateY(X, Y, angle) + getY();
            points.add(point);
            angle += 120;
            angle %= 360;
        }

        boolean insideSmaller = true;

        for (int i = 0; i < 360; i += 2) {
            p1 = Renderer.rotateX(0, -ball.getRadius(), i) + ball.getX();
            p2 = Renderer.rotateY(0, -ball.getRadius(), i) + ball.getY();
            insideSmaller &= Renderer.checkInside(points, p1, p2);
        }
        boolean isCollided = false;
        if (insideBigger && !insideSmaller) {
            if (ball.getY() < getY()) {
                if (rotationAngle > 0 && rotationAngle < 120) {
                    isCollided = checkNotEqual(colors.get(0), ball.getColor());
                }
                if (rotationAngle > 120 && rotationAngle < 240) {
                    isCollided |= checkNotEqual(colors.get(2), ball.getColor());
                }
                if (rotationAngle > 240 && rotationAngle < 360){
                    isCollided |= checkNotEqual(colors.get(1), ball.getColor());
                }
            } else {
                if (rotationAngle < 60 || rotationAngle > 300) {
                    isCollided = checkNotEqual(colors.get(2), ball.getColor());
                }
                if (rotationAngle > 60 && rotationAngle < 180) {
                    isCollided |= checkNotEqual(colors.get(1), ball.getColor());
                }
                if (rotationAngle > 180 && rotationAngle < 300){
                    isCollided |= checkNotEqual(colors.get(0), ball.getColor());
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
