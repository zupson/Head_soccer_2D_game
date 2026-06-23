package hr.algebra.head_soccer_2d_game.server.game;

import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.threads.GameLoopThread;
import hr.algebra.head_soccer_2d_game.shared.annotations.BusinessLogic;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.event.GameOverListener;
import hr.algebra.head_soccer_2d_game.shared.event.PlayerInputApplier;
import hr.algebra.head_soccer_2d_game.shared.utilities.GameDataUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@BusinessLogic(description = "Main game loop execution")
@RequiredArgsConstructor
public class GameLoop {
    private final GameDataListener gameDataListener;
    private final GameOverListener gameOverListener;

    private long lastTime = System.nanoTime();
    private static final double GAME_DURATION = 60;
    @Setter @Getter private double remainingTime = GAME_DURATION;

    private final GameObjectManager gameObjectManager;
    private final GamePhysicManager gamePhysicManager;
    private final GameStateManager gameStateManager;
    private final PlayerInputApplier playerInputApplier;

    public void startGameLoop() {
        new GameLoopThread(this).start();
    }

    public void timerTick() {
        if (!gameStateManager.isRunning()){
            lastTime = System.nanoTime();
            GameDataSnapshot gameDataSnapshot = GameDataUtils.saveCurrentGameData(this);
            gameDataListener.onGameDataChanged(gameDataSnapshot);
            return;
        }
        double deltaTime = calculateDeltaTime();

        remainingTime -= deltaTime;

        if (remainingTime <= 0) {
            handleGameOver();
            return;
        }
        updateGame(deltaTime);
    }

    private double calculateDeltaTime() {
        long now = System.nanoTime();
        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;
        return deltaTime;
    }

    private void handleGameOver() {
        remainingTime = 0;
        gameStateManager.setCurrentState(GameState.GAME_OVER);
        GameDataSnapshot snapshot = GameDataUtils.saveCurrentGameData(this);
        gameDataListener.onGameDataChanged(snapshot);
        gameOverListener.onGameOver();
    }

    private void updateGame(double deltaTime) {
        playerInputApplier.applyPlayerInputs();
        gamePhysicManager.update(deltaTime);
        GameDataSnapshot gameDataSnapshot = GameDataUtils.saveCurrentGameData(this);

        handleGoalIfScored();
        gameDataListener.onGameDataChanged(gameDataSnapshot);
    }

    private void handleGoalIfScored() {
        if (gameStateManager.isScoredGoalFlag()) {
            resetAfterGoal();
            gameStateManager.setScoredGoalFlag(false);
        }
    }

    public void resetTimer() {
        remainingTime = GAME_DURATION;
        lastTime = System.nanoTime();
    }

    public void resetAfterGoal() {
        gameObjectManager.setPlayersStartPositions();
        gameObjectManager.setBallStartPosition();
    }
}