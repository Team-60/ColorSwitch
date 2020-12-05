package gameEngine;

import java.io.Serializable;

@SuppressWarnings("rawtypes")
public class Player implements Serializable, Comparable { // info. keep

    private static final long serialVersionUID = 2023L;
    public static final String FILE_PATH = "src/data/dataPlayer.ser";

    private String name;
    private int id;
    private int score;
    private int jumps;
    private double distance;
    private String date;

    public Player() {
        this.name = null;
        this.id = -1;
        this.score = 0;
        this.jumps = 0;
        this.distance = 0;
        this.date = null;
    }

    public int getScore() {
        return score;
    }
    public String getDistance() {
        double fDist = this.distance/1000.0;
        String res = String.format("%.1f", fDist);
        return res + "k";
    } // return formatted string
    public String getJumps() {
        int fJumps = this.jumps;
        return (fJumps >= 1000) ? String.format("%.1f", fJumps / 1000.0) : Integer.toString(fJumps);
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return this.name;
    }
    public String getDate() {
        return this.date;
    }

    public void setId(int _id) {
        this.id = _id;
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
    public void setDate(String _date) {
        this.date = _date;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) { // for leaderboard
        Player p = (Player) o;
        return Integer.compare(p.getScore(), this.score);
    }

    @Override
    public String toString() {
        String ps = "* Name: " + this.name + "\n";
        String is = "* Game Id: " + this.id + "\n";
        String ss = "* Score: " + this.score + "\n";
        String js = "* Jumps: " + this.jumps + "\n";
        String dis = "* Dist.: " + this.getDistance() + " px \n";
        String ds = "* Date: " + this.date + "\n";
        return ps + is + ss + js + dis + ds;
    }

}
