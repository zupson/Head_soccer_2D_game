package hr.algebra.head_soccer_2d_game.game.loop;

import hr.algebra.head_soccer_2d_game.controller.PlaygroundController;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.model.entities.enums.GameState;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    private final PlaygroundController playgroundController;
    private long lastTime = 0;

    private static final double GAME_DURATION = 60;
    private double remainingTime = GAME_DURATION;

    private final GameObjectManager gameObjectManager;
    private final GamePhysicManager gamePhysicManager;
    private final GameStateManager gameStateManager;

    public GameLoop(GameObjectManager gameObjectManager, GamePhysicManager gamePhysicManager,
                    GameStateManager gameStateManager, PlaygroundController playgroundController) {
        this.gameObjectManager = gameObjectManager;
        this.gamePhysicManager = gamePhysicManager;
        this.gameStateManager = gameStateManager;
        this.playgroundController = playgroundController;
    }

    @Override
    public void handle(long now) {
        if (lastTime == 0) {
            lastTime = now;
            return;
        }

        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        if (!gameStateManager.isRunning()) return;
        remainingTime -= deltaTime;

        if (remainingTime <= 0) {
            remainingTime = 0;
            gameStateManager.setCurrentState(GameState.PAUSE);
            return;
        }

        gamePhysicManager.update(deltaTime);

        if (gameStateManager.isScoredGoalFlag()) {
            resetAfterGoal();
            gameStateManager.setScoredGoalFlag(false);
        }
        playgroundController.render();
        playgroundController.updateTimerLabel(remainingTime);
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
        this.lastTime = 0;
    }
}