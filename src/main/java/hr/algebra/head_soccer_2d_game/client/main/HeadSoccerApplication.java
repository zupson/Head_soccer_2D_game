package hr.algebra.head_soccer_2d_game.client.main;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.utilities.DynamicPopUpUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.NetworkUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HeadSoccerApplication extends Application {

    public static PlayerType playerType;

    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/hr/algebra/head_soccer_2d_game/playground_view.fxml"));
        var scene = new Scene(fxmlLoader.load());//initialize iz kontrollera ce se ovdje automatski pozvati na fxml.load()

        stage.setTitle("Head Socker 2D Game - " + playerType);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        runSelectedPlayer(fxmlLoader.getController());
        DynamicPopUpUtils.createStartPopUpDialog(playerType);

    }

    private void runSelectedPlayer(GameDataListener controller) {
        ConfigKey port = switch (playerType) {
            case PLAYER_2 -> ConfigKey.PLAYER_TWO_SERVER_PORT;
            default -> ConfigKey.PLAYER_ONE_SERVER_PORT;
        };
        NetworkUtils.receiveSnapshot(ConfigReader.getIntegerValueForKey(port), controller);
    }

    public static void main(String[] args) {
        playerType = parsePlayerType(args[0]);
        if (playerType != null)
            launch();
    }

    private static PlayerType parsePlayerType(String arg) {
        try {
            return PlayerType.valueOf(arg.toUpperCase());
        }catch (IllegalArgumentException e) {
            return null;
        }
    }
}