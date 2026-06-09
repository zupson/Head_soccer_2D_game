package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.shared.constant.ColorConstants;
import hr.algebra.head_soccer_2d_game.shared.constant.DimenConstants;
import hr.algebra.head_soccer_2d_game.shared.constant.PosConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DrawUtils {
    private static final double SCALE = 100.0;

    public static void drawFloor(GraphicsContext gC) {
        drawBoundary(gC,
                PosConstants.FLOOR_FIX_POS_X.getValue(), PosConstants.FLOOR_FIX_POS_Y.getValue(),
                DimenConstants.HORIZONTAL_BOUNDARY_WIDTH.getValue(), DimenConstants.HORIZONTAL_BOUNDARY_HEIGHT.getValue(),
                ColorConstants.FLOOR_COLOR);
    }

    public static void drawCelling(GraphicsContext gC) {
        drawBoundary(gC,
                PosConstants.CELLING_FIX_POS_X.getValue(), PosConstants.CELLING_FIX_POS_Y.getValue(),
                DimenConstants.HORIZONTAL_BOUNDARY_WIDTH.getValue(), DimenConstants.HORIZONTAL_BOUNDARY_HEIGHT.getValue(),
                ColorConstants.BOUNDERY_COLOR);
    }

    public static void drawLeftWall(GraphicsContext gC) {
        drawBoundary(gC,
                PosConstants.LEFT_WALL_FIX_POS_X.getValue(), PosConstants.LEFT_WALL_FIX_POS_Y.getValue(),
                DimenConstants.VERTICAL_BOUNDARY_WIDTH.getValue(), DimenConstants.VERTICAL_BOUNDARY_HEIGHT.getValue(),
                ColorConstants.BOUNDERY_COLOR);
    }

    public static void drawRightWall(GraphicsContext gC) {
        drawBoundary(gC,
                PosConstants.RIGHT_WALL_FIX_POS_X.getValue(), PosConstants.RIGHT_WALL_FIX_POS_Y.getValue(),
                DimenConstants.VERTICAL_BOUNDARY_WIDTH.getValue(), DimenConstants.VERTICAL_BOUNDARY_HEIGHT.getValue(),
                ColorConstants.BOUNDERY_COLOR);
    }

    private static void drawBoundary(GraphicsContext gC, double posX, double posY,
                                     double width, double height, Color c) {
        double scaledWidth = width * SCALE;
        double scaledHeight = height * SCALE;
        double scaledCenterX = posX * SCALE;
        double scaledCenterY = posY * SCALE;

        double canvasY = gC.getCanvas().getHeight() - (scaledCenterY + scaledHeight / 2);
        double canvasX = scaledCenterX - scaledWidth / 2;

        gC.setFill(c);
        gC.fillRect(canvasX, canvasY, scaledWidth, scaledHeight);
    }

    public static void drawGoal(GraphicsContext gC, Side side) {
        double baseX = Side.LEFT == side
                ? DimenConstants.GOAL_WIDTH.getValue() * SCALE / 2
                : gC.getCanvas().getWidth() - DimenConstants.GOAL_WIDTH.getValue() * SCALE / 2;

        double baseY = gC.getCanvas().getHeight() - (PosConstants.GAME_OBJECT_POS_Y.getValue() * SCALE);

        double scaledGoalWidth = DimenConstants.GOAL_WIDTH.getValue() * SCALE;
        double scaledGoalHeight = DimenConstants.GOAL_HEIGHT.getValue() * SCALE;

        double crossbarHeight = scaledGoalHeight / 10.0;
        double postWidth = scaledGoalWidth / 3.0;

        gC.setFill(ColorConstants.GOALS_COLOR);
        drawGoalShape(gC, side, baseX, scaledGoalWidth, baseY, scaledGoalHeight, crossbarHeight, postWidth);
    }

    private static void drawGoalShape(GraphicsContext gC, Side side, double baseX, double scaledGoalWidth,
                                      double baseY, double scaledGoalHeight, double crossbarHeight, double postWidth) {
        if (Side.LEFT == side) {
            gC.fillRect(baseX - scaledGoalWidth / 2, baseY - scaledGoalHeight, scaledGoalWidth, crossbarHeight);
            gC.fillRect(baseX - scaledGoalWidth / 2, baseY - scaledGoalHeight, postWidth, scaledGoalHeight);
        } else if (Side.RIGHT == side) {
            double crossbarX = baseX + scaledGoalWidth / 2 - scaledGoalWidth;
            double postX = baseX + scaledGoalWidth / 2 - postWidth;
            gC.fillRect(crossbarX, baseY - scaledGoalHeight, scaledGoalWidth, crossbarHeight);
            gC.fillRect(postX, baseY - scaledGoalHeight, postWidth, scaledGoalHeight);
        }
    }

    public static void drawBall(GraphicsContext gC, double x, double y) {
        double xScaled = x * SCALE - (DimenConstants.BALL_WIDTH.getValue() * SCALE) / 2;
        double yScaled = gC.getCanvas().getHeight() - (y * SCALE);

        xScaled = Math.clamp(xScaled, 0, gC.getCanvas().getWidth() - DimenConstants.BALL_WIDTH.getValue() * SCALE);
        yScaled = Math.clamp(yScaled, 0, gC.getCanvas().getHeight() - DimenConstants.BALL_HEIGHT.getValue() * SCALE);

        gC.setFill(ColorConstants.BALL_COLOR);
        gC.fillOval(xScaled, yScaled, DimenConstants.BALL_WIDTH.getValue() * SCALE, DimenConstants.BALL_HEIGHT.getValue() * SCALE);
    }

    public static void drawPlayer(GraphicsContext gC, double x, double y, Color color) {
        double scaledX = x * SCALE;
        double scaledY = gC.getCanvas().getHeight() - (y * SCALE / 2);
        double scaledHeadRadius = (DimenConstants.PLAYER_HEIGHT.getValue() / 2.0) * SCALE;
        double scaledFootWidth = DimenConstants.PLAYER_WIDTH.getValue() * SCALE;
        double scaledFootHeight = (DimenConstants.PLAYER_HEIGHT.getValue() / 4.0) * SCALE;

        scaledX = Math.clamp(scaledX, 0, (gC.getCanvas().getWidth() - DimenConstants.PLAYER_WIDTH.getValue()) * SCALE);
        scaledY = Math.clamp(scaledY, 0, gC.getCanvas().getHeight() - scaledFootHeight);

        gC.setFill(color);
        gC.fillOval(scaledX - scaledHeadRadius, scaledY - scaledHeadRadius * 2, scaledHeadRadius * 2, scaledHeadRadius * 2);
        gC.fillRect(scaledX - scaledFootWidth / 2, scaledY, scaledFootWidth, scaledFootHeight);
    }
}