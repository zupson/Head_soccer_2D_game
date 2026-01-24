package hr.algebra.head_soccer_2d_game.model.entities;

import org.dyn4j.dynamics.Body;

public abstract class GameObject implements Simulable {
    protected final Body body;
    private final double width;
    private final double heigh;

    public GameObject(Body body, double width, double heigh) {
        this.body = body;
        this.width = width;
        this.heigh = heigh;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeigh() {
        return heigh;
    }

    @Override
    public Body getBody() {
        return body;
    }
}

