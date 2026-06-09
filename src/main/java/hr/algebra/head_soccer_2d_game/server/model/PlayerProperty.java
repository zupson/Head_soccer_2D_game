package hr.algebra.head_soccer_2d_game.server.model;

import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import javafx.scene.paint.Color;
import lombok.Data;

@Data
public class PlayerProperty {
    private String playerName;
    private Color color;
    private PlayerType  playerType;
}