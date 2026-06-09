package hr.algebra.head_soccer_2d_game.server.model;

import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GameCommand implements Serializable {
    private GameState gameState;
}