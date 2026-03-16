package hr.algebra.head_soccer_2d_game.client.controller.input;

import hr.algebra.head_soccer_2d_game.server.model.entities.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.constant.NetworkConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;
import javafx.scene.input.KeyEvent;

public class PlayerInputHandler {

    private final PlayerType playerType;
    private int targetPort;

    public PlayerInputHandler(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        sendInput(keyEvent, true);
    }

    public void handleKeyRelease(KeyEvent keyEvent) {
        sendInput(keyEvent, false);
    }

    private void sendInput(KeyEvent keyEvent, boolean isPressed) {
        System.out.println("CLIENT SENDING: " + keyEvent.getCode().getName() + " pressed: " + isPressed);
        PlayerInput playerInput = new PlayerInput();
        playerInput.setKeyCode(keyEvent.getCode().getName());
        playerInput.setPlayerType(playerType);
        playerInput.setKeyPressed(isPressed);
        switch (playerType) {
            case PlayerType.PLAYER_1 -> targetPort = (int) NetworkConstants.PORT_SERVER_FROM_PLAYER_1.getValue();
            case PlayerType.PLAYER_2 -> targetPort = (int) NetworkConstants.PORT_SERVER_FROM_PLAYER_2.getValue();
        }
        NetworkUtils.sendSnapshot(playerInput, targetPort);
    }
}