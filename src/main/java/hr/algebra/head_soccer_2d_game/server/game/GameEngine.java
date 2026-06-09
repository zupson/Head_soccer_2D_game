package hr.algebra.head_soccer_2d_game.server.game;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.server.input.PlayerInputHandler;
import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.server.model.GameCommand;
import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.model.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.annotations.BusinessLogic;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import hr.algebra.head_soccer_2d_game.shared.event.*;
import hr.algebra.head_soccer_2d_game.shared.utilities.FileUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;
import lombok.extern.slf4j.Slf4j;

import static hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils.threadPool;

@Slf4j
@BusinessLogic(description = "Main game engine, coordinates game flow")
public class GameEngine implements GoalListener, GameDataListener, GameOverListener, PlayerInputListener, GameCommandListener, PlayerInputApplier {
    private GameObjectManager gameObjectManager;
    private GamePhysicManager gamePhysicManager;
    private GameStateManager gameStateManager;
    private GameLoop gameLoop;
    private final PlayerInputHandler playerOneInputHandler = new PlayerInputHandler();
    private final PlayerInputHandler playerTwoInputHandler = new PlayerInputHandler();
    private int readyPlayerCounter = 0;

    public void init() {
        GameSingleton.init(this);
        loadManagersFromSingleton(GameSingleton.getCurrentInstance());
        gamePhysicManager.setGoalListener(this);
        initNetwork();
        initGameLoop();
    }

    private void initNetwork() {
        NetworkUtils.receivePlayerInput(ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_PLAYER_ONE_PORT), this);
        NetworkUtils.receivePlayerInput(ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_PLAYER_TWO_PORT), this);
        NetworkUtils.receiveGameCommand(ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_CONTROL_PORT), this);
    }

    private void initGameLoop() {
        gameLoop = new GameLoop(this, this, gameObjectManager, gamePhysicManager, gameStateManager, this);
        gameLoop.startGameLoop();
    }

    private void loadManagersFromSingleton(GameSingleton currentInstance) {
        gameObjectManager = currentInstance.getGameObjectManager();
        gamePhysicManager = currentInstance.getGamePhysicManager();
        gameStateManager = currentInstance.getGameStateManager();
    }

    @Override
    public void onGoalScored(Side side) {
        switch (side) {
            case Side.LEFT -> gameObjectManager.getLeftGoal().addScore();
            case Side.RIGHT -> gameObjectManager.getRightGoal().addScore();
        }
        gameStateManager.setScoredGoalFlag(true);
    }

    @Override
    public void onGameDataChanged(GameDataSnapshot gameDataSnapshot) {
        threadPool.submit(() -> {
            NetworkUtils.sendSnapshot(gameDataSnapshot, ConfigReader.getIntegerValueForKey(ConfigKey.PLAYER_ONE_SERVER_PORT));
            NetworkUtils.sendSnapshot(gameDataSnapshot, ConfigReader.getIntegerValueForKey(ConfigKey.PLAYER_TWO_SERVER_PORT));
        });
    }

    @Override
    public void onGameOver() {
        gameStateManager.setCurrentState(GameState.GAME_OVER);
        FileUtils.deleteSavedGameData();
    }

    @Override
    public void onPlayerInput(PlayerInput input) {
        log.debug("INPUT from: {} key: {}", input.getPlayerType(), input.getKeyCode());
        switch (input.getPlayerType()) {
            case PLAYER_1 -> playerOneInputHandler.handleInput(input);
            case PLAYER_2 -> playerTwoInputHandler.handleInput(input);
        }
    }

    @Override
    public void onGameCommand(GameCommand gameCommand) {
        switch (gameCommand.getGameState()) {
            case NEW_GAME -> resetGame();
            case WAITING_FOR_PLAYER -> onPlayerReady();
            default -> gameStateManager.setCurrentState(gameCommand.getGameState());
        }
    }

    private void onPlayerReady() {
        readyPlayerCounter++;
        if (readyPlayerCounter == 2)
            gameStateManager.setCurrentState(GameState.RUNNING);
    }

    @Override
    public void applyPlayerInputs() {
        playerOneInputHandler.applyInput(gameObjectManager.getLeftPlayer());
        playerTwoInputHandler.applyInput(gameObjectManager.getRightPlayer());
    }

    private void resetGame() {
        resetScores();
        resetPositions();
        gameLoop.resetTimer();
        gameStateManager.setCurrentState(GameState.RUNNING);
    }

    private void resetPositions() {
        gameObjectManager.setPlayersStartPositions();
        gameObjectManager.setBallStartPosition();
    }

    private void resetScores() {
        gameObjectManager.getLeftGoal().setScore(0);
        gameObjectManager.getRightGoal().setScore(0);
    }
}