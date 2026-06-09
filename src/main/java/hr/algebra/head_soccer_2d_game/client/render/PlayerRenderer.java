package hr.algebra.head_soccer_2d_game.client.render;

import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.model.PlayerProperty;
import hr.algebra.head_soccer_2d_game.shared.constant.ColorConstants;
import hr.algebra.head_soccer_2d_game.shared.utilities.DrawUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.XMLUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class PlayerRenderer implements Drawable {
    private final GraphicsContext graphicsContext;
    private List<PlayerProperty> playerProperties;

    @Override
    public void draw(GameDataSnapshot gameDataSnapshot) {
        if (getPlayerProperties().isEmpty()) {
            drawDefaultPlayers(gameDataSnapshot);
            return;
        }

        drawPlayersFromProperties(gameDataSnapshot);
    }

    private List<PlayerProperty> getPlayerProperties() {
        if (playerProperties == null) {
            playerProperties = loadPlayerPropertiesFromXML();
        }
        return playerProperties;
    }

    private void drawDefaultPlayers(GameDataSnapshot gameDataSnapshot) {
        DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerOneX(),
                gameDataSnapshot.getPlayerOneY(), ColorConstants.PLAYER_COLOR);
        DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerTwoX(),
                gameDataSnapshot.getPlayerTwoY(), ColorConstants.PLAYER_COLOR);
    }

    private void drawPlayersFromProperties(GameDataSnapshot gameDataSnapshot) {
        for (PlayerProperty playerProperty : playerProperties) {
            switch (playerProperty.getPlayerType()) {
                case PLAYER_1 -> DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerOneX(),
                        gameDataSnapshot.getPlayerOneY(), playerProperty.getColor());
                case PLAYER_2 -> DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerTwoX(),
                        gameDataSnapshot.getPlayerTwoY(), playerProperty.getColor());
            }
        }
    }

    private List<PlayerProperty> loadPlayerPropertiesFromXML() {
        try {
            return XMLUtils.loadPlayerProps();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showPlayerName(Label lbLeftPlayer, Label lbRightPlayer) {
        for (PlayerProperty playerProperty : playerProperties) {
            switch (playerProperty.getPlayerType()) {
                case PLAYER_1 -> lbLeftPlayer.setText(playerProperty.getPlayerName());
                case PLAYER_2 -> lbRightPlayer.setText(playerProperty.getPlayerName());
            }
        }
    }
}