package hr.algebra.head_soccer_2d_game.shared.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InputType {
    UP("W"),
    LEFT("A"),
    RIGHT("D");

    private final String value;
}