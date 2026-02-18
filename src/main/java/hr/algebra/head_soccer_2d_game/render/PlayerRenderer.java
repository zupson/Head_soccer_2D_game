package hr.algebra.head_soccer_2d_game.render;

import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.model.entities.Player;
import hr.algebra.head_soccer_2d_game.utilities.DrawUtils;
import javafx.scene.canvas.GraphicsContext;

public class PlayerRenderer implements Drawable {
    private final GraphicsContext graphicsContext;
    private final GameObjectManager gameObjectManager;

    public PlayerRenderer(GraphicsContext graphicsContext, GameObjectManager gameObjectManager) {
        this.graphicsContext = graphicsContext;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void draw() {
        for (Player p : gameObjectManager.getPlayers()){
            DrawUtils.drawPlayer(graphicsContext,p);
        }
    }
}
