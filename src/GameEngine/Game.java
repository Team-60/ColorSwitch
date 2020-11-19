package GameEngine;

import javafx.scene.canvas.GraphicsContext;

public class Game {

    Ball ball;

    Game(GraphicsContext graphicsContext) {
        ball = new Ball(graphicsContext);
    }

    public void checkAndUpdate() {

    }

    public void registerJump() {
        ball.jump();
    }

    public void refreshGameElements() {
        ball.refresh();
    }

}
