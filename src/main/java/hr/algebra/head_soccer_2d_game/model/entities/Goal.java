package hr.algebra.head_soccer_2d_game.model.entities;

import hr.algebra.head_soccer_2d_game.model.entities.enums.Side;
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
