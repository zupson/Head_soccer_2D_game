package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.PlayerProperty;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import hr.algebra.head_soccer_2d_game.shared.enums.PlayerType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DynamicPopUpUtils {

    public static void createStartPopUpDialog(PlayerType playerPlayer) {
        PlayerProperty playerProperty = new PlayerProperty();

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Player settings");

        Label insertPlayerName = new Label("Insert player name");
        TextField playerName = new TextField();
        playerName.setId("tfPlayerName");
        playerName.getText();

        Label choosePlayerColor = new Label("Choose player color");
        ColorPicker color = new ColorPicker();
        color.setId("cpColor");

        Button saveButton = new Button();
        saveButton.setId("btnSave");
        saveButton.setText("Save");
        saveButton.setOnAction(e -> {
            handleSave(playerPlayer, playerProperty, playerName, color);
            popupStage.close();
        });

        VBox layout = new VBox(10,
                insertPlayerName,
                playerName,
                choosePlayerColor,
                color,
                new HBox(10, saveButton)
        );
        layout.setPadding(new Insets(20));

        popupStage.setScene(new Scene(layout, 300, 220));
        popupStage.showAndWait();
    }

    private static void handleSave(PlayerType playerPlayer, PlayerProperty playerProperty, TextField playerName,
                                   ColorPicker color) {
        savePlayerProperty(playerPlayer, playerProperty, playerName, color);
        sendReadyCommand();
    }

    private static void savePlayerProperty(PlayerType playerPlayer, PlayerProperty playerProperty,
                                           TextField playerName, ColorPicker color) {
        playerProperty.setPlayerName(playerName.getText());
        playerProperty.setColor(color.getValue());
        playerProperty.setPlayerType(playerPlayer);
        XMLUtils.saveNewPlayerProp(playerProperty);
    }

    private static void sendReadyCommand() {
        NetworkUtils.sendGameCommand(GameState.WAITING_FOR_PLAYER);
    }
}