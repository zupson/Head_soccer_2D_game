package hr.algebra.head_soccer_2d_game.server.model.entities;

import org.dyn4j.dynamics.Body;

public class Ball extends GameObject {
    public Ball(Body body, double width, double height) {
        super(body, width, height);
    }
}
