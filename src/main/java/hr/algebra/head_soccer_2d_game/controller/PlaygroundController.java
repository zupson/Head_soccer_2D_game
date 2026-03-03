package hr.algebra.head_soccer_2d_game.controller;

import hr.algebra.head_soccer_2d_game.game.context.GameContext;
import hr.algebra.head_soccer_2d_game.game.loop.GameLoop;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.model.entities.enums.GameState;
import hr.algebra.head_soccer_2d_game.model.entities.enums.Side;
import hr.algebra.head_soccer_2d_game.render.GameFieldRenderer;
import hr.algebra.head_soccer_2d_game.render.PlayerRenderer;
import hr.algebra.head_soccer_2d_game.utilities.FileUtils;
import hr.algebra.head_soccer_2d_game.utilities.GameDataUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class PlaygroundController implements GoalListener {
    private static final int SCENE_WIDTH = 1000;
    private static final int SCENE_HEIGHT = 800;

    private GraphicsContext graphicContext;

    private GameObjectManager gameObjectManager;
    private GameStateManager gameStateManager;

    private PlayerRenderer playerRenderer;
    private GameFieldRenderer gameFieldRenderer;
    private PlayerInputHandler playerInputHandler;
    private GameLoop gameLoop;

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label lbLeftPlayerScore;
    @FXML
    private Label lbRightPlayerScore;
    @FXML
    private Label lbTimer;
    @FXML
    public MenuItem miLoadGame;
    @FXML
    public MenuItem miSaveGame;
    @FXML
    public Button btnPause;
    @FXML
    public Button btnResume;

    @FXML
    private void initialize() {
        graphicContext = gameCanvas.getGraphicsContext2D();
        gameCanvas.setWidth(SCENE_WIDTH);
        gameCanvas.setHeight(SCENE_HEIGHT);
        gameCanvas.setFocusTraversable(true);
        gameCanvas.requestFocus();
    }

    public void setGameContext(GameContext context) {
        gameObjectManager = context.getGameObjectManager();
        GamePhysicManager gamePhysicManager = context.getGamePhysicManager();
        gameStateManager = context.getGameStateManager();

        playerInputHandler = new PlayerInputHandler(gameObjectManager);
        gameLoop = new GameLoop(gameObjectManager, gamePhysicManager, gameStateManager, this);
        gamePhysicManager.setGoalListener(this);
        gameStateManager.setCurrentState(GameState.RUNNING);

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(playerInputHandler::handleKeyPress);
        gameCanvas.setOnKeyReleased(playerInputHandler::handleKeyRelease);
        Platform.runLater(() -> gameCanvas.requestFocus());

        playerRenderer = new PlayerRenderer(graphicContext, gameObjectManager);
        gameFieldRenderer = new GameFieldRenderer(graphicContext, gameObjectManager);
        gameLoop.start();
    }

    public void render() {
        if (graphicContext == null || gameObjectManager == null) return;
        graphicContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameFieldRenderer.draw();
        playerRenderer.draw();
    }

    @Override
    public void onGoalScored(Side side) {
        switch (side) {
            case Side.LEFT -> gameObjectManager.getLeftGoal().addScore();
            case Side.RIGHT -> gameObjectManager.getRightGoal().addScore();
        }
        updateScoreLabel(side);
    }

    private void updateScoreLabel(Side side) {
        Platform.runLater(() -> {
            int score = (side == Side.LEFT)
                    ? gameObjectManager.getLeftGoal().getScore()
                    : gameObjectManager.getRightGoal().getScore();

            gameStateManager.setScoredGoalFlag(true);
            switch (side) {
                case Side.LEFT -> lbLeftPlayerScore.setText(String.valueOf(score));
                case Side.RIGHT -> lbRightPlayerScore.setText(String.valueOf(score));
            }
        });
    }

    public void showGameOverMessage() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("Time is up!!!");
            alert.showAndWait();
        });
    }

    public void updateTimerLabel(double time) {
        lbTimer.setText(String.format("%d", (int) time));
    }

    @FXML
    public void onPauseClicked(ActionEvent event) {
        gameLoop.stop();
        btnPause.setVisible(false);
        btnResume.setVisible(true);
        try {
            FileUtils.saveGame(GameDataUtils.saveCurrentGameData(gameLoop));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Failed");
            alert.setContentText("Could not save game: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onResumeClicked(ActionEvent event) {
        btnResume.setVisible(false);
        btnPause.setVisible(true);
        try {
            GameDataSnapshot gameDataSnapshot = FileUtils.loadGame();
            GameDataUtils.loadCurrentGameData(gameDataSnapshot, gameLoop);
        } catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Failed");
            alert.setContentText("Could not load game: " + e.getMessage());
            alert.showAndWait();
        }
        gameCanvas.requestFocus();
    }
}