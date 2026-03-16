package hr.algebra.head_soccer_2d_game.server.model.entities;

import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import org.dyn4j.dynamics.Body;

public class Player extends GameObject {
    private final Side side;
    private String playerName;

    public Player(Body body, double width, double height, Side side) {
        super(body, width, height);
        this.side = side;
    }
    public Side getSide() {
        return side;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}