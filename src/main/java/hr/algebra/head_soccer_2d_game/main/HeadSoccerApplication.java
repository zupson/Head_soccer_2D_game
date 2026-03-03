package hr.algebra.head_soccer_2d_game.main;

import hr.algebra.head_soccer_2d_game.controller.PlaygroundController;
import hr.algebra.head_soccer_2d_game.game.context.GameContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HeadSoccerApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(
                getClass().getResource("/hr/algebra/head_soccer_2d_game/playground_view.fxml")
        );
        var scene = new Scene(fxmlLoader.load());

        PlaygroundController controller = fxmlLoader.getController();
        GameContext.init(controller);
        GameContext currentInstance = GameContext.getCurrentInstance();
        controller.setGameContext(currentInstance);

        stage.setTitle("Head Socker 2D Game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}