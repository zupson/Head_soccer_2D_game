package hr.algebra.head_soccer_2d_game.client.controller.ui;

import hr.algebra.head_soccer_2d_game.client.controller.input.PlayerInputHandler;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.client.main.HeadSoccerApplication;
import hr.algebra.head_soccer_2d_game.client.render.GameFieldRenderer;
import hr.algebra.head_soccer_2d_game.client.render.PlayerRenderer;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameCommand;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteService;
import hr.algebra.head_soccer_2d_game.shared.constant.WindowSizeConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.event.GameOverListener;
import hr.algebra.head_soccer_2d_game.shared.utilities.ChatUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

import java.util.Optional;

public class PlaygroundController implements GameOverListener, GameDataListener {
    public TextField tfChat;
    public TextArea taChat;
    private GraphicsContext graphicContext;
    private PlayerRenderer playerRenderer;
    private GameFieldRenderer gameFieldRenderer;
    private PlayerInputHandler playerInputHandler;
    ChatRemoteService chatRemoteService;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label lbLeftPlayerScore;
    @FXML
    public Label lbRightPlayerScore;
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
    public Button btnSent;

    @FXML //ova anotacija omogucuje kontroler metodi initialize da bude selfinitializing
    public void initialize() {
        playerInputHandler = new PlayerInputHandler(HeadSoccerApplication.playerType);
        initCanvas();
        setupInputHandlers();
        initRenderers();
        initChat();
    }

    private void initCanvas() {
        graphicContext = gameCanvas.getGraphicsContext2D();
        gameCanvas.setWidth(WindowSizeConstants.SCENE_WIDTH.getValue());
        gameCanvas.setHeight(WindowSizeConstants.SCENE_HEIGHT.getValue());
        gameCanvas.setFocusTraversable(true);
        Platform.runLater(() -> gameCanvas.requestFocus());
    }

    private void initChat() {
        chatRemoteService = ChatUtils.initializeChatRemoteService().orElse(null);
        if (chatRemoteService != null) {
            ChatUtils.getChatRefreshTimeline(chatRemoteService, taChat).play();
        }
    }

    private void setupInputHandlers() {
        gameCanvas.setOnKeyPressed(playerInputHandler::handleKeyPress);
        gameCanvas.setOnKeyReleased(playerInputHandler::handleKeyRelease);
    }

    @FXML
    public void onGameOver() {

        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Time is up!");
            alert.setContentText("Do you want to start a new game or close the application?");

            var btnNewGame = new javafx.scene.control.ButtonType("New Game");
            var btnClose = new javafx.scene.control.ButtonType("Close");
            alert.getButtonTypes().setAll(btnNewGame, btnClose);

            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get() == btnNewGame) {
                GameCommand gameCommand = new GameCommand();
                gameCommand.setGameState(GameState.NEW_GAME);
//                NetworkUtils.sendSnapshot(gameCommand, (int) NetworkConstants.PORT_SERVER_CONTROL.getValue());
                NetworkUtils.sendSnapshot(gameCommand, ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_CONTROL_PORT));
            } else if (buttonType.isPresent() && buttonType.get() == btnClose) {
                Platform.exit();
            }
        });
    }

    @FXML
    public void onPauseClicked(ActionEvent event) {
        btnPause.setVisible(false);
        btnResume.setVisible(true);
        GameCommand gameCommand = new GameCommand();
        gameCommand.setGameState(GameState.PAUSE);
        NetworkUtils.sendSnapshot(gameCommand, ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_CONTROL_PORT));
    }

    @FXML
    public void onResumeClicked(ActionEvent event) {
        btnResume.setVisible(false);
        btnPause.setVisible(true);
        GameCommand gameCommand = new GameCommand();
        gameCommand.setGameState(GameState.RUNNING);
        NetworkUtils.sendSnapshot(gameCommand, ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_CONTROL_PORT));
        gameCanvas.requestFocus();
    }

    //end TRAM station = Sopot
    @Override
    public void onGameDataChanged(GameDataSnapshot gameDataSnapshot) {
        restoreGameData(gameDataSnapshot);
    }

    private void restoreGameData(GameDataSnapshot gameDataSnapshot) {
        Platform.runLater(() -> {
            updateScoreLabel(gameDataSnapshot.getPlayerOneScore(), gameDataSnapshot.getPlayerTwoScore());
            updateTimerLabel(gameDataSnapshot.getRemainingTime());
            render(gameDataSnapshot);
        });
    }

    private void updateScoreLabel(int leftPlayer, int rightPlayer) {
        lbLeftPlayerScore.setText(String.valueOf(leftPlayer));
        lbRightPlayerScore.setText(String.valueOf(rightPlayer));
    }

    private void updateTimerLabel(double time) {
        lbTimer.setText(String.format("%d", (int) time));
    }

    private void render(GameDataSnapshot gameDataSnapshot) {
        if (graphicContext == null) return;
        graphicContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameFieldRenderer.draw(gameDataSnapshot);
        playerRenderer.draw(gameDataSnapshot);
    }

    private void initRenderers() {
        playerRenderer = new PlayerRenderer(graphicContext);
        gameFieldRenderer = new GameFieldRenderer(graphicContext);
    }

    public void sendChatMessage() {
        ChatUtils.sendChatMessage(chatRemoteService, tfChat);
        System.out.println("CHAT REMOTE SERVICE: " +  chatRemoteService);
        tfChat.clear();
    }
}