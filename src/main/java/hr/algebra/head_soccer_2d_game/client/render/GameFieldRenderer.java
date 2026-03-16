package hr.algebra.head_soccer_2d_game.client.render;

import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import hr.algebra.head_soccer_2d_game.shared.utilities.DrawUtils;
import javafx.scene.canvas.GraphicsContext;

public class GameFieldRenderer implements Drawable {
    private final GraphicsContext graphicsContext;

    public GameFieldRenderer(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void draw(GameDataSnapshot  gameDataSnapshot) {
        DrawUtils.drawFloor(graphicsContext);
        DrawUtils.drawCelling(graphicsContext);
        DrawUtils.drawLeftWall(graphicsContext);
        DrawUtils.drawRightWall(graphicsContext);
        DrawUtils.drawGoal(graphicsContext, Side.LEFT);
        DrawUtils.drawGoal(graphicsContext, Side.RIGHT);

        DrawUtils.drawBall(graphicsContext, gameDataSnapshot.getBallX(), gameDataSnapshot.getBallY());
    }
}