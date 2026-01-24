package hr.algebra.head_soccer_2d_game.controller;

import hr.algebra.head_soccer_2d_game.constant.DimenConstants;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.model.entities.GameObject;
import javafx.scene.input.KeyEvent;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

public class PlayerController {
    public static final double MOVE_SPEED_X = 3.0;
    public static final double MOVE_SPEED_Y = 0.0;
    public static final double FLOOR_TOLERANCE = 0.01;
    public static final double JUMP_VELOCITY = 8.0;
    private final GameObjectManager gameObjectManager;

    public PlayerController(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
        init();
    }

    private Body playerL_body;
    private Body playerR_body;

    public void init() {
        playerL_body = gameObjectManager.getLeftPlayer().getBody();
        playerR_body = gameObjectManager.getRightPlayer().getBody();
    }

    public void handleKeyRelease(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT, RIGHT ->  playerL_body.setLinearVelocity(0, playerL_body.getLinearVelocity().y);
            case A, D -> playerR_body.setLinearVelocity(0, playerR_body.getLinearVelocity().y);
        }
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        var key = keyEvent.getCode();
        switch (key) {
            case LEFT -> playerL_body.applyImpulse(new Vector2(-MOVE_SPEED_X, MOVE_SPEED_Y));
            case RIGHT -> playerL_body.applyImpulse(new Vector2(MOVE_SPEED_X, MOVE_SPEED_Y));
            case UP -> {
                if (isPlayerOnFloor(gameObjectManager.getLeftPlayer())) {
                    Vector2 vel = playerL_body.getLinearVelocity();
                    vel.y = JUMP_VELOCITY; // instant jump
                    playerL_body.setLinearVelocity(vel);
                }
            }
            case A -> playerR_body.applyImpulse(new Vector2(-MOVE_SPEED_X, MOVE_SPEED_Y));
            case D -> playerR_body.applyImpulse(new Vector2(MOVE_SPEED_X, MOVE_SPEED_Y));
            case W -> {
                if (isPlayerOnFloor(gameObjectManager.getRightPlayer())) {
                    Vector2 vel = playerR_body.getLinearVelocity();
                    vel.y = JUMP_VELOCITY; // instant jump
                    playerR_body.setLinearVelocity(vel);
                }
            }
        }
    }

    private boolean isPlayerOnFloor(GameObject gObject) {
        double playerY = gObject.getBody().getTransform().getTranslationY();
        double floorY = gameObjectManager.getFloor().getBody().getTransform().getTranslationY() + DimenConstants.HORIZONTAL_BOUNDARY_HEIGHT / 2;
        return Math.abs(playerY - (floorY + DimenConstants.PLAYER_HEIGHT / 2)) <= FLOOR_TOLERANCE;
    }
}
