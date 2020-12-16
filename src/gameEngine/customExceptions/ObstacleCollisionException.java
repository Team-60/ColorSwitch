package gameEngine.customExceptions;

import gameEngine.gameElements.obstacles.Obstacle;

public class ObstacleCollisionException extends GameOverException {
    public ObstacleCollisionException(Class<? extends Obstacle> T) {
        super("Ball collision with " + T.getName());
    }
    @Override
    public String toString() {
        return "ObstacleCollisionException";
    }
}
