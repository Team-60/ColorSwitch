package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

public class Renderer {

    // TODO: make all methods static

    public final static int WIDTH = 450;
    public final static int HEIGHT = 700;
    // to handle gui components
    GraphicsContext graphicsContext;

    public double rotateX(double x, double y, double angle) {
        double angleRadians = angle * (Math.PI/180);
        double newX = x * Math.cos(angleRadians) - y * Math.sin(angleRadians);
        return newX;
    }


    public double rotateY(double x, double y, double angle) {
        double angleRadians = angle * (Math.PI/180);
        double newY = x * Math.sin(angleRadians) + y * Math.cos(angleRadians);
        return newY;
    }

    public void drawRotatedRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight, double angle, Color color) {
        graphicsContext.translate(x , y);
        graphicsContext.rotate(-angle);
        graphicsContext.setFill(color);
        graphicsContext.fillRoundRect(-height/2, -height/2, width, height, arcHeight, arcHeight);
        graphicsContext.rotate(angle);
        graphicsContext.translate(-x, -y);
    }

    Renderer(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    // draw rectangle that curves around screen
    public void drawFoldingRed(double topLeftX, double topLeftY, double width, double length, Color color) {

        graphicsContext.setFill(color);
        graphicsContext.fillRect(topLeftX, topLeftY, width, length);
        if (topLeftX + width > WIDTH) {
            graphicsContext.fillRect(0,topLeftY, topLeftX + width - WIDTH, length);
        }

    }

    public void drawArc(double centerX, double centerY, double radius, double innerRadius, Color bgColor, Color strokeColor, int angle) {

        graphicsContext.beginPath();
        graphicsContext.setFill(bgColor);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setLineWidth(0);
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

    private double transformX(double x, double y, double centerX, double centerY, int angle) {
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

    private double transformY(double x, double y, double centerX, double centerY, int angle) {
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
