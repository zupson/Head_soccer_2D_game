package hr.algebra.head_soccer_2d_game.controller;

import hr.algebra.head_soccer_2d_game.constant.DimenConstants;
import hr.algebra.head_soccer_2d_game.constant.PlayerConstants;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.model.entities.GameObject;
import javafx.scene.input.KeyEvent;
import org.dyn4j.geometry.Vector2;

public class PlayerInputHandler {
    private final GameObjectManager gameObjectManager;
    private final GameObject leftPlayer;
    private final GameObject rightPlayer;

    public PlayerInputHandler(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
        leftPlayer = gameObjectManager.getLeftPlayer();
        rightPlayer = gameObjectManager.getRightPlayer();
    }

    public void handleKeyRelease(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT, RIGHT ->  leftPlayer.getBody().setLinearVelocity(0, leftPlayer.getBody().getLinearVelocity().y);
            case A, D -> rightPlayer.getBody().setLinearVelocity(0, rightPlayer.getBody().getLinearVelocity().y);
        }
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        var key = keyEvent.getCode();
        System.out.println("KEY PRESSED");
        switch (key) {
            case LEFT -> leftPlayer.getBody().applyImpulse(new Vector2(-PlayerConstants.MOVE_SPEED_X, PlayerConstants.MOVE_SPEED_Y));
            case RIGHT -> leftPlayer.getBody().applyImpulse(new Vector2(PlayerConstants.MOVE_SPEED_X, PlayerConstants.MOVE_SPEED_Y));
            case UP -> {
                if (isPlayerOnFloor(gameObjectManager.getLeftPlayer())) {
                    Vector2 vel = leftPlayer.getBody().getLinearVelocity();
                    vel.y = PlayerConstants.JUMP_VELOCITY;
                    leftPlayer.getBody().setLinearVelocity(vel);
                }
            }
            case A -> rightPlayer.getBody().applyImpulse(new Vector2(-PlayerConstants.MOVE_SPEED_X, PlayerConstants.MOVE_SPEED_Y));
            case D -> rightPlayer.getBody().applyImpulse(new Vector2(PlayerConstants.MOVE_SPEED_X, PlayerConstants.MOVE_SPEED_Y));
            case W -> {
                if (isPlayerOnFloor(gameObjectManager.getRightPlayer())) {
                    Vector2 vel = rightPlayer.getBody().getLinearVelocity();
                    vel.y = PlayerConstants.JUMP_VELOCITY;
                    rightPlayer.getBody().setLinearVelocity(vel);
                }
            }
        }
    }

    private boolean isPlayerOnFloor(GameObject gObject) {
        double playerY = gObject.getBody().getTransform().getTranslationY();
        double floorY = gameObjectManager.getFloor().getBody().getTransform()
                .getTranslationY() + DimenConstants.HORIZONTAL_BOUNDARY_HEIGHT / 2;
        return Math.abs(playerY - (floorY + DimenConstants.PLAYER_HEIGHT / 2)) <= PlayerConstants.FLOOR_TOLERANCE;
    }
}
