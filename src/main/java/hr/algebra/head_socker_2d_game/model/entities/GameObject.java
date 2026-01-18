package hr.algebra.head_socker_2d_game.model.entities;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

public abstract class GameObject {

    protected final Body body;//protected, samo child kalse od ove klase moze koriti ovaj prop
    private final double width;
    private final double height;

    public GameObject(Body body, double width, double height) {
        this.body = body;
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getTransform().getTranslation().copy();
    }

    public double getRotation() {
        return body.getTransform().getRotation().toRadians();
    }
}