package hr.algebra.head_soccer_2d_game.client.main;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class HeadSoccerApplication extends Application {
    private static final String VIEW_PATH = "/hr/algebra/head_soccer_2d_game/playground_view.fxml";
    private static PlayerType playerType;

    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource(VIEW_PATH));
        var scene = new Scene(fxmlLoader.load());

        stage.setTitle("Head Socker 2D Game - " + playerType);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        runSelectedPlayer(fxmlLoader.getController());
    }

    private void runSelectedPlayer(GameDataListener controller) {
        ConfigKey port = switch (playerType) {
            case PLAYER_1 -> ConfigKey.PLAYER_ONE_SERVER_PORT;
            case PLAYER_2 -> ConfigKey.PLAYER_TWO_SERVER_PORT;
        };
        NetworkUtils.receiveSnapshot(ConfigReader.getIntegerValueForKey(port),
                controller);
    }

    public static void main(String[] args) {
        parsePlayerType(args[0]).ifPresentOrElse(
                type -> {
                    playerType = type;
                    launch();
                },
                () -> log.error("Application can't start without a valid player type.")
        );
    }

    private static Optional<PlayerType> parsePlayerType(String arg) {
        try {
            return Optional.of(PlayerType.valueOf(arg.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.error("Invalid player type argument: '{}'. Expected PLAYER_1 or PLAYER_2.",
                    arg, e);
            return Optional.empty();
        }
    }

    public static PlayerType getPlayerType() {
        return playerType;
    }
}