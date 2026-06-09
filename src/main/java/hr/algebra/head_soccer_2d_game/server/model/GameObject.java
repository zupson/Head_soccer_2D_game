package hr.algebra.head_soccer_2d_game.server.model;

import lombok.RequiredArgsConstructor;
import org.dyn4j.dynamics.Body;

@RequiredArgsConstructor
public abstract class GameObject implements Simulable {
    protected final Body body;
    private final double width;
    private final double heigh;

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