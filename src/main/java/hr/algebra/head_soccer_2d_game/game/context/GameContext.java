package hr.algebra.head_soccer_2d_game.game.context;

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

    public GameContext() {
        var gameFactory = new GameFactory();
        gameObjectManager = gameFactory.createGameObjectManager();
        gamePhysicManager = gameFactory.cretePhysicsManager(gameObjectManager);
        gameStateManager = gameFactory.createStateManager();
    }

    public static GameContext getCurrentInstance() {
        if (currentInstance == null) {
            currentInstance = new GameContext();
        }
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