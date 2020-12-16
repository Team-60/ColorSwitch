package gameEngine.customExceptions;

import gameEngine.App;

public class InsufficientStarsException extends Exception {
    public InsufficientStarsException(int have) {
        super("Need: " + App.REVIVAL_STARS + " Have: " + have);
    }
    @Override
    public String toString() {
        return "InsufficientStarsException";
    }
}
