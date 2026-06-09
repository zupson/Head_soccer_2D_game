package hr.algebra.head_soccer_2d_game.server.model;

import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class PlayerInput implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private PlayerType playerType;
    private String keyCode;
    private boolean keyPressed;
}