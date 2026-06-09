package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.game.GameLoop;
import hr.algebra.head_soccer_2d_game.server.game.GameSingleton;
import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameDataUtils {

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