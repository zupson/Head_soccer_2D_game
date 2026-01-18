package hr.algebra.head_socker_2d_game.model.entities;

import org.dyn4j.dynamics.Body;

public class Player extends GameObject {

    private final Side side;
    private String playerName;


    private int scoredGoals;

    public Player(Body body, double width, double height, Side side) {
        super(body, width, height);
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    public int getScoredGoals() {
        return scoredGoals;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void addGoal() {
        scoredGoals++;
    }

    public void resetGoal() {
        scoredGoals = 0;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}