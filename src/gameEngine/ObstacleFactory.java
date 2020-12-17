package gameEngine;

import gameEngine.gameElements.obstacles.*;

import java.util.Random;

public class ObstacleFactory {

    public static Obstacle obstacle(int score, double x, double y) {
        int level = (score + 1) / 2;
        if (level > 24) level = 24;

        int difficulty;
        Random random = new Random();
        if (level > 3) {
            difficulty = -random.nextInt(4) + level;
        } else difficulty = random.nextInt(3) + 1;

        System.out.println(ObstacleFactory.class.toString() + " difficulty: " + difficulty);
        Obstacle obstacle;
        difficulty = 22;
        switch (difficulty) {
            case 1:
                return (new ObsCircle(x, y - 90, 90, 15));
            case 2:
                return new ObsDoubleCircle(x, y - 115, 90, 115, 15);
            case 3:
                // smaller square
                return new ObsSquare(x, y - 70 * Math.sqrt(2), 140, 13);
            case 4:
                return new ObsLine(x, y - 7.5, 15);
            case 5:
                return new ObsTriangle(x, y - 200 / Math.sqrt(3), 200, 15);
            case 6:
                // faster circle with same radius
                obstacle = new ObsCircle(x, y - 90, 90, 15);
                obstacle.setRotationalSpeed(150);
                return obstacle;
            case 7:
                // faster square with same size
                obstacle = new ObsSquare(x, y - 85 * Math.sqrt(2), 170, 15);
                obstacle.setRotationalSpeed(150);
                return obstacle;
            case 8:
                // faster double circle with same size
                obstacle = new ObsDoubleCircle(x, y - 115, 90, 115, 15);
                obstacle.setRotationalSpeed(150);
                return obstacle;
            case 9:
                // faster triangle with same size
                obstacle = new ObsTriangle(x, y - 200 / Math.sqrt(3), 200, 15);
                obstacle.setRotationalSpeed(130);
                return obstacle;
            case 10:
                // smaller circle
                return new ObsCircle(x, y - 75, 75, 10);
            case 11:
                // smaller double circle
                return new ObsDoubleCircle(x, y - 90, 70, 90, 13);
            case 12:
                ObsLine obsLine = new ObsLine(x, y - 7.5, 15);
                obsLine.setTranslationSpeed(150);
                return obsLine;
            case 13:
                // smaller triangle
                return new ObsTriangle(x, y - 190 / Math.sqrt(3), 190, 13);
            case 14:
                // super slow circle
                obstacle = new ObsCircle(x, y - 65, 65, 8);
                obstacle.setRotationalSpeed(50);
                return obstacle;
            case 15:
                ObsCircle circle = new ObsCircle(x, y - 90, 90, 15);
                return new ObsConsecutiveCircles(circle, 3);
            case 16:
                circle = new ObsCircle(x, y - 90, 90, 15);
                return new ObsConsecutiveCircles(circle, 4);
            case 17:
                return new ObsSquareCircle(x, y - 85 * Math.sqrt(2), 200, 15);
            case 18:
                // smaller consecutive
                circle = new ObsCircle(x, y - 75, 75, 10);
                return new ObsConsecutiveCircles(circle, 3);
            case 19:
                return new ObsCircleTriangle(x, y - 300 / Math.sqrt(3), 300, 15);
            case 20:
                ObsCircle doubleCircle = new ObsDoubleCircle(x, y - 115, 90, 115, 15);
                return new ObsConsecutiveCircles(doubleCircle, 3);
            case 21:
                ObsCircle obsTripleCircle = new ObsTripleCircle(x, y - 140, 90, 115, 140, 15);
                return new ObsConsecutiveCircles(obsTripleCircle, 3);
            case 22:
                // faster and smaller consecutive circle
                circle = new ObsCircle(x, y - 75, 75, 10);
                obstacle = new ObsConsecutiveCircles(circle, 4);
                obstacle.setRotationalSpeed(150);
                return obstacle;
            case 23:
                doubleCircle = new ObsDoubleCircle(x, y - 115, 90, 115, 15);
                return new ObsConsecutiveCircles(doubleCircle, 4);
            default:
                obsTripleCircle = new ObsTripleCircle(x, y - 140, 90, 115, 140, 15);
                return new ObsConsecutiveCircles(obsTripleCircle, 5);
        }
    }

}
