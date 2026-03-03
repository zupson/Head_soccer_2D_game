package hr.algebra.head_soccer_2d_game.controller.ui;

import hr.algebra.head_soccer_2d_game.constant.WindowSizeConstants;
import hr.algebra.head_soccer_2d_game.controller.event.GoalListener;
import hr.algebra.head_soccer_2d_game.controller.input.PlayerInputHandler;
import hr.algebra.head_soccer_2d_game.game.GameContext;
import hr.algebra.head_soccer_2d_game.game.GameLoop;
import hr.algebra.head_soccer_2d_game.manager.GameObjectManager;
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
import javafx.scene.control.*;

import java.io.IOException;

public class PlaygroundController implements GoalListener {
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

    public void setGameContext(GameContext context) {
        gameObjectManager = context.getGameObjectManager();
        var gamePhysicManager = context.getGamePhysicManager();
        gameStateManager = context.getGameStateManager();

        gameLoop = new GameLoop(gameObjectManager, gamePhysicManager, gameStateManager, this);
        gamePhysicManager.setGoalListener(this);

        playerInputHandler = new PlayerInputHandler(gameObjectManager);

        initCanvas();
        setupInputHandlers();
        initRenderers();

        loadSavedOrStartNewGame();
    }

    private void initCanvas() {
        graphicContext = gameCanvas.getGraphicsContext2D();
        gameCanvas.setWidth(WindowSizeConstants.SCENE_WIDTH);
        gameCanvas.setHeight(WindowSizeConstants.SCENE_HEIGHT);
        gameCanvas.setFocusTraversable(true);
        Platform.runLater(() -> gameCanvas.requestFocus());
    }

    private void initRenderers() {
        playerRenderer = new PlayerRenderer(graphicContext, gameObjectManager);
        gameFieldRenderer = new GameFieldRenderer(graphicContext, gameObjectManager);
    }

    private void loadSavedOrStartNewGame() {
        GameDataSnapshot snapshot = null;
        try {
            snapshot = FileUtils.loadGame();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No valid save found. Starting new game.");
            FileUtils.deleteSave();
        }
        if (snapshot != null) {
            GameDataUtils.loadCurrentGameData(snapshot, gameLoop);
            updateScoreLabels(snapshot);
        } else {
            showStartNewGameAlert();
        }
    }

    private void showStartNewGameAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No Saved Game");
            alert.setHeaderText("No previous game found!");
            alert.setContentText("Do you want to start a new game or close the application?");

            var btnStart = new ButtonType("Start New Game");
            var btnClose = new ButtonType("Close");
            alert.getButtonTypes().setAll(btnStart, btnClose);

            var result = alert.showAndWait().orElse(btnClose);

            if (result == btnStart) {
                startNewGame();
            } else {
                Platform.exit();
            }
        });
    }

    private void updateScoreLabels(GameDataSnapshot snapshot) {
        lbLeftPlayerScore.setText(String.valueOf(snapshot.player1Score));
        lbRightPlayerScore.setText(String.valueOf(snapshot.player2Score));
    }

    private void setupInputHandlers() {
        gameCanvas.setOnKeyPressed(playerInputHandler::handleKeyPress);
        gameCanvas.setOnKeyReleased(playerInputHandler::handleKeyRelease);
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

    @FXML
    public void onGameOver() {
        gameLoop.stop();
        gameStateManager.setCurrentState(GameState.GAME_OVER);
        FileUtils.deleteSave();
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Time is up!");
            alert.setContentText("Do you want to start a new game or close the application?");

            var btnNewGame = new javafx.scene.control.ButtonType("New Game");
            var btnClose = new javafx.scene.control.ButtonType("Close");
            alert.getButtonTypes().setAll(btnNewGame, btnClose);

            var result = alert.showAndWait().orElse(btnClose);

            if (result == btnNewGame) {
                startNewGame();
            } else {
                Platform.exit();
            }
        });
    }

    private void startNewGame() {
        gameObjectManager.setPlayersStartPositions();
        gameObjectManager.setBallStartPosition();
        gameObjectManager.getLeftGoal().setScore(0);
        gameObjectManager.getRightGoal().setScore(0);
        gameLoop.setRemainingTime(60);
        gameStateManager.setCurrentState(GameState.RUNNING);
        gameLoop.start();
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
        gameLoop.start();
        gameCanvas.requestFocus();
    }
}