package hr.algebra.head_soccer_2d_game.client.controller.input;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.server.model.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;
import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerInputHandler {
    private final PlayerType playerType;

    public void handleKeyPress(KeyEvent keyEvent) {
        sendInput(keyEvent, true);
    }

    public void handleKeyRelease(KeyEvent keyEvent) {
        sendInput(keyEvent, false);
    }

    private void sendInput(KeyEvent keyEvent, boolean isPressed) {
        System.out.println("CLIENT SENDING: " + keyEvent.getCode().getName() + " pressed: " + isPressed);

        PlayerInput playerInput = createPlayerInput(keyEvent, isPressed);

        int targetPort = switch (playerType) {
            case PlayerType.PLAYER_1 -> ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_PLAYER_ONE_PORT);
            case PlayerType.PLAYER_2 -> ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_PLAYER_TWO_PORT);
        };

        NetworkUtils.sendSnapshot(playerInput, targetPort);
    }

    private PlayerInput createPlayerInput(KeyEvent keyEvent, boolean isPressed) {
        return PlayerInput.builder()
                .keyCode(keyEvent.getCode().getName())
                .playerType(playerType)
                .keyPressed(isPressed)
                .build();
    }
}