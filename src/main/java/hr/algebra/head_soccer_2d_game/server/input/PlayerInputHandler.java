package hr.algebra.head_soccer_2d_game.server.input;

import hr.algebra.head_soccer_2d_game.server.model.Player;
import hr.algebra.head_soccer_2d_game.server.model.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.constant.InputType;
import lombok.extern.slf4j.Slf4j;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PlayerInputHandler {
    private static final int HORIZONTAL_FORCE = 250;
    private static final int JUMP_FORCE = 250;

    private final Set<String> pressedKeys = ConcurrentHashMap.newKeySet();

    public void handleInput(PlayerInput playerInput) {
        if (playerInput.isKeyPressed())
            pressedKeys.add(playerInput.getKeyCode());
        else
            pressedKeys.remove(playerInput.getKeyCode());
    }

    public void applyInput(Player player) {
        Body body = player.getBody();
        if (pressedKeys.contains(InputType.LEFT.getValue())) {
            log.debug("APPLYING LEFT FORCE to body at: {}", body.getTransform().getTranslationX());
            body.applyForce(new Vector2(-HORIZONTAL_FORCE, 0));
        }
        if (pressedKeys.contains(InputType.RIGHT.getValue())) {
            log.debug("APPLYING RIGHT FORCE to body at: {}", body.getTransform().getTranslationX());
            body.applyForce(new Vector2(HORIZONTAL_FORCE, 0));
        }
        if (pressedKeys.contains(InputType.UP.getValue()))
            body.applyForce(new Vector2(0, JUMP_FORCE));
    }
}