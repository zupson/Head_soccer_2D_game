package hr.algebra.head_soccer_2d_game.server.input;

import hr.algebra.head_soccer_2d_game.server.model.entities.Player;
import hr.algebra.head_soccer_2d_game.server.model.entities.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.constant.InputType;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerInputHandler {
    private final Set<String> pressedKeys = ConcurrentHashMap.newKeySet();

    public void handleInput(PlayerInput playerInput, Player currentPlayer) {
        if (playerInput.isKeyPressed()) {
            pressedKeys.add(playerInput.getKeyCode());
        } else {
            pressedKeys.remove(playerInput.getKeyCode());
        }
    }

    public void applyInput(Player player) {
//        System.out.println("applyInput called, pressedKeys: " + pressedKeys);
        Body body = player.getBody();
        if (pressedKeys.contains(InputType.LEFT.getValue())) {
            System.out.println("APPLYING LEFT FORCE to body at: " + body.getTransform().getTranslationX());
            body.applyForce(new Vector2(-250, 0));
        }
        if (pressedKeys.contains(InputType.RIGHT.getValue())) {
            System.out.println("APPLYING RIGHT FORCE to body at: " + body.getTransform().getTranslationX());
            body.applyForce(new Vector2(250, 0));
        }
        if (pressedKeys.contains(InputType.UP.getValue())) {
            body.applyForce(new Vector2(0, 250));
        }
    }
}