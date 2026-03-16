package hr.algebra.head_soccer_2d_game.shared.event;

import hr.algebra.head_soccer_2d_game.server.model.entities.PlayerInput;

public interface PlayerInputListener {
    void onPlayerInput(PlayerInput input);
}