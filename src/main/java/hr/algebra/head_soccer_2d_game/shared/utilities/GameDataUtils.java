package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.game.GameLoop;
import hr.algebra.head_soccer_2d_game.server.game.GameSingleton;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;

public class GameDataUtils {
    private GameDataUtils() {
    }

    public static void loadCurrentGameData(GameDataSnapshot snapshot, GameLoop gameLoop) {
        var gameObjectManager = GameSingleton.getCurrentInstance().getGameObjectManager();
        var gameStateManager = GameSingleton.getCurrentInstance().getGameStateManager();

        if (snapshot == null || snapshot.getGameState() == GameState.GAME_OVER) {
            FileUtils.deleteSave();
            gameStateManager.setCurrentState(GameState.RUNNING);
            return;
        }
        gameObjectManager.getLeftGoal().setScore(snapshot.getPlayerOneScore());
        gameObjectManager.getRightGoal().setScore(snapshot.getPlayerTwoScore());
        gameObjectManager.getLeftPlayer().getBody().getTransform().setTranslationX(snapshot.getPlayerOneX());
        gameObjectManager.getLeftPlayer().getBody().getTransform().setTranslationY(snapshot.getPlayerOneY());
        gameObjectManager.getRightPlayer().getBody().getTransform().setTranslationX(snapshot.getPlayerTwoX());
        gameObjectManager.getRightPlayer().getBody().getTransform().setTranslationY(snapshot.getPlayerTwoY());
        gameObjectManager.getBall().getBody().getTransform().setTranslationX(snapshot.getBallX());
        gameObjectManager.getBall().getBody().getTransform().setTranslationY(snapshot.getBallY());
        gameLoop.setRemainingTime(snapshot.getRemainingTime());

        gameStateManager.setCurrentState(GameState.RUNNING);

        gameLoop.startGameLoop();
    }

    public static GameDataSnapshot saveCurrentGameData(GameLoop gameLoop) {
        var gameObjectManager = GameSingleton.getCurrentInstance().getGameObjectManager();
        var gameStateManager = GameSingleton.getCurrentInstance().getGameStateManager();

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