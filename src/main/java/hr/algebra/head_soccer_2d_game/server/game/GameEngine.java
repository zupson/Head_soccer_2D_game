package hr.algebra.head_soccer_2d_game.server.game;

import hr.algebra.head_soccer_2d_game.server.input.PlayerInputHandler;
import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameCommand;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.model.entities.Player;
import hr.algebra.head_soccer_2d_game.server.model.entities.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.constant.NetworkConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import hr.algebra.head_soccer_2d_game.shared.event.*;
import hr.algebra.head_soccer_2d_game.shared.utilities.FileUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;

import static hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils.threadPool;

public class GameEngine implements GoalListener, GameDataListener, GameOverListener, PlayerInputListener, GameCommandListener, PlayerInputApplier {
    private GameObjectManager gameObjectManager;
    private GamePhysicManager gamePhysicManager;
    private GameStateManager gameStateManager;
    private GameLoop gameLoop;
    private final PlayerInputHandler playerOneInputHandler = new PlayerInputHandler();
    private final PlayerInputHandler playerTwoInputHandler = new PlayerInputHandler();

    public void init() {
        GameSingleton.init(this);
        getCurrentInstanceForManagers(GameSingleton.getCurrentInstance());
        gamePhysicManager.setGoalListener(this);

        NetworkUtils.receivePlayerInput((int) NetworkConstants.PORT_SERVER_FROM_PLAYER_1.getValue(), this);
        NetworkUtils.receivePlayerInput((int) NetworkConstants.PORT_SERVER_FROM_PLAYER_2.getValue(), this);
        NetworkUtils.receiveGameCommand((int) NetworkConstants.PORT_SERVER_CONTROL.getValue(), this);

        gameLoop = new GameLoop(this, this, gameObjectManager, gamePhysicManager, gameStateManager, this);
        gameLoop.startGameLoop();
        gameStateManager.setCurrentState(GameState.RUNNING);
    }

    private void getCurrentInstanceForManagers(GameSingleton currentInstance) {
        gameObjectManager = currentInstance.getGameObjectManager();
        gamePhysicManager = currentInstance.getGamePhysicManager();
        gameStateManager = currentInstance.getGameStateManager();
    }

    //update gameObjectManager props for every goal scores on right value
    @Override
    public void onGoalScored(Side side) {
        switch (side) {
            case Side.LEFT -> gameObjectManager.getLeftGoal().addScore();
            case Side.RIGHT -> gameObjectManager.getRightGoal().addScore();
        }
    }

    //Starting TRAM station = Crnomerec
    @Override
    public void onGameDataChanged(GameDataSnapshot gameDataSnapshot) {
        threadPool.submit(() -> {
            NetworkUtils.sendSnapshot(gameDataSnapshot, (int) NetworkConstants.PORT_PLAYER_1.getValue());
            NetworkUtils.sendSnapshot(gameDataSnapshot, (int) NetworkConstants.PORT_PLAYER_2.getValue());
        });
    }

    @Override
    public void onGameOver() {
        gameStateManager.setCurrentState(GameState.GAME_OVER);
        FileUtils.deleteSave();
    }

    @Override
    public void onPlayerInput(PlayerInput input) {
        System.out.println("INPUT from: " + input.getPlayerType() + " key: " + input.getKeyCode());
        Player player = input.getPlayerType() == PlayerType.PLAYER_1
                ? gameObjectManager.getLeftPlayer()
                : gameObjectManager.getRightPlayer();


        if (input.getPlayerType() == PlayerType.PLAYER_1) {
            playerOneInputHandler.handleInput(input, player);
        } else {
            playerTwoInputHandler.handleInput(input, player);
        }
    }

    @Override
    public void onGameCommand(GameCommand gameCommand) {
        if (gameCommand.getGameState() == GameState.NEW_GAME) {
            resetGame();
        } else {
            gameStateManager.setCurrentState(gameCommand.getGameState());
        }
    }

    @Override
    public void applyPlayerInputs() {
        playerOneInputHandler.applyInput(gameObjectManager.getLeftPlayer());
        playerTwoInputHandler.applyInput(gameObjectManager.getRightPlayer());
    }

    private void resetGame() {
        gameObjectManager.getLeftGoal().setScore(0);
        gameObjectManager.getRightGoal().setScore(0);
        gameObjectManager.setPlayersStartPositions();
        gameObjectManager.setBallStartPosition();
        gameLoop.resetTimer();
        gameStateManager.setCurrentState(GameState.RUNNING);
    }
}