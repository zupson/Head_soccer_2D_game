package hr.algebra.head_soccer_2d_game.client.render;

import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameRender {
    private final GraphicsContext graphicContext;
    private final PlayerRenderer playerRenderer;
    private final GameFieldRenderer gameFieldRenderer;
    private final Label lbLeftPlayer;
    private final Label lbRightPlayer;
    private final Label lbLeftPlayerScore;
    private final Label lbRightPlayerScore;
    private final Label lbTimer;

    public void render(GameDataSnapshot gameDataSnapshot) {
        if (graphicContext == null) return;
        graphicContext.clearRect(0, 0,
                graphicContext.getCanvas().getWidth(),
                graphicContext.getCanvas().getHeight());
        gameFieldRenderer.draw(gameDataSnapshot);
        playerRenderer.draw(gameDataSnapshot);
        playerRenderer.showPlayerName(lbLeftPlayer, lbRightPlayer);

        updateScoreLabel(gameDataSnapshot.getPlayerOneScore(),
                gameDataSnapshot.getPlayerTwoScore());
        updateTimerLabel(gameDataSnapshot.getRemainingTime());
    }

    private void updateScoreLabel(int leftPlayer, int rightPlayer) {
        lbLeftPlayerScore.setText(String.valueOf(leftPlayer));
        lbRightPlayerScore.setText(String.valueOf(rightPlayer));
    }

    private void updateTimerLabel(double time) {
        lbTimer.setText(String.format("%d", (int) time));
    }
}