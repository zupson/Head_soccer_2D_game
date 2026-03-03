package hr.algebra.head_soccer_2d_game.utilities;

import hr.algebra.head_soccer_2d_game.game.GameContext;
import hr.algebra.head_soccer_2d_game.game.GameLoop;
import hr.algebra.head_soccer_2d_game.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.model.entities.enums.GameState;

public class GameDataUtils {
    private GameDataUtils() {
    }

    public static void loadCurrentGameData(GameDataSnapshot snapshot, GameLoop gameLoop) {
        var gameObjectManager = GameContext.getCurrentInstance().getGameObjectManager();
        var gameStateManager = GameContext.getCurrentInstance().getGameStateManager();

        if (snapshot == null || snapshot.gameState == GameState.GAME_OVER) {
            FileUtils.deleteSave();
            gameStateManager.setCurrentState(GameState.RUNNING);
            return;
        }
        gameObjectManager.getLeftGoal().setScore(snapshot.player1Score);
        gameObjectManager.getRightGoal().setScore(snapshot.player2Score);
        gameObjectManager.getLeftPlayer().getBody().getTransform().setTranslationX(snapshot.player1X);
        gameObjectManager.getLeftPlayer().getBody().getTransform().setTranslationY(snapshot.player1Y);
        gameObjectManager.getRightPlayer().getBody().getTransform().setTranslationX(snapshot.player2X);
        gameObjectManager.getRightPlayer().getBody().getTransform().setTranslationY(snapshot.player2Y);
        gameObjectManager.getBall().getBody().getTransform().setTranslationX(snapshot.ballX);
        gameObjectManager.getBall().getBody().getTransform().setTranslationY(snapshot.ballY);

        gameStateManager.setCurrentState(GameState.RUNNING);

        gameLoop.setRemainingTime(snapshot.remainingTime);
        gameLoop.start();
    }

    public static GameDataSnapshot saveCurrentGameData(GameLoop gameLoop) {
        var gameObjectManager = GameContext.getCurrentInstance().getGameObjectManager();
        var gameStateManager = GameContext.getCurrentInstance().getGameStateManager();

        if (gameObjectManager == null || gameLoop == null) return null;
        return new GameDataSnapshot(
                gameObjectManager.getLeftGoal().getScore(),
                gameObjectManager.getRightGoal().getScore(),
                gameObjectManager.getLeftPlayer().getBody().getTransform().getTranslationX(),
                gameObjectManager.getLeftPlayer().getBody().getTransform().getTranslationY(),
                gameObjectManager.getRightPlayer().getBody().getTransform().getTranslationX(),
                gameObjectManager.getRightPlayer().getBody().getTransform().getTranslationY(),
                gameObjectManager.getBall().getBody().getTransform().getTranslationX(),
                gameObjectManager.getBall().getBody().getTransform().getTranslationY(),

                gameStateManager.getCurrentState(),

                gameLoop.getRemainingTime());
    }
}