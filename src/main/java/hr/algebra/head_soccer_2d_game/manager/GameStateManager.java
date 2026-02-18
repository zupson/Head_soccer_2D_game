package hr.algebra.head_soccer_2d_game.manager;

import hr.algebra.head_soccer_2d_game.model.entities.enums.GameState;

public class GameStateManager {
    private GameState currentState;
    private boolean scoredGoalFlag;

    public GameStateManager() {
        currentState = GameState.MENU;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState newState) {
        if(newState != currentState) {
            currentState = newState;
            //Ako ce mi trebati callback metoda za reset kad se stanje promjeni(ovdje je pozovi)
        }
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    public boolean isScoredGoalFlag() {
        return scoredGoalFlag;
    }

    public void setScoredGoalFlag(boolean scoredGoalFlag) {
        this.scoredGoalFlag = scoredGoalFlag;
    }
}
