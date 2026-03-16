package hr.algebra.head_soccer_2d_game.server.manager;

import hr.algebra.head_soccer_2d_game.shared.enums.GameState;

public class GameStateManager {
    private GameState currentState;
    private boolean scoredGoalFlag;

    private boolean localPlayerReady = false;
    private boolean remotePlayerReady = false;

    public GameStateManager() {
        currentState = GameState.MENU;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState newState) {
        if (newState != currentState) {
            currentState = newState;
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

    public void setLocalPlayerReady(boolean localPlayerReady) {
        this.localPlayerReady = localPlayerReady;
    }

    public void setRemotePlayerReady(boolean remotePlayerReady) {
        this.remotePlayerReady = remotePlayerReady;
    }

    public boolean areBothPlayersReady() {
        return localPlayerReady && remotePlayerReady;
    }
}
