package hr.algebra.head_socker_2d_game.model.entities;

import org.dyn4j.dynamics.Body;

public class Goal extends GameObject {

    private final Side side;

    public Goal(Body body, double width, double height, Side side) {
        super(body, width, height);
        this.side = side;
    }
    public Side getSide() {
        return side;
    }
}
