package hr.algebra.head_soccer_2d_game.shared.event;

import hr.algebra.head_soccer_2d_game.server.model.entities.GameCommand;

public interface GameCommandListener {
    void onGameCommand(GameCommand gameCommand);
}
