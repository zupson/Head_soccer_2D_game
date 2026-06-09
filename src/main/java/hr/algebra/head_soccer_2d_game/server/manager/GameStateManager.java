package hr.algebra.head_soccer_2d_game.server.manager;

import hr.algebra.head_soccer_2d_game.shared.annotations.BusinessLogic;
import hr.algebra.head_soccer_2d_game.shared.enums.GameState;
import lombok.Getter;
import lombok.Setter;

@BusinessLogic(description = "Manages game state and transitions")
@Getter
public class GameStateManager {
    private GameState currentState;
    @Setter
    private boolean scoredGoalFlag;

    public GameStateManager() {
        currentState = GameState.MENU;
    }

    public void setCurrentState(GameState newState) {
        if (newState != currentState)
            currentState = newState;
    }
    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }
}