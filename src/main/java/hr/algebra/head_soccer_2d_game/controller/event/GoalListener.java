package hr.algebra.head_soccer_2d_game.controller.event;

import hr.algebra.head_soccer_2d_game.model.entities.enums.Side;

public interface GoalListener {
    void onGoalScored(Side side);
}
