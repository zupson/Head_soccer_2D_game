package hr.algebra.head_soccer_2d_game.utilities;

import hr.algebra.head_soccer_2d_game.constant.ColorConstants;
import hr.algebra.head_soccer_2d_game.constant.DimenConstants;
import hr.algebra.head_soccer_2d_game.model.entities.*;
import hr.algebra.head_soccer_2d_game.model.entities.enums.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawUtils {
    private DrawUtils() {
    }
    private static final double SCALE = 100.0;

    public static void drawFloor(GraphicsContext gC, Boundary b) {
        drawBoundary(gC, b, ColorConstants.FLOOR_COLOR);
    }

    public static void drawCelling(GraphicsContext gC, Boundary b) {
        drawBoundary(gC, b, ColorConstants.BOUNDERY_COLOR);
    }

    public static void drawLeftWall(GraphicsContext gC, Boundary b) {
        drawBoundary(gC, b, ColorConstants.BOUNDERY_COLOR);
    }

    public static void drawRightWall(GraphicsContext gC, Boundary b) {
        drawBoundary(gC, b, ColorConstants.BOUNDERY_COLOR);
    }

    private static void drawBoundary(GraphicsContext gC, GameObject b, Color c) {
        double width = b.getWidth() * SCALE;
        double height = b.getHeigh() * SCALE;
        double centerX = b.getBody().getTransform().getTranslationX() * SCALE;
        double centerY = b.getBody().getTransform().getTranslationY() * SCALE;
        double canvasY = gC.getCanvas().getHeight() - (centerY + height / 2);
        double canvasX = centerX - width / 2;
        gC.setFill(c);
        gC.fillRect(canvasX, canvasY, width, height);
    }

    public static void drawGoal(GraphicsContext gC, Goal g) {
        double baseX = Side.LEFT == g.getSide()
                ? DimenConstants.GOAL_WIDTH * SCALE / 2
                : gC.getCanvas().getWidth() - DimenConstants.GOAL_WIDTH * SCALE / 2;
        double baseY = gC.getCanvas().getHeight() - (g.getBody().getTransform().getTranslationY() * SCALE);
        double goalWidth = DimenConstants.GOAL_WIDTH * SCALE;
        double goalHeight = DimenConstants.GOAL_HEIGHT * SCALE;
        double crossbarHeight = goalHeight / 10.0;
        double postWidth = goalWidth / 3.0;
        gC.setFill(ColorConstants.GOALS_COLOR);

        if(Side.LEFT == g.getSide()) {
            gC.fillRect(baseX - goalWidth / 2, baseY - goalHeight, goalWidth, crossbarHeight);
            gC.fillRect(baseX - goalWidth / 2, baseY - goalHeight, postWidth, goalHeight);
        } else if (Side.RIGHT ==  g.getSide()) {
            double crossbarX = baseX + goalWidth / 2 - goalWidth;
            double postX = baseX + goalWidth / 2 - postWidth;
            gC.fillRect(crossbarX, baseY - goalHeight, goalWidth, crossbarHeight);
            gC.fillRect(postX, baseY - goalHeight, postWidth, goalHeight);
        }
    }

    public static void drawBall(GraphicsContext gC, Ball b) {
        double x = b.getBody().getTransform().getTranslationX() * SCALE - (DimenConstants.BALL_WIDTH * SCALE) / 2;
        double y = gC.getCanvas().getHeight() - (b.getBody().getTransform().getTranslationY() * SCALE) - (DimenConstants.BALL_HEIGHT * SCALE);

        x = Math.clamp(x, 0, gC.getCanvas().getWidth() - DimenConstants.BALL_WIDTH * SCALE);
        y = Math.clamp(y, 0, gC.getCanvas().getHeight() - DimenConstants.BALL_HEIGHT * SCALE);

        gC.setFill(ColorConstants.BALL_COLOR);
        gC.fillOval(x, y, DimenConstants.BALL_WIDTH * SCALE, DimenConstants.BALL_HEIGHT * SCALE);
    }

    public static void drawPlayer(GraphicsContext gC, GameObject p) {
        double x = p.getBody().getTransform().getTranslationX() * SCALE;
        double y = gC.getCanvas().getHeight() - (p.getBody().getTransform().getTranslationY() * SCALE);
        double headRadius = DimenConstants.PLAYER_HEIGHT / 2.0 * SCALE;
        double footWidth = DimenConstants.PLAYER_WIDTH * SCALE;
        double footHeight = DimenConstants.PLAYER_HEIGHT / 4.0 * SCALE;

        x = Math.clamp(x, 0, gC.getCanvas().getWidth() - DimenConstants.PLAYER_WIDTH * SCALE);
        y = Math.clamp(y, 0, gC.getCanvas().getHeight() - footHeight);

        gC.setFill(ColorConstants.PLAYER_COLOR);
        gC.fillOval(x - headRadius, y - headRadius * 2, headRadius * 2, headRadius * 2);
        gC.fillRect(x - footWidth / 2, y, footWidth, footHeight);
    }
}