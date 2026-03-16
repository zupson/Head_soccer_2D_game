package hr.algebra.head_soccer_2d_game.server.game;

import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.event.GameOverListener;
import hr.algebra.head_soccer_2d_game.shared.event.PlayerInputApplier;
import hr.algebra.head_soccer_2d_game.shared.utilities.GameDataUtils;

public class GameLoop {
    private final GameDataListener gameDataListener;
    private final GameOverListener gameOverListener;
    private long lastTime = System.nanoTime();
    private static final double GAME_DURATION = 60;
    private double remainingTime = GAME_DURATION;

    private final GameObjectManager gameObjectManager;
    private final GamePhysicManager gamePhysicManager;
    private final GameStateManager gameStateManager;
    private final PlayerInputApplier playerInputApplier;

    public GameLoop(GameDataListener gameDataListener, GameOverListener gameOverListener, GameObjectManager gameObjectManager, GamePhysicManager gamePhysicManager, GameStateManager gameStateManager, PlayerInputApplier playerInputApplier) {
        this.gameDataListener = gameDataListener;
        this.gameOverListener = gameOverListener;
        this.gameObjectManager = gameObjectManager;
        this.gamePhysicManager = gamePhysicManager;
        this.gameStateManager = gameStateManager;
        this.playerInputApplier = playerInputApplier;
    }

    public void startGameLoop() {
        Thread gameThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                timerTick();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void timerTick() {
        long now = System.nanoTime();
        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        if (!gameStateManager.isRunning()) return;
        remainingTime -= deltaTime;

        if (remainingTime <= 0) {
            remainingTime = 0;
            gameStateManager.setCurrentState(GameState.GAME_OVER);
            gameOverListener.onGameOver();
            return;
        }
        playerInputApplier.applyPlayerInputs();
        gamePhysicManager.update(deltaTime);
        GameDataSnapshot gameDataSnapshot = GameDataUtils.saveCurrentGameData(this);

        if (gameStateManager.isScoredGoalFlag()) {
            resetAfterGoal();
            gameStateManager.setScoredGoalFlag(false);
        }
        gameDataListener.onGameDataChanged(gameDataSnapshot);
    }

    public void resetTimer() {
        remainingTime = GAME_DURATION;
        lastTime = System.nanoTime();
    }

    public void resetAfterGoal() {
        gameObjectManager.setPlayersStartPositions();
        gameObjectManager.setBallStartPosition();
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }
}