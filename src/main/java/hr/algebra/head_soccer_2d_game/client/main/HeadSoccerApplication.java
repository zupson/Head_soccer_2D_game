package hr.algebra.head_soccer_2d_game.client.main;

import hr.algebra.head_soccer_2d_game.shared.constant.NetworkConstants;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
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
    }

    private void runSelectedPlayer(GameDataListener controller) {
        if (PlayerType.PLAYER_2.equals(playerType)) {
            new Thread(() -> NetworkUtils.receiveSnapshot((int) NetworkConstants.PORT_PLAYER_2.getValue(), controller)).start();
        } else {
            new Thread(() -> NetworkUtils.receiveSnapshot((int) NetworkConstants.PORT_PLAYER_1.getValue(), controller)).start();
        }
    }

    public static void main(String[] args) {
        String firstCommandLineArgument = args[0];
        Boolean playerTypeExists = false;

        for (PlayerType playerType : PlayerType.values()) {
            if (firstCommandLineArgument.equals(playerType.toString())) {
                playerTypeExists = true;
                break;
            }
        }
        if (playerTypeExists) {
            playerType = PlayerType.valueOf(firstCommandLineArgument);
            launch();
        }
    }
}