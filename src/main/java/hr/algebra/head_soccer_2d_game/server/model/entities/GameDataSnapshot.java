package hr.algebra.head_soccer_2d_game.server.model.entities;

import hr.algebra.head_soccer_2d_game.shared.enums.GameState;

import java.io.Serial;
import java.io.Serializable;

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
    private final boolean playerReady = false;

    public GameDataSnapshot(int playerOneScore, int playerTwoScore, double playerOneX, double playerOneY, double playerTwoX,
                            double playerTwoY, double ballX, double ballY, GameState gameState, double elapsedTime) {
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
        this.playerOneX = playerOneX;
        this.playerOneY = playerOneY;
        this.playerTwoX = playerTwoX;
        this.playerTwoY = playerTwoY;
        this.ballX = ballX;
        this.ballY = ballY;
        this.gameState = gameState;
        this.remainingTime = elapsedTime;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public double getPlayerOneX() {
        return playerOneX;
    }

    public double getPlayerOneY() {
        return playerOneY;
    }

    public double getPlayerTwoX() {
        return playerTwoX;
    }

    public double getPlayerTwoY() {
        return playerTwoY;
    }

    public double getBallX() {
        return ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public GameState getGameState() {
        return gameState;
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public boolean isPlayerReady() {
        return playerReady;
    }
}
