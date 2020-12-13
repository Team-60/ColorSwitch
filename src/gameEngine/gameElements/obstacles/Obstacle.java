package gameEngine.gameElements.obstacles;

import gameEngine.Ball;
import gameEngine.gameElements.GameElement;
import gameEngine.gameElements.Star;

public abstract class Obstacle extends GameElement {

    protected double translationSpeed;
    protected double rotationalSpeed;
    protected double closestStar = 0;
    protected Star star;
    private boolean isCrossed;
    public double getClosestStar() {
        return closestStar;
    }

    public void setTranslationSpeed(double translationSpeed) {
        this.translationSpeed = translationSpeed;
    }

    public void setRotationalSpeed(double rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed;
    }

    public Obstacle(double x, double y, double topY, double bottomY) {
        super(x, y, topY, bottomY);
        rotationalSpeed = 90;
        audioClipPath = "src/assets/music/gameplay/dead.wav";
        this.loadAssets(); // need to ensure that audio clip path has been set
        isCrossed = false;
    }

    public boolean isCrossed() {
        return isCrossed;
    }

    public void setCrossed() {
        isCrossed = true;
    }

    public abstract String getRandomColor();

    public abstract void update(double time);

    public boolean checkNotEqual(String a, String b) {
        return !a.equals(b);
    }

    public boolean checkCollisionStar(Ball ball) {
        if (star == null) {
            return false;
        }
        boolean isCollided = star.checkCollision(ball);
        if (isCollided) {
            star.playSound();
            destroyStar();
        }
        return isCollided;
    }

    @Override
    public void applyOffset(double offset) { // all concatenated obstacles, need to call super
        if (star != null) {
            star.applyOffset(offset);
        }
        super.applyOffset(offset);
    }

    public void destroyStar() {
        star = null;
    }

    public int getMaxCount() {
        return 1;
    }

}
