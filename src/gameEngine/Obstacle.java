package gameEngine;

public abstract class Obstacle extends GameElement {

    protected double translationSpeed;
    protected double rotationalSpeed;
    protected double closestStar = 0;

    public double getClosestStar() {
        return closestStar;
    }

    Obstacle(double x, double y, double safeDist) {
        super(x, y, safeDist);
        rotationalSpeed = 90;
        audioClipPath = "src/assets/music/gameplay/dead.wav";
        this.loadAssets(); // need to ensure that audio clip path has been set
    }

    public abstract String getRandomColor();

    public abstract void update(double time);

    public boolean checkNotEqual(String a, String b) {
        return !a.equals(b);
    }
}
