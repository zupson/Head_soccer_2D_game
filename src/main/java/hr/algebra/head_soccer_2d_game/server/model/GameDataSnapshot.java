package hr.algebra.head_soccer_2d_game.server.model;

import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
@RequiredArgsConstructor
@Getter
public class GameDataSnapshot implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int playerOneScore;
    private final int playerTwoScore;
    private final double playerOneX, playerOneY;
    private final double playerTwoX, playerTwoY;
    private final double ballX, ballY;
    private final GameState gameState;
    private final double remainingTime;
}