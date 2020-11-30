package gameEngine;

import java.io.Serializable;

public class Player implements Serializable { // info. keep

    public static final String FILE_PATH = "src/data/dataGame.ser";
    private String name;
    private int id;
    private int score;
    private int jumps;
    private double distance;

    public Player() {
        this.name = null;
        this.id = -1;
        this.score = 0;
        this.jumps = 0;
        this.distance = 0;
    }

    public int getScore() {
        return score;
    }
    public double getDistance() {
        return distance;
    }
    public int getJumps() {
        return jumps;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void incScore() {
        this.score ++;
    }
    public void incJumps() {
        this.jumps ++;
    }
    public void incDist(double dist) {
        this.distance += dist;
    }
}
