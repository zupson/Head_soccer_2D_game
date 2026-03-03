package hr.algebra.head_soccer_2d_game.model.entities;

import hr.algebra.head_soccer_2d_game.model.entities.enums.GameState;

import java.io.Serializable;

public class GameDataSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    public int player1Score;
    public int player2Score;
    public double player1X, player1Y;
    public double player2X, player2Y;
    public double ballX, ballY;
    public GameState gameState;
    public double remainingTime;

    public GameDataSnapshot(int player1Score, int player2Score, double player1X, double player1Y, double player2X,
                            double player2Y, double ballX, double ballY, GameState gameState, double elapsedTime) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.player1X = player1X;
        this.player1Y = player1Y;
        this.player2X = player2X;
        this.player2Y = player2Y;
        this.ballX = ballX;
        this.ballY = ballY;
        this.gameState = gameState;
        this.remainingTime = elapsedTime;
    }
}
