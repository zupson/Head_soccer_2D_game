package hr.algebra.head_soccer_2d_game.shared.event;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;

public interface GameDataListener {
    void onGameDataChanged(GameDataSnapshot gameDataSnapshot);
}