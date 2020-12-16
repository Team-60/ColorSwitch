package gameEngine.bubbles;

import gameEngine.GamePlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Bubbles {

    ArrayList<Bubble> bubbles;
    // TODO : optimal ratio between number of bubbles in size of each bubbles is yet to be achieved
    // TODO : color combinations are not final
    private final int numberOfBubbles = 30;
    private final GraphicsContext graphicsContext;

    private final ArrayList<Color> colors = new ArrayList<>()
    {{
        add(Color.web("F6DF0E"));
        add(Color.web("8E11FE"));
        add(Color.web("32E1F4"));
        add(Color.web("FD0082"));
    }};

    public Bubbles(GraphicsContext graphicsContext) {
        bubbles = new ArrayList<>();
        this.graphicsContext = graphicsContext;
        generate();
    }

    public Color getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }

    private void generate() {
        Random random = new Random();
        double radius, x, y, Vx, Vy;
        int dir;
        for (int i = 0; i < numberOfBubbles; ++i) {
            radius = random.nextInt(60) + 20;
            x = (random.nextDouble() * 1000) % (GamePlay.WIDTH - radius) + radius;
            y = (random.nextDouble() * 1000) % (GamePlay.HEIGHT - radius) + radius;
            Vx = random.nextInt(150) + 20;
            Vy = random.nextInt(150) + 20;
            dir = random.nextInt(4);
            if (dir == 1) {
                Vx *= -1;
            } else if (dir == 2) {
                Vx *= -1;
                Vy *= -1;
            } else if (dir == 3) {
                Vy *= -1;
            }
            Bubble bubble = new Bubble(x, y, radius, Vx, Vy, getRandomColor(), graphicsContext);
            bubbles.add(bubble);
        }
    }

    public void clip() {

        graphicsContext.beginPath();
        for (Bubble bubble : bubbles) {
            bubble.clipBubble();
        }
    }

    public void move(double time) {
        for (Bubble bubble : bubbles) {
            bubble.move(time);
        }
    }

    public void color() {
        for (Bubble bubble : bubbles)
            bubble.color();
    }

    public void debug() {
        for (Bubble bubble : bubbles) bubble.debug();
    }

}
