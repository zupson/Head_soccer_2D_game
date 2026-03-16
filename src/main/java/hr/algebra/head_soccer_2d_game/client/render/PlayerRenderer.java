package hr.algebra.head_soccer_2d_game.client.render;

import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.shared.utilities.DrawUtils;
import javafx.scene.canvas.GraphicsContext;

public class PlayerRenderer implements Drawable {
    private final GraphicsContext graphicsContext;

    public PlayerRenderer(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void draw(GameDataSnapshot gameDataSnapshot) {
            DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerOneX(),  gameDataSnapshot.getPlayerOneY());
            DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerTwoX(),  gameDataSnapshot.getPlayerTwoY());
    }
}
