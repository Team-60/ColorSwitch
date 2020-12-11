package gameEngine;

public abstract class Obstacle extends GameElement {

    protected double translationSpeed;
    protected double rotationalSpeed;
    protected double closestStar = 0;
    protected Star star;
    public double getClosestStar() {
        return closestStar;
    }

    public void setTranslationSpeed(double translationSpeed) {
        this.translationSpeed = translationSpeed;
    }

    public void setRotationalSpeed(double rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed;
    }

    Obstacle(double x, double y, double topY, double bottomY) {
        super(x, y, topY, bottomY);
        rotationalSpeed = 90;
        audioClipPath = "src/assets/music/gameplay/dead.wav";
        this.loadAssets(); // need to ensure that audio clip path has been set
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
            destroyStar();
        }
        return isCollided;
    }
    @Override
    void applyOffset(double offset) {
        if (star != null) {
            star.applyOffset(offset);
        }
        y += offset;
    }

    public void destroyStar() {
        star = null;
    }

}
