package hr.algebra.head_soccer_2d_game.controller;

import hr.algebra.head_soccer_2d_game.game.context.GameContext;
import hr.algebra.head_soccer_2d_game.game.loop.GameLoop;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.model.entities.enums.GameState;
import hr.algebra.head_soccer_2d_game.view.PlayerView;
import hr.algebra.head_soccer_2d_game.view.GameFieldView;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameController {
    private static final int SCENE_WIDTH = 1000;
    private static final int SCENE_HEIGHT = 800;

    @FXML
    private Canvas gameCanvas;
    private GraphicsContext graphicContext;
    private GameObjectManager gameObjectManager;
    private PlayerView playerView;
    private GameFieldView gameFieldView;
    private PlayerController playerController;
//    private GameFieldController gameFieldController;
    @FXML
    private void initialize() {
        var currentInstance = GameContext.getCurrentInstance();
        var gamePhysicManager = currentInstance.getGamePhysicManager();
        var gameStateManager = currentInstance.getGameStateManager();
        gameObjectManager = currentInstance.getGameObjectManager();
        playerController = new PlayerController(gameObjectManager);

//        gameFieldController = new GameFieldController(gameObjectManager);
        graphicContext = gameCanvas.getGraphicsContext2D();
        gameCanvas.setWidth(SCENE_WIDTH);
        gameCanvas.setHeight(SCENE_HEIGHT);
        gameCanvas.setFocusTraversable(true);
        gameCanvas.requestFocus();
        gameCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
//                newScene.setOnKeyPressed(e->playerController.handleKeyPress(e)); Umjesto ovoga
                newScene.setOnKeyPressed(playerController::handleKeyPress);
                newScene.setOnKeyReleased(playerController::handleKeyRelease);
            }
        });

        playerView = new PlayerView(graphicContext, gameObjectManager);
        gameFieldView = new  GameFieldView(graphicContext, gameObjectManager);

        AnimationTimer gameLoop = new GameLoop(gamePhysicManager, gameStateManager, this);
        gameLoop.start();
        gameStateManager.setCurrentState(GameState.RUNNING);
    }
    public void render() {
        if (graphicContext == null || gameObjectManager == null) return;
        graphicContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameFieldView.draw();
        playerView.draw();
    }
}