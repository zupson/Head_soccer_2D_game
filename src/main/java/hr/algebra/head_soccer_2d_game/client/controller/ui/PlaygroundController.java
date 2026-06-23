package hr.algebra.head_soccer_2d_game.client.controller.ui;

import hr.algebra.head_soccer_2d_game.client.controller.input.PlayerInputHandler;
import hr.algebra.head_soccer_2d_game.client.main.HeadSoccerApplication;
import hr.algebra.head_soccer_2d_game.client.render.GameFieldRenderer;
import hr.algebra.head_soccer_2d_game.client.render.GameRender;
import hr.algebra.head_soccer_2d_game.client.render.PlayerRenderer;
import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteService;
import hr.algebra.head_soccer_2d_game.shared.constant.WindowSizeConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.utilities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class PlaygroundController implements GameDataListener {
    private PlayerInputHandler playerInputHandler;
    private ChatRemoteService chatRemoteService;
    private GameDataSnapshot lastSnapshot;
    private final AtomicBoolean gameOverShown = new AtomicBoolean(false);
    private GameRender gameRender;
    private boolean chatFocused = false;
    
    @FXML
    private TextField tfChat;
    @FXML
    private TextArea taChat;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label lbTimer;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnResume;
    @FXML
    private Label lbLeftPlayer;
    @FXML
    private Label lbRightPlayer;
    @FXML
    private Label lbLeftPlayerScore;
    @FXML
    private Label lbRightPlayerScore;

    @FXML
    public void initialize() {
        playerInputHandler = new PlayerInputHandler(HeadSoccerApplication.getPlayerType());
        setupInputHandlers();
        initRenderers();
        initChat();
        checkForSavedGame();
        setupButtonsVisibility();
        setupChatFocusHandlers();

        Platform.runLater(() -> {
            Stage stage = (Stage) lbTimer.getScene().getWindow();
            stage.setOnCloseRequest(event -> AlertUtils.showQuitAlert(this::saveGameToFile));
        });
    }

    private void setupButtonsVisibility() {
        btnPause.setVisible(false);
        btnResume.setVisible(true);
    }

    private void setupChatFocusHandlers() {
        tfChat.setOnMouseClicked(event -> chatFocused = true);
        tfChat.setOnMouseExited(event -> chatFocused = false);
    }

    private void saveGameToFile() {
        try {
            FileUtils.saveGameToFile(lastSnapshot);
        } catch (IOException e) {
            AlertUtils.showSaveErrorAlert(e);
        }
    }

    private void initChat() {
        chatRemoteService = ChatUtils.initializeChatRemoteService().orElse(null);
        if (chatRemoteService != null)
            ChatUtils.getChatRefreshTimeline(chatRemoteService, taChat).play();
    }

    private void setupInputHandlers() {
        gameCanvas.setOnKeyPressed(playerInputHandler::handleKeyPress);
        gameCanvas.setOnKeyReleased(playerInputHandler::handleKeyRelease);
    }

    @FXML
    public void onPauseClicked(ActionEvent event) {
        NetworkUtils.sendGameCommand(GameState.PAUSE);
    }

    @FXML
    public void onResumeClicked(ActionEvent event) {
        NetworkUtils.sendGameCommand(GameState.RUNNING);
        gameCanvas.requestFocus();
    }

    @Override
    public void onGameDataChanged(GameDataSnapshot gameDataSnapshot) {
        lastSnapshot = gameDataSnapshot;
        if (gameDataSnapshot.getGameState() == GameState.QUIT) {
            Platform.runLater(Platform::exit);
            return;
        }
        if (gameDataSnapshot.getGameState() == GameState.GAME_OVER) {
            if (gameOverShown.compareAndSet(false, true)) {
                Platform.runLater(() -> {
                    Optional<ButtonType> buttonType = AlertUtils.showGameOverAlert(lastSnapshot);
                    buttonType.ifPresent(this::handleGameOverChoice);
                });
            }
            return;
        }
        if (gameDataSnapshot.getGameState() == GameState.RUNNING) {
            gameOverShown.set(false);

            if (!chatFocused)
                Platform.runLater(() -> gameCanvas.requestFocus());
        }
        restoreGameData(gameDataSnapshot);
        updatePauseUI(gameDataSnapshot.getGameState());
    }

    private void handleGameOverChoice(ButtonType buttonType) {
        if (buttonType.getText().equals("Rerun"))
            NetworkUtils.sendGameCommand(GameState.RERUN);
        else {
            NetworkUtils.sendGameCommand(GameState.QUIT);
            Platform.runLater(Platform::exit);
        }
    }

    private void updatePauseUI(GameState gameState) {
        Platform.runLater(() -> {
                    boolean isPaused = gameState == GameState.PAUSE;
                    btnPause.setVisible(!isPaused);
                    btnResume.setVisible(isPaused);
                }
        );
    }

    private void checkForSavedGame() {
        if (!FileUtils.savedGameExists()) {
            DynamicPopUpUtils.createStartPopUpDialog(HeadSoccerApplication.getPlayerType());
            return;
        }

        Platform.runLater(() -> {
            AlertUtils.showLoadSuccessAlert(continued -> {
                if (continued) {
                    NetworkUtils.sendGameCommand(GameState.LOAD_GAME);
                } else {
                    FileUtils.deleteSavedGameData();
                    XMLUtils.deletePlayerProps();
                    DynamicPopUpUtils.createStartPopUpDialog(HeadSoccerApplication.getPlayerType());
                }
            });
        });
    }

    private void restoreGameData(GameDataSnapshot gameDataSnapshot) {
        Platform.runLater(() -> gameRender.render(gameDataSnapshot));
    }

    private void initRenderers() {
        GraphicsContext graphicContext = gameCanvas.getGraphicsContext2D();
        gameCanvas.setWidth(WindowSizeConstants.SCENE_WIDTH.getValue());
        gameCanvas.setHeight(WindowSizeConstants.SCENE_HEIGHT.getValue());
        gameCanvas.setFocusTraversable(true);
        Platform.runLater(() -> gameCanvas.requestFocus());

        var fieldRenderer = new GameFieldRenderer(graphicContext);
        var playerRenderer = new PlayerRenderer(graphicContext);
        gameRender = new GameRender(graphicContext, playerRenderer, fieldRenderer,
                lbLeftPlayer, lbRightPlayer, lbLeftPlayerScore, lbRightPlayerScore, lbTimer);
    }

    public void sendChatMessage() {
        ChatUtils.sendChatMessage(chatRemoteService, tfChat);
        log.debug("CHAT REMOTE SERVICE: {}", chatRemoteService);
        tfChat.clear();
    }
}