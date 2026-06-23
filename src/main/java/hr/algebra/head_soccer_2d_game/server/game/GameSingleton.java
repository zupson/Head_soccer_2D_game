package hr.algebra.head_soccer_2d_game.server.game;

import hr.algebra.head_soccer_2d_game.shared.annotations.BusinessLogic;
import hr.algebra.head_soccer_2d_game.shared.event.GoalListener;
import hr.algebra.head_soccer_2d_game.server.game.factory.GameManagersFactory;
import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;
import lombok.Getter;

@BusinessLogic(description = "Singleton instance of the game")
public class GameSingleton {
    private static GameSingleton currentInstance;
    @Getter
    private final GameObjectManager gameObjectManager;
    @Getter
    private final GamePhysicManager gamePhysicManager;
    @Getter
    private final GameStateManager gameStateManager;

    private GameSingleton(GoalListener goalListener) {
        var gameFactory = new GameManagersFactory();
        gameObjectManager = gameFactory.createGameObjectManager();
        gameStateManager = gameFactory.createStateManager();
        gamePhysicManager = gameFactory.createPhysicsManager(gameObjectManager, goalListener, gameStateManager);
    }

    public static void init(GoalListener goalListener) {
        if (currentInstance == null) {
            currentInstance = new GameSingleton(goalListener);
        }
    }

    public static GameSingleton getCurrentInstance() {
        return currentInstance;
    }
}