package hr.algebra.head_soccer_2d_game.client.controller.ui;

import hr.algebra.head_soccer_2d_game.client.controller.input.PlayerInputHandler;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.client.main.HeadSoccerApplication;
import hr.algebra.head_soccer_2d_game.client.render.GameFieldRenderer;
import hr.algebra.head_soccer_2d_game.client.render.PlayerRenderer;
import hr.algebra.head_soccer_2d_game.server.model.GameCommand;
import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteService;
import hr.algebra.head_soccer_2d_game.shared.constant.WindowSizeConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.event.GameOverListener;
import hr.algebra.head_soccer_2d_game.shared.utilities.ChatUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.FileUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;
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

@Slf4j
public class PlaygroundController implements GameOverListener, GameDataListener {
    private GraphicsContext graphicContext;
    private PlayerRenderer playerRenderer;
    private GameFieldRenderer gameFieldRenderer;
    private PlayerInputHandler playerInputHandler;
    private ChatRemoteService chatRemoteService;
    private GameDataSnapshot lastSnapshot;

    @FXML
    private TextField tfChat;
    @FXML
    private TextArea taChat;
    @FXML
    private Label lbLeftPlayer;
    @FXML
    private Label lbRightPlayer;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label lbLeftPlayerScore;
    @FXML
    private Label lbRightPlayerScore;
    @FXML
    private Label lbTimer;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnResume;

    @FXML
    public void initialize() {
        playerInputHandler = new PlayerInputHandler(HeadSoccerApplication.playerType);
        initCanvas();
        setupInputHandlers();
        initRenderers();
        initChat();
        Platform.runLater(() -> {
            Stage stage = (Stage) lbTimer.getScene().getWindow();
            stage.setOnCloseRequest(event -> showAlert());
        });
    }

    //TODO: pauzirati obje instance klijenta kad 1 od njih klikne quit.
    //TODO: Javiti message drugom klijentu ako je prvi quita-o.
    //TODO: Ako file za serijalizaciju psotoji, onda loadaj s njega, inače loadaj new Game.

    private void showAlert() {
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        quitAlert.setTitle("Quit");
        quitAlert.setContentText("Are you sure?");

        Optional<ButtonType> buttonType = quitAlert.showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK)
            saveGameToFile();
    }

    private void saveGameToFile() {
        try {
            FileUtils.saveGameToFile(lastSnapshot);
        } catch (IOException e) {
            showSaveErrorAlert(e);
        }
    }

    private void showSaveErrorAlert(IOException e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Greška");
        errorAlert.setContentText("Nije moguće spremiti igru: " + e.getMessage());
        errorAlert.showAndWait();
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
        if (chatRemoteService != null)
            ChatUtils.getChatRefreshTimeline(chatRemoteService, taChat).play();
    }

    private void setupInputHandlers() {
        gameCanvas.setOnKeyPressed(playerInputHandler::handleKeyPress);
        gameCanvas.setOnKeyReleased(playerInputHandler::handleKeyRelease);
    }

    @FXML
    public void onGameOver() {
        Platform.runLater(() -> {
            Optional<ButtonType> buttonType = showGameOverAlert();
            buttonType.ifPresent(this::handleGameOverChoice);
        });
    }

    private Optional<ButtonType> showGameOverAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game over");
        alert.setHeaderText("Time is up!");
        alert.setContentText("Do you want to start a new game or close the application?");

        var btnNewGame = new ButtonType("New Game");
        var btnClose = new ButtonType("Close");
        alert.getButtonTypes().setAll(btnNewGame, btnClose);

        return alert.showAndWait();
    }

    private void handleGameOverChoice(ButtonType buttonType) {
        if (buttonType.getText().equals("New Game"))
            sendGameCommand(GameState.NEW_GAME);
        else
            Platform.exit();
    }

    private void sendGameCommand(GameState newGame) {
        GameCommand gameCommand = new GameCommand();
        gameCommand.setGameState(newGame);
        NetworkUtils.sendSnapshot(gameCommand,
                        ConfigReader.getIntegerValueForKey(ConfigKey.SERVER_CONTROL_PORT));
    }

    @FXML
    public void onPauseClicked(ActionEvent event) {
        btnPause.setVisible(false);
        btnResume.setVisible(true);
        sendGameCommand(GameState.PAUSE);
    }

    @FXML
    public void onResumeClicked(ActionEvent event) {
        btnResume.setVisible(false);
        btnPause.setVisible(true);
        sendGameCommand(GameState.RUNNING);
        gameCanvas.requestFocus();
    }

    @Override
    public void onGameDataChanged(GameDataSnapshot gameDataSnapshot) {
        lastSnapshot = gameDataSnapshot;
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
        playerRenderer.showPlayerName(lbLeftPlayer, lbRightPlayer);
    }

    private void initRenderers() {
        playerRenderer = new PlayerRenderer(graphicContext);
        gameFieldRenderer = new GameFieldRenderer(graphicContext);
    }

    public void sendChatMessage() {
        ChatUtils.sendChatMessage(chatRemoteService, tfChat);
        log.debug("CHAT REMOTE SERVICE: {}", chatRemoteService);
        tfChat.clear();
    }
}