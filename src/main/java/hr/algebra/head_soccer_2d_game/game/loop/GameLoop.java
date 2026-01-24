package hr.algebra.head_soccer_2d_game.game.loop;

import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.controller.GameController;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    private long lastTime = 0;

    private final GamePhysicManager gamePhysicManager;
    private final GameStateManager gameStateManager;
    private final GameController gameController;

    public GameLoop(GamePhysicManager gamePhysicManager, GameStateManager gameStateManager, GameController gameController) {

        this.gamePhysicManager = gamePhysicManager;
        this.gameStateManager = gameStateManager;
        this.gameController = gameController;
    }

    @Override
    public void handle(long now) {
        if (lastTime == 0){
            lastTime = now;
            return;
        }

        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        if (!gameStateManager.isRunning()) return;

        gamePhysicManager.update(deltaTime);

        //TODO: BusinessLogic() - npr. kad je zabijen gol?, reset ako je goal.

        gameController.render();
    }
}
