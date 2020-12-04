package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class Star extends GameElement {

    // TODO: why is this useful??
    private static final int closestSafeDist = 100;
    private static final int points = 0;

    private transient final Image image = new Image(new File("src/assets/gameplay/star.png").toURI().toString());

    Star(double x, double y) {
        super(x, y, closestSafeDist);
        audioClipPath = "src/assets/music/gameplay/star.wav";
        this.loadAssets(); // need to ensure that audio clip path has been set
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        double x = getX();
        double y = getY();
        x -= image.getWidth()/2;
        y -= image.getHeight()/2;
        graphicsContext.drawImage(image, x, y);
    }

    @Override
    public boolean checkCollision(Ball ball) {
        return ball.getY() < getY() + 20;
    }

    @Override
    public void destroy() {

    }

}
