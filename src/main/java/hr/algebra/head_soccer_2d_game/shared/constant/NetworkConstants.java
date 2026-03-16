package hr.algebra.head_soccer_2d_game.shared.constant;

public enum NetworkConstants {
    //port moze biti bilo koji broj, ali iznad 1024 jer su od 0-1024 ugrađeni porotovi

    HOST("localhost"),
    PORT_PLAYER_2(2008),
    PORT_PLAYER_1(2009),
    PORT_SERVER_FROM_PLAYER_1(2028),
    PORT_SERVER_FROM_PLAYER_2(2029),
    PORT_SERVER_CONTROL(2030);

    private final Object value;

    NetworkConstants(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}