package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;


@UtilityClass
public class AlertUtils {

    public static void showSaveErrorAlert(IOException e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Greška");
        errorAlert.setContentText("Nije moguće spremiti igru: " + e.getMessage());
        errorAlert.showAndWait();
    }

    public static void showLoadSuccessAlert(Consumer<Boolean> onChoice) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Saved game found");
        alert.setHeaderText("You have a saved game!");
        alert.setContentText("Do you want to continue or start a new game?");

        var btnContinue = new ButtonType("Continue");
        var btnNewGame = new ButtonType("New Game");
        alert.getButtonTypes().setAll(btnContinue, btnNewGame);

        alert.showAndWait().ifPresent(btn ->
                onChoice.accept(btn.getText().equals("Continue"))
        );
    }

    public static Optional<ButtonType> showGameOverAlert(GameDataSnapshot snapshot) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game over");
        alert.setHeaderText(determineWinner(snapshot));
        alert.setContentText("Score: " + snapshot.getPlayerOneScore() + " - " + snapshot.getPlayerTwoScore()
                + "\nDo you want to play again?");

        var btnRerun = new ButtonType("Rerun");
        var btnClose = new ButtonType("Close");
        alert.getButtonTypes().setAll(btnRerun, btnClose);

        return alert.showAndWait();
    }
    private static String determineWinner(GameDataSnapshot gameDataSnapshot) {
        int player1 = gameDataSnapshot.getPlayerOneScore();
        int player2 = gameDataSnapshot.getPlayerTwoScore();
        if (player1 > player2) return "Player 1 wins!";
        if (player2 > player1) return "Player 2 wins!";
        return "It's a draw!";
    }

    public static void showQuitAlert(Runnable onConfirm) {
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        quitAlert.setTitle("Quit");
        quitAlert.setContentText("Are you sure?");

       quitAlert.showAndWait()
               .filter(btn-> btn==ButtonType.OK)
               .ifPresent(btn-> onConfirm.run());
    }
}