package hr.algebra.head_soccer_2d_game.server.game;

import hr.algebra.head_soccer_2d_game.shared.event.GoalListener;
import hr.algebra.head_soccer_2d_game.server.game.factory.GameManagersFactory;
import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;

public class GameSingleton {
    private static GameSingleton currentInstance;
    private final GameObjectManager gameObjectManager;
    private final GamePhysicManager gamePhysicManager;
    private final GameStateManager gameStateManager;

    private GameSingleton(GoalListener goalListener) {
        var gameFactory = new GameManagersFactory();
        gameObjectManager = gameFactory.createGameObjectManager();
        gameStateManager = gameFactory.createStateManager();
        gamePhysicManager = gameFactory.cretePhysicsManager(gameObjectManager, goalListener);
    }

    public static void init(GoalListener goalListener) {
        if (currentInstance == null) {
            currentInstance = new GameSingleton(goalListener);
        }
    }

    public static GameSingleton getCurrentInstance() {
        return currentInstance;
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

    public GamePhysicManager getGamePhysicManager() {
        return gamePhysicManager;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }
}