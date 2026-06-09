package hr.algebra.head_soccer_2d_game.server.model;

import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import lombok.Getter;
import lombok.Setter;
import org.dyn4j.dynamics.Body;
@Getter
public class Goal extends GameObject {
    private final Side side;
    @Setter
    private int score;

    public Goal(Body body, double width, double height, Side side) {
        super(body, width, height);
        this.side = side;
    }
    public void addScore() {
        this.score++;
    }
}