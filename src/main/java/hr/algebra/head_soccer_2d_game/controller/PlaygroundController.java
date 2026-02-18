package hr.algebra.head_soccer_2d_game.controller;

import hr.algebra.head_soccer_2d_game.game.context.GameContext;
import hr.algebra.head_soccer_2d_game.game.loop.GameLoop;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.model.entities.enums.GameState;
import hr.algebra.head_soccer_2d_game.model.entities.enums.Side;
import hr.algebra.head_soccer_2d_game.render.GameFieldRenderer;
import hr.algebra.head_soccer_2d_game.render.PlayerRenderer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

public class PlaygroundController implements GoalListener {
    private static final int SCENE_WIDTH = 1000;
    private static final int SCENE_HEIGHT = 800;

    @FXML
    private Canvas gameCanvas;
    private GraphicsContext graphicContext;
    private GameObjectManager gameObjectManager;
    private PlayerRenderer playerRenderer;
    private GameFieldRenderer gameFieldRenderer;
    private PlayerController playerController;
    private GameStateManager gameStateManager;
    @FXML
    private Label lbLeftPlayerScore;
    @FXML
    private Label lbRightPlayerScore;


    @FXML
    private void initialize() {
        var currentInstance = GameContext.getCurrentInstance();
        var gamePhysicManager = currentInstance.getGamePhysicManager();
        gamePhysicManager.setGoalListener(this);

        gameStateManager = currentInstance.getGameStateManager();
        gameObjectManager = currentInstance.getGameObjectManager();
        playerController = new PlayerController(gameObjectManager);

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


        playerRenderer = new PlayerRenderer(graphicContext, gameObjectManager);
        gameFieldRenderer = new GameFieldRenderer(graphicContext, gameObjectManager);

        var gameLoop = new GameLoop(gameObjectManager, gamePhysicManager, gameStateManager, this);
        gameLoop.start();
        gameStateManager.setCurrentState(GameState.RUNNING);
    }

    public void render() {
        if (graphicContext == null || gameObjectManager == null) return;
        graphicContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameFieldRenderer.draw();
        playerRenderer.draw();
    }

    @Override
    public void onGoalScored(Side side, int ignoredScore) {

        switch (side){
            case Side.LEFT ->{
                gameObjectManager.getLeftGoal().addScore();
            }
            case Side.RIGHT -> {
                gameObjectManager.getRightGoal().addScore();
            }
        }

        updateScoreLabel(side);
    }

    private void updateScoreLabel(Side side) {
        Platform.runLater(() -> {
            int score = (side == Side.LEFT)
                    ? gameObjectManager.getLeftGoal().getScore()
                    : gameObjectManager.getRightGoal().getScore();

            System.out.println("Updating UI label: " + score);
            gameStateManager.setScoredGoalFlag(true);

            if (side == Side.LEFT)
                lbLeftPlayerScore.setText(String.valueOf(score));
            else
                lbRightPlayerScore.setText(String.valueOf(score));
        });
    }
}