package hr.algebra.head_socker_2d_game.model.entities;

import org.dyn4j.dynamics.Body;

public class Ball extends GameObject {

    private Side lastTouchedBy;

    public Ball(Body body, double width, double height) {
        super(body, width, height);
    }

    public Side getLastTouchedBy() {
        return lastTouchedBy;
    }

    public void setLastTouchedBy(Side side) {
        this.lastTouchedBy = side;
    }
}
