package gameEngine.util;

import gameEngine.GamePlay;
import gameEngine.util.Pair;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

import java.util.ArrayList;

public final class Renderer {

    public final static int WIDTH = 450;
    public final static int HEIGHT = 700;
    private static GraphicsContext graphicsContext;

    // to handle gui components
    public static void init(GraphicsContext gc) {
        graphicsContext = gc;
    }

    public static boolean checkInside(ArrayList<Pair> points, double p1, double p2) {
        // points : An arrayList of points in clockwise Order
        // add first again for full circular order
        Pair first = points.get(0);
        points.add(first);
        double areaByPoint = 0;
        double area = 0;
        Pair prevPoint = null;
        for (Pair point : points) {
            if (prevPoint == null) {
                prevPoint = point;
                continue;
            }
            Pair vector = new Pair(prevPoint.first - point.first, prevPoint.second - point.second);
            areaByPoint += crossProduct(vector.first, vector.second, p1 - point.first, p2 - point.second)/2;
            area += crossProduct(vector.first, vector.second, first.first - point.first, first.second - point.second)/2;
            prevPoint = point;
        }
        return Math.abs(areaByPoint - area) < 0.001;
    }

    public static double crossProduct(double x, double y, double a, double b) {
        return Math.abs(x * b - y * a);
    }

    public static double rotateX(double x, double y, double angle) {
        double angleRadians = angle * (Math.PI/180);
        double newX = x * Math.cos(angleRadians) - y * Math.sin(angleRadians);
        return newX;
    }

    public static double rotateY(double x, double y, double angle) {
        double angleRadians = angle * (Math.PI/180);
        double newY = x * Math.sin(angleRadians) + y * Math.cos(angleRadians);
        return newY;
    }

    public static void drawRotatedRoundRect(double pivotX, double pivotY, double x, double y, double width, double height, double arcWidth, double arcHeight, double angle, Color color) {
        graphicsContext.translate(pivotX, pivotY);
        graphicsContext.rotate(-angle);
        graphicsContext.setFill(color);
        graphicsContext.fillRoundRect(x, y, width, height, arcHeight, arcHeight);
        graphicsContext.rotate(angle);
        graphicsContext.translate(-pivotX, -pivotY);
    }

    // draw rectangle that curves around screen
    public static void drawFoldingRed(double topLeftX, double topLeftY, double width, double length, Color color) {

        graphicsContext.setFill(color);
        graphicsContext.fillRect(topLeftX, topLeftY, width, length);
        if (topLeftX + width > WIDTH) {
            graphicsContext.fillRect(0,topLeftY, topLeftX + width - WIDTH, length);
        }

    }

    public static void drawDashedLine(double x, double y, double width, double dashLength, double gap, Color color) {
        graphicsContext.setStroke(color);
        graphicsContext.setLineWidth(width);
        graphicsContext.setLineDashes(2);
        for (double i = x; i < GamePlay.WIDTH; ++i) {
            graphicsContext.strokeLine(i, y, i + dashLength, y);
            i += dashLength;
            i += gap;
        }
    }

    public static void drawArc(double centerX, double centerY, double radius, double innerRadius, Color bgColor, Color strokeColor, int angle) {

        graphicsContext.beginPath();
        graphicsContext.setFill(bgColor);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setLineWidth(1);
        graphicsContext.setFillRule(FillRule.EVEN_ODD);

        double shiftedX = centerX + innerRadius;
        double shiftedY = centerY;
        double tShiftedX, tShiftedY;
        tShiftedX = transformX(shiftedX, shiftedY, centerX, centerY, angle);
        tShiftedY = transformY(shiftedX, shiftedY, centerX, centerY, angle);
        graphicsContext.moveTo(tShiftedX, tShiftedY);

        double pivotX;
        shiftedX = centerX;
        shiftedY = centerY - innerRadius;
        pivotX = centerX + innerRadius;
        double pivotY = centerY - innerRadius;
        double tPivotX = transformX(pivotX, pivotY, centerX, centerY, angle);
        double tPivotY = transformY(pivotX, pivotY, centerX, centerY, angle);

        tShiftedX = transformX(shiftedX, shiftedY, centerX, centerY, angle);
        tShiftedY = transformY(shiftedX, shiftedY, centerX, centerY, angle);
        graphicsContext.arcTo(tPivotX, tPivotY, tShiftedX, tShiftedY, innerRadius);

        shiftedX = centerX + innerRadius;
        shiftedY = centerY;
        tShiftedX = transformX(shiftedX, shiftedY, centerX, centerY, angle);
        tShiftedY = transformY(shiftedX, shiftedY, centerX, centerY, angle);
        graphicsContext.moveTo(tShiftedX, tShiftedY);

        shiftedX = centerX + radius;
        shiftedY = centerY;
        tShiftedX = transformX(shiftedX, shiftedY, centerX, centerY, angle);
        tShiftedY = transformY(shiftedX, shiftedY, centerX, centerY, angle);
        graphicsContext.lineTo(tShiftedX, tShiftedY);

        pivotX = shiftedX;
        pivotY = shiftedY - radius;
        shiftedX = centerX;
        shiftedY = centerY - radius;
        tPivotX = transformX(pivotX, pivotY, centerX, centerY, angle);
        tPivotY = transformY(pivotX, pivotY, centerX, centerY, angle);
        tShiftedX = transformX(shiftedX, shiftedY, centerX, centerY, angle);
        tShiftedY = transformY(shiftedX, shiftedY, centerX, centerY, angle);
        graphicsContext.arcTo(tPivotX, tPivotY, tShiftedX, tShiftedY, radius);

        shiftedX = centerX;
        shiftedY = centerY - innerRadius;
        tShiftedX = transformX(shiftedX, shiftedY, centerX, centerY, angle);
        tShiftedY = transformY(shiftedX, shiftedY, centerX, centerY, angle);
        graphicsContext.lineTo(tShiftedX, tShiftedY);

        shiftedX = centerX + innerRadius;
        shiftedY = centerY;
        tShiftedX = transformX(shiftedX, shiftedY, centerX, centerY, angle);
        tShiftedY = transformY(shiftedX, shiftedY, centerX, centerY, angle);
        graphicsContext.moveTo(tShiftedX, tShiftedY);
        graphicsContext.closePath();
        graphicsContext.stroke();
        graphicsContext.fill();
    }

    private static double transformX(double x, double y, double centerX, double centerY, int angle) {
        double xx = x - centerX;
        double yy = y - centerY;
        if (angle == 0) {
            return xx + centerX;
        }else if (angle == 1) {
            return yy + centerX;
        }else if (angle == 2) {
            return -xx + centerX;
        }
        return -yy + centerX;
    }

    private static double transformY(double x, double y, double centerX, double centerY, int angle) {
        double xx = x - centerX;
        double yy = y - centerY;
        if (angle == 0) {
            return yy + centerY;
        }else if (angle == 1) {
            return -xx + centerY;
        }else if (angle == 2) {
            return -yy + centerY;
        }
        return xx + centerY;
    }
}
