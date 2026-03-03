package hr.algebra.head_soccer_2d_game.game.factory;

import hr.algebra.head_soccer_2d_game.controller.event.GoalListener;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.manager.GameStateManager;

public class GameManagersFactory {

    public GameObjectManager createGameObjectManager() {
        var gameObjectManager = new GameObjectManager();
        gameObjectManager.initGameObjectManager();
        return gameObjectManager;
    }

    public GamePhysicManager cretePhysicsManager(GameObjectManager gom, GoalListener goalListener) {
        var gamePhysicManager = new GamePhysicManager(goalListener);
        gamePhysicManager.initPhysics(gom);
        return gamePhysicManager;
    }

    public GameStateManager createStateManager() {
        return new GameStateManager();
    }
}