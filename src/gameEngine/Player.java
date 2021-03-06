package gameEngine;

import java.io.Serializable;

public class Player implements Serializable, Comparable<Player> { // info. keep

    private static final long serialVersionUID = 2023L;
    public static final String FILE_PATH = "src/data/dataPlayer.ser";

    private String name;
    private int id;
    private int score;
    private int jumps;
    private double distance;
    private String date;
    private Boolean hasRevived;
    private int scoreBeforeRevival;
    private final boolean isClassicMode;

    public Player() {
        this.name = null;
        this.id = -1;
        this.score = 0;
        this.jumps = 0;
        this.distance = 0;
        this.date = null;
        this.hasRevived = false;
        this.scoreBeforeRevival = 0; // 0 in case no revival has happened
        this.isClassicMode = GamePlay.IS_CLASSIC; // as new player inherits current existing mode from GamePlay
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
        return (fJumps >= 1000) ? (String.format("%.1f", fJumps / 1000.0) + "k") : Integer.toString(fJumps);
    }
    public int getNJumps() {
        return this.jumps;
    }
    public double getNDistance() {
        return this.distance;
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
    public boolean getHasRevived() {
        return this.hasRevived;
    }
    public int getScoreBeforeRevival() {
        return this.scoreBeforeRevival;
    }
    public boolean getIsClassicMode() {
        return this.isClassicMode;
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
    public void setHasRevived() {
        assert (!this.hasRevived);
        this.hasRevived = true;
    }
    public void setScoreBeforeRevival() {
        assert (this.hasRevived);
        this.scoreBeforeRevival = this.score;
    }

    @Override
    public int compareTo(Player p) { // for leaderboard
        int sc = Integer.compare(p.getScore(), this.score);
        int jc = Integer.compare(this.getNJumps(), p.getNJumps());
        int dc = Double.compare(this.getNDistance(), p.getNDistance());
        if (sc == 0)
            if (jc == 0) return dc;
            else return jc;
        else return sc;
    }

    @Override
    public boolean equals(Object p) {
        if (p.getClass() != this.getClass())
            return false;
        Player temp = (Player) p;
        if (this.id == -1) return false; // unidentified player
        return temp.getId() == this.id;
    }

    @Override
    public String toString() {
        String ms = "* Mode: " + (this.isClassicMode ? "Classic\n" : "Bubbles\n");
        String ps = "* Name: " + this.name + "\n";
        String is = "* Game Id: " + this.id + "\n";
        String ss = "* Score: " + this.score + "\n";
        String js = "* Jumps: " + this.jumps + "\n";
        String dis = "* Dist.: " + this.getDistance() + " px \n";
        String ds = "* Date: " + this.date + "\n";
        String rs = "* Revived: " + this.hasRevived + "\n";
        return ms + ps + is + ss + js + dis + ds + rs;
    }

}
