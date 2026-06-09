package hr.algebra.head_soccer_2d_game.server.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlayerPropsTag {
    PLAYER_PROPERTIES("PlayerProperties"),
    PLAYER_PROPERTY("PlayerProperty"),
    PLAYER_NAME("PlayerName"),
    COLOR("Color"),
    PLAYER_TYPE("PlayerType"),;

    private final String tagName;
}