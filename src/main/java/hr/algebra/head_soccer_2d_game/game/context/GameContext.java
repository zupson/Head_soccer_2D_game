package hr.algebra.head_soccer_2d_game.game.context;

import hr.algebra.head_soccer_2d_game.controller.GoalListener;
import hr.algebra.head_soccer_2d_game.game.factory.GameFactory;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.manager.GameStateManager;

public class GameContext {
    //SINGLETON, centralno mjesto za pristup instancama
    private static GameContext currentInstance;
    private final GameObjectManager gameObjectManager;
    private final GamePhysicManager gamePhysicManager;
    private final GameStateManager gameStateManager;

    public GameContext(GoalListener goalListener) {
        var gameFactory = new GameFactory();
        gameObjectManager = gameFactory.createGameObjectManager();
        gameStateManager = gameFactory.createStateManager();
        gamePhysicManager = gameFactory.cretePhysicsManager(gameObjectManager, goalListener);
    }

    public static void init(GoalListener goalListener) {
        if (currentInstance == null) {
            currentInstance = new GameContext(goalListener);
        }
    }

    public static GameContext getCurrentInstance() {
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