package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

public class Renderer {

    // to handle gui components
    GraphicsContext graphicsContext;

    Renderer(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
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
