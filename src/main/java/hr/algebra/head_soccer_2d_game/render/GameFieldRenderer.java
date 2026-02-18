package hr.algebra.head_soccer_2d_game.render;

import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.utilities.DrawUtils;
import javafx.scene.canvas.GraphicsContext;

public class GameFieldRenderer implements Drawable {
    private final GraphicsContext graphicsContext;
    private final GameObjectManager gameObjectManager;

    public GameFieldRenderer(GraphicsContext graphicsContext, GameObjectManager gameObjectManager) {
        this.graphicsContext = graphicsContext;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void draw() {
        DrawUtils.drawFloor(graphicsContext, gameObjectManager.getFloor());
        DrawUtils.drawCelling(graphicsContext, gameObjectManager.getCeiling());
        DrawUtils.drawLeftWall(graphicsContext, gameObjectManager.getLeftBoundaryWall());
        DrawUtils.drawRightWall(graphicsContext, gameObjectManager.getRightBoundaryWall());
        DrawUtils.drawGoal(graphicsContext, gameObjectManager.getLeftGoal());
        DrawUtils.drawGoal(graphicsContext, gameObjectManager.getRightGoal());
        DrawUtils.drawBall(graphicsContext, gameObjectManager.getBall());
    }
}