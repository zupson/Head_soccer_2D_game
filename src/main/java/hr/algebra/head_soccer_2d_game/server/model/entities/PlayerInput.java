package hr.algebra.head_soccer_2d_game.server.model.entities;

import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import java.io.Serial;
import java.io.Serializable;

public class PlayerInput implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private PlayerType playerType;
    private String keyCode;
    private boolean isKeyPressed;

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public boolean isKeyPressed() {
        return isKeyPressed;
    }

    public void setKeyPressed(boolean keyPressed) {
        isKeyPressed = keyPressed;
    }
}