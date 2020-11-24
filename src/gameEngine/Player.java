package gameEngine;

public class Player { // info. keep

    private String name;
    private int id;
    private int score;

    public Player() {
        this.name = null;
        this.id = -1;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incScore() {
        this.score ++;
    }
}
