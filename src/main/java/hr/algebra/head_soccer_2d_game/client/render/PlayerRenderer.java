package hr.algebra.head_soccer_2d_game.client.render;

import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.model.PlayerProperty;
import hr.algebra.head_soccer_2d_game.shared.constant.ColorConstants;
import hr.algebra.head_soccer_2d_game.shared.utilities.DrawUtils;
import hr.algebra.head_soccer_2d_game.shared.utilities.XMLUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@RequiredArgsConstructor
public class PlayerRenderer implements Drawable {
    private final GraphicsContext graphicsContext;

    @Override
    public void draw(GameDataSnapshot gameDataSnapshot) {
        List<PlayerProperty> props = loadPlayerPropertiesFromXML();
        if (props.isEmpty()) {
            drawDefaultPlayers(gameDataSnapshot);
            return;
        }
        drawPlayersFromProperties(props,gameDataSnapshot);
    }

    private void drawDefaultPlayers(GameDataSnapshot gameDataSnapshot) {
        DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerOneX(),
                gameDataSnapshot.getPlayerOneY(), ColorConstants.PLAYER_COLOR);
        DrawUtils.drawPlayer(graphicsContext, gameDataSnapshot.getPlayerTwoX(),
                gameDataSnapshot.getPlayerTwoY(), ColorConstants.PLAYER_COLOR);
    }

    private void drawPlayersFromProperties(List<PlayerProperty> props,GameDataSnapshot gameDataSnapshot) {
        for (PlayerProperty playerProperty : props) {
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
            log.error("Failed to load player properties from XML: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public void showPlayerName(Label lbLeftPlayer, Label lbRightPlayer) {
        List<PlayerProperty> props = loadPlayerPropertiesFromXML();
        for (PlayerProperty playerProperty : props) {
            switch (playerProperty.getPlayerType()) {
                case PLAYER_1 -> lbLeftPlayer.setText(playerProperty.getPlayerName());
                case PLAYER_2 -> lbRightPlayer.setText(playerProperty.getPlayerName());
            }
        }
    }
}