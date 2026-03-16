package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.shared.constant.ColorConstants;
import hr.algebra.head_soccer_2d_game.shared.constant.DimenConstants;
import hr.algebra.head_soccer_2d_game.shared.constant.PosConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawUtils {
    private DrawUtils() {
    }

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

    private static void drawBoundary(GraphicsContext gC, double pos_x, double pos_y, double width, double height, Color c) {
        double scaled_width = width * SCALE;
        double scaled_height = height * SCALE;
        double scaled_centerX = pos_x * SCALE;
        double scaled_centerY = pos_y * SCALE;
        double canvasY = gC.getCanvas().getHeight() - (scaled_centerY + scaled_height / 2);
        double canvasX = scaled_centerX - scaled_width / 2;
        gC.setFill(c);
        gC.fillRect(canvasX, canvasY, scaled_width, scaled_height);
    }

    public static void drawGoal(GraphicsContext gC, Side side) {
        double baseX = Side.LEFT == side
                ? DimenConstants.GOAL_WIDTH.getValue() * SCALE / 2
                : gC.getCanvas().getWidth() - DimenConstants.GOAL_WIDTH.getValue() * SCALE / 2;

        double baseY = gC.getCanvas().getHeight() - (PosConstants.GAME_OBJECT_POS_Y.getValue() * SCALE);

        double scaled_goalWidth = DimenConstants.GOAL_WIDTH.getValue() * SCALE;
        double scaled_goalHeight = DimenConstants.GOAL_HEIGHT.getValue() * SCALE;
        double crossbarHeight = scaled_goalHeight / 10.0;
        double postWidth = scaled_goalWidth / 3.0;

        gC.setFill(ColorConstants.GOALS_COLOR);

        if (Side.LEFT == side) {
            gC.fillRect(baseX - scaled_goalWidth / 2, baseY - scaled_goalHeight, scaled_goalWidth, crossbarHeight);
            gC.fillRect(baseX - scaled_goalWidth / 2, baseY - scaled_goalHeight, postWidth, scaled_goalHeight);
        } else if (Side.RIGHT == side) {
            double crossbarX = baseX + scaled_goalWidth / 2 - scaled_goalWidth;
            double postX = baseX + scaled_goalWidth / 2 - postWidth;
            gC.fillRect(crossbarX, baseY - scaled_goalHeight, scaled_goalWidth, crossbarHeight);
            gC.fillRect(postX, baseY - scaled_goalHeight, postWidth, scaled_goalHeight);
        }
    }

    public static void drawBall(GraphicsContext gC, double x, double y) {
        double x_scaled = x * SCALE - (DimenConstants.BALL_WIDTH.getValue() * SCALE) / 2;
        double y_scaled = gC.getCanvas().getHeight() - (y * SCALE) - (DimenConstants.BALL_HEIGHT.getValue() * SCALE);

        x_scaled = Math.clamp(x_scaled, 0, gC.getCanvas().getWidth() - DimenConstants.BALL_WIDTH.getValue() * SCALE);
        y_scaled = Math.clamp(y_scaled, 0, gC.getCanvas().getHeight() - DimenConstants.BALL_HEIGHT.getValue() * SCALE);

        gC.setFill(ColorConstants.BALL_COLOR);
        gC.fillOval(x_scaled, y_scaled, DimenConstants.BALL_WIDTH.getValue() * SCALE, DimenConstants.BALL_HEIGHT.getValue() * SCALE);
    }

    public static void drawPlayer(GraphicsContext gC, double x, double y) {
        double scaled_x = x * SCALE;
        double scaled_y = gC.getCanvas().getHeight() - (y * SCALE);
        double headRadius = DimenConstants.PLAYER_HEIGHT.getValue() / 2.0 * SCALE;
        double footWidth = DimenConstants.PLAYER_WIDTH.getValue() * SCALE;
        double footHeight = DimenConstants.PLAYER_HEIGHT.getValue() / 4.0 * SCALE;

        scaled_x = Math.clamp(scaled_x, 0, gC.getCanvas().getWidth() - DimenConstants.PLAYER_WIDTH.getValue() * SCALE);
        scaled_y = Math.clamp(scaled_y, 0, gC.getCanvas().getHeight() - footHeight);

        gC.setFill(ColorConstants.PLAYER_COLOR);
        gC.fillOval(scaled_x - headRadius, scaled_y - headRadius * 2, headRadius * 2, headRadius * 2);
        gC.fillRect(scaled_x - footWidth / 2, scaled_y, footWidth, footHeight);
    }
}