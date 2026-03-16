package hr.algebra.head_soccer_2d_game.shared.constant;

public enum PosConstants {
    //GAME OBJECTS POS
    GAME_OBJECT_POS_Y(0.25),
    LEFT_PLAYER_POS_X(1.0),
    RIGHT_PLAYER_POS_X(9.0),
    LEFT_GOAL_FIX_POS_X(0.3),
    RIGHT_GOAL_FIX_POS_X(9.7),
    BALL_POS_X(5.0),
    //BOUNDARIES POS
    FLOOR_FIX_POS_X(10.0),
    FLOOR_FIX_POS_Y(0.2),
    RIGHT_WALL_FIX_POS_X(10.0),
    RIGHT_WALL_FIX_POS_Y(2.5),
    LEFT_WALL_FIX_POS_X(0.0),
    LEFT_WALL_FIX_POS_Y(2.5),
    CELLING_FIX_POS_X(10.0),
    CELLING_FIX_POS_Y(6.0);

    private final double value;

    PosConstants(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}