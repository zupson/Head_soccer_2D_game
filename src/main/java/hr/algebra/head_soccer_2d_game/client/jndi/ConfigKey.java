package hr.algebra.head_soccer_2d_game.client.jndi;

public enum ConfigKey {
    HOSTNAME("host.name"),
    PLAYER_ONE_SERVER_PORT("player.one.server.port"),
    PLAYER_TWO_SERVER_PORT("player.two.server.port"),
    SERVER_PLAYER_ONE_PORT("server.player.one.port"),
    SERVER_PLAYER_TWO_PORT("server.player.two.port"),
    SERVER_CONTROL_PORT("server.control.port"),
    RMI_PORT("rmi.server.port");

    private final String key;

    ConfigKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}