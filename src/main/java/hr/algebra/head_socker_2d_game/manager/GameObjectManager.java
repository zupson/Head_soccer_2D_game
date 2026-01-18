package hr.algebra.head_socker_2d_game.manager;

import hr.algebra.head_socker_2d_game.model.entities.Ball;
import hr.algebra.head_socker_2d_game.model.entities.Goal;
import hr.algebra.head_socker_2d_game.model.entities.Player;
import hr.algebra.head_socker_2d_game.model.entities.Side;
import org.dyn4j.dynamics.Body;

public class GameObjectManager {
    //object WIDTH and HEIGHT
    public static final double PLAYER_WIDTH = 1.0;
    public static final double PLAYER_HEIGHT = 1.8;
    public static final double GOAL_WIDTH = 0.3;
    public static final double GOAL_HEIGHT = 2.3;
    public static final double BALL_WIDTH = 0.5;
    public static final double BALL_HEIGHT = 0.5;

    //object Start position X, Y
    public static final double GAME_OBJECT_POSITION_Y = 0.1;
    public static final double LEFT_PLAYER_POSITION_X = 1.0;
    public static final double RIGHT_PLAYER_POSITION_X = 9.0;
    public static final double LEFT_GOAL_POSITION_X = 0.3;
    public static final double RIGHT_GOAL_POSITION_X = 9.7;
    public static final double BALL_POSITION_X = 5.0;
    public static final double BALL_POSITION_Y = 0.1;

    private Player leftPlayer;
    private Player rightPlayer;
    private Goal leftGoal;
    private Goal rightGoal;
    private Ball ball;

    public void initGameObjectManager() {
        createPlayers();
        createGoals();
        createBall();

        setPlayersStartPositions();
        setGoalsStartPositions();
        setBallStartPosition();
    }

    private void createPlayers() {
        leftPlayer = new Player(new Body(), PLAYER_WIDTH, PLAYER_HEIGHT, Side.LEFT);
        rightPlayer = new Player(new Body(), PLAYER_WIDTH, PLAYER_HEIGHT, Side.RIGHT);
    }

    private void createGoals() {
        leftGoal = new Goal(new Body(), GOAL_WIDTH, GOAL_HEIGHT, Side.LEFT);
        rightGoal = new Goal(new Body(), GOAL_WIDTH, GOAL_HEIGHT, Side.RIGHT);
    }

    private void createBall() {
        ball = new Ball(new Body(), BALL_WIDTH, BALL_HEIGHT);
    }

    private void setPlayersStartPositions() {
        leftPlayer.getBody().translate(LEFT_PLAYER_POSITION_X, GAME_OBJECT_POSITION_Y);
        rightPlayer.getBody().translate(RIGHT_PLAYER_POSITION_X, GAME_OBJECT_POSITION_Y);
    }

    private void setGoalsStartPositions() {
        leftGoal.getBody().translate(LEFT_GOAL_POSITION_X, GAME_OBJECT_POSITION_Y);
        rightGoal.getBody().translate(RIGHT_GOAL_POSITION_X, GAME_OBJECT_POSITION_Y);


    }

    private void setBallStartPosition() {
        ball.getBody().translate(BALL_POSITION_X, BALL_POSITION_Y);
    }

    public Player getLeftPlayer() {
        return leftPlayer;
    }

    public Player getRightPlayer() {
        return rightPlayer;
    }

    public Goal getLeftGoal() {
        return leftGoal;
    }

    public Goal getRightGoal() {
        return rightGoal;
    }

    public Ball getBall() {
        return ball;
    }
}
