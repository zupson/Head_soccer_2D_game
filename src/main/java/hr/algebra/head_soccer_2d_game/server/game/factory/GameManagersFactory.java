package hr.algebra.head_soccer_2d_game.server.game.factory;

import hr.algebra.head_soccer_2d_game.shared.annotations.BusinessLogic;
import hr.algebra.head_soccer_2d_game.shared.event.GoalListener;
import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;

@BusinessLogic(description = "Factory for creating game manager instances")
public class GameManagersFactory {

    public GameObjectManager createGameObjectManager() {
        var gameObjectManager = new GameObjectManager();
        gameObjectManager.initGameObjectManager();
        return gameObjectManager;
    }

    public GamePhysicManager createPhysicsManager(GameObjectManager gom,
                                                  GoalListener goalListener,
                                                  GameStateManager gameStateManager) {
        var gamePhysicManager = new GamePhysicManager(goalListener, gameStateManager);
        gamePhysicManager.initPhysics(gom);
        return gamePhysicManager;
    }

    public GameStateManager createStateManager() {
        return new GameStateManager();
    }
}