package hr.algebra.head_socker_2d_game.model.entities;

import org.dyn4j.dynamics.Body;

public class BoundaryWall extends GameObject {
    public BoundaryWall(Body body, double width, double height) {
        super(body, width, height);
    }
}
