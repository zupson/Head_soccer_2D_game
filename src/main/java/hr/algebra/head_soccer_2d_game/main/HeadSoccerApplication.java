package hr.algebra.head_soccer_2d_game.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HeadSoccerApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/hr/algebra/head_soccer_2d_game/playground_view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Head Socker 2D Game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}