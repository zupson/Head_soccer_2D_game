package hr.algebra.head_soccer_2d_game.shared.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WindowSizeConstants {

    SCENE_WIDTH(1000),
    SCENE_HEIGHT(700);

    private final int value;
}