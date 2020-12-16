package gameEngine.customExceptions;

public class FallOutException extends GameOverException {
    public FallOutException() {
        super("Ball fell out of the screen");
    }
    @Override
    public String toString() {
        return "FallOutException";
    }
}
