package gameEngine.swarm;

import gameEngine.Ball;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Random;

public class Swarm {

    ArrayList<Particle> particles;
    private final int numberOfParticles = 41;
    private final GraphicsContext graphicsContext;

    private final ArrayList<String> colors = new ArrayList<>()
    {{
        add("F6DF0E");
        add("8E11FE");
        add("32E1F4");
        add("FD0082");
    }};

    public Swarm(GraphicsContext graphicsContext) {
        particles = new ArrayList<>();
        this.graphicsContext = graphicsContext;
    }

    public void explode(Ball ball) {
        Random random = new Random();
        int radius;
        double x = ball.getX(), y = ball.getY();
        double Vx, Vy;
        int va;
        String color;
        for (int i = 0; i < numberOfParticles - 1; ++i) {
            radius = random.nextInt(4) + 2;
            Vx = random.nextInt(500);
            Vy = random.nextInt(1200);
            color = getRandomColor();
            va = i/(numberOfParticles/4);
            if (va == 1) {
                Vx *= -1;
            }else if (va == 2) {
                Vx *= -1;
                Vy *= -1;
            }else {
                Vy *= -1;
            }
            Particle particle = new Particle(x, y, radius, Vx, Vy, graphicsContext, color);
            particles.add(particle);
        }
    }

    public void update(double time) {
        for (Particle particle : particles) {
            particle.move(time);
        }
    }

    public void refresh() {
        for (Particle particle : particles) {
            particle.refresh();
        }
    }

    public String getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }
}
