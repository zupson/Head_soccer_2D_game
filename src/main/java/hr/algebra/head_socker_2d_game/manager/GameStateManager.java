package hr.algebra.head_socker_2d_game.manager;

import hr.algebra.head_socker_2d_game.model.entities.GameState;

public class GameStateManager {
    private GameState currentState;

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
}
