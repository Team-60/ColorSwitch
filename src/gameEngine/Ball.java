package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.Serializable;

public class Ball implements Serializable {

    private static final long serialVersionUID = 2022L;

    private final double jumpSpeed = -450;
    private final double gravity = 1500;
    private final double radius = 10;
    private final double x = 225;
    private final double midLine = 350;

    private double handPosition;
    private double y;
    private double velocity;
    private String color;

    private transient final GraphicsContext graphicsContext;
    private transient final Image hand = new Image(new File("src/assets/gameplay/hand_s.png").toURI().toString());
    private transient final AudioClip audioClip = new AudioClip(new File("src/assets/music/gameplay/jump.wav").toURI().toString());

    Ball(GraphicsContext graphicsContext) {
        y = 600;
        velocity = 0;
        handPosition = 570;
        color = "000000";
        this.graphicsContext = graphicsContext;
    }

    public void refresh() {
        graphicsContext.setFill(Color.web(color));
        graphicsContext.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        if (handPosition < 900) {
            graphicsContext.drawImage(hand, x - 17, handPosition + 15);
        }
    }

    public void jump() {
        audioClip.play(); // TEMPORARY, TODO: MUSIC DISABLE
        velocity = jumpSpeed;
    }

    public double move(double time, Player player) {
        // TODO : tweaks to make jump animation good
        double distance = velocity * time + (gravity * time * time)/2;
        velocity += gravity * time;
        y += distance;
        player.incDist(Math.abs(distance)); // inc player distance

        if (y > handPosition) {
            y = handPosition;
            velocity = 0;
        }
        if (y < midLine) {
            double te = y;
            double offset = midLine - te;
            y = midLine + offset * 0.0;
            handPosition += offset;
            return offset * 1.0;
        }
        return 0;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }
}
