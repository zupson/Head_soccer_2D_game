package hr.algebra.head_soccer_2d_game.server.model.entities;

import hr.algebra.head_soccer_2d_game.shared.enums.GameState;

import java.io.Serializable;

public class GameCommand implements Serializable {
    private GameState gameState;

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}