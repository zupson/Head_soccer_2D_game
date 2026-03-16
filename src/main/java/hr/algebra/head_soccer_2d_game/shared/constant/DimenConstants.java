package hr.algebra.head_soccer_2d_game.shared.constant;

public enum DimenConstants {
    PLAYER_WIDTH(1.0),
    PLAYER_HEIGHT(1.8),
    GOAL_WIDTH(0.3),
    GOAL_HEIGHT(2.3),
    BALL_WIDTH(0.5),
    BALL_HEIGHT(0.5),
    HORIZONTAL_BOUNDARY_WIDTH(20.0),
    HORIZONTAL_BOUNDARY_HEIGHT(0.2),
    VERTICAL_BOUNDARY_WIDTH(0.2),
    VERTICAL_BOUNDARY_HEIGHT(7.0);

    private final double value;

    DimenConstants(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}