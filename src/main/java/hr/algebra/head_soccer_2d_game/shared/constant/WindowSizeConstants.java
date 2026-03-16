package hr.algebra.head_soccer_2d_game.shared.constant;

public enum WindowSizeConstants {

    SCENE_WIDTH(1000),
    SCENE_HEIGHT(800);

    private final int value;

    WindowSizeConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}