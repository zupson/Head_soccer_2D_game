package hr.algebra.head_socker_2d_game.game.factory;

import hr.algebra.head_socker_2d_game.manager.GameObjectManager;
import hr.algebra.head_socker_2d_game.manager.GamePhysicManager;
import hr.algebra.head_socker_2d_game.manager.GameStateManager;

public class GameFactory {
    
    //SAMO KREIRA, NIŠTA NE ČUVA STATIČKI

    public GameObjectManager createGameObjectManager() {
        GameObjectManager gameObjectManager = new GameObjectManager();
        gameObjectManager.initGameObjectManager();
        return gameObjectManager;
    }

    public GamePhysicManager cretePhysicsManager(GameObjectManager gom) {
        GamePhysicManager gamePhysicManager = new GamePhysicManager();
        gamePhysicManager.initPhysics(gom);
        return gamePhysicManager;
    }

    public GameStateManager createStateManager() {
        return new GameStateManager();
    }
}
