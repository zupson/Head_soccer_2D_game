package hr.algebra.head_soccer_2d_game.shared.event;

import hr.algebra.head_soccer_2d_game.shared.enums.Side;

public interface GoalListener {
    void onGoalScored(Side side);
}
