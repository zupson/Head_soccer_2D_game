package hr.algebra.head_soccer_2d_game.server.model;

import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import lombok.Data;

import java.io.Serializable;

@Data
public class GameCommand implements Serializable {
    private GameState gameState;
}