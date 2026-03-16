package hr.algebra.head_soccer_2d_game.shared.constant;

public enum InputType {
    UP("W"),
    LEFT("A"),
    RIGHT("D");

    private final String value;

    InputType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}