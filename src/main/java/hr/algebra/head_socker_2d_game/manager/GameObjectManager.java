package hr.algebra.head_socker_2d_game.manager;

import hr.algebra.head_socker_2d_game.model.entities.*;
import org.dyn4j.dynamics.Body;

public class GameObjectManager {
    //object WIDTH and HEIGHT
    public static final double PLAYER_WIDTH = 1.0;
    public static final double PLAYER_HEIGHT = 1.8;
    public static final double GOAL_WIDTH = 0.3;
    public static final double GOAL_HEIGHT = 2.3;
    public static final double BALL_WIDTH = 0.5;
    public static final double BALL_HEIGHT = 0.5;
    public static final int FLOOR_WIDTH = 20;
    public static final double FLOOR_HEIGHT = 0.2;
    private static final double BOUNDARY_WALL_WIDTH = 0.2;
    private static final double BOUNDARY_WALL_HEIGHT = 7.0;
    private static final double CELLING_WIDTH = 20.0;
    private static final double CELLING_HEIGHT = 0.2;

    //object Start position X, Y
    public static final double GAME_OBJECT_POS_Y = 0.25;
    public static final double LEFT_PLAYER_POS_X = 1.0;
    public static final double RIGHT_PLAYER_POS_X = 9.0;
    public static final double LEFT_GOAL_FIX_POS_X = 0.3;
    public static final double RIGHT_GOAL_FIX_POS_X = 9.7;
    public static final double BALL_POS_X = 5.0;
//    public static final double BALL_POS_Y = 0.1;
    private static final double FLOOR_FIX_POS_X = 10.0;
    private static final double FLOOR_FIX_POS_Y = 0.2;

    private static final double RIGHT_WALL_BOUNDARY_FIX_POS_X = 10.0;
    private static final double RIGHT_WALL_BOUNDARY_FIX_POS_Y = 2.5;
    private static final double LEFT_WALL_BOUNDARY_FIX_POS_X = 0.0;
    private static final double LEFT_WALL_BOUNDARY_FIX_POS_Y = 2.5;
    private static final double CELLING_FIX_POS_X = 10.0;
    private static final double CELLING_FIX_POS_Y = 6.0;

    private Floor floor;
    private CeilingBoundary ceiling;
    private BoundaryWall leftBoundaryWall;
    private BoundaryWall rightBoundaryWall;
    private Player leftPlayer;
    private Player rightPlayer;
    private Goal leftGoal;
    private Goal rightGoal;
    private Ball ball;

    public void initGameObjectManager() {
        createFloor();
        createCeiling();
        createLeftBoundaryWall();
        createRightBoundaryWall();
        createPlayers();
        createGoals();
        createBall();

        setFloorFixedPosition();
        setCeilingFixedPosition();
        setLeftWallBoundaryFixedPosition();
        setRightWallBoundaryFixedPosition();
        setGoalsFixedPositions();
        setPlayersStartPositions();
        setBallStartPosition();
    }

    private void createRightBoundaryWall() {
        rightBoundaryWall = new BoundaryWall(new Body(), BOUNDARY_WALL_WIDTH, BOUNDARY_WALL_HEIGHT);
    }

    private void createLeftBoundaryWall() {
        leftBoundaryWall = new BoundaryWall(new Body(), BOUNDARY_WALL_WIDTH, BOUNDARY_WALL_HEIGHT);
    }

    private void createCeiling() {
        ceiling = new CeilingBoundary(new Body(), CELLING_WIDTH, CELLING_HEIGHT);
    }


    private void createFloor() {
        floor = new Floor(new Body(), FLOOR_WIDTH, FLOOR_HEIGHT);
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

    private void setRightWallBoundaryFixedPosition() {
        rightBoundaryWall.getBody().translate(RIGHT_WALL_BOUNDARY_FIX_POS_X, RIGHT_WALL_BOUNDARY_FIX_POS_Y);
    }

    private void setLeftWallBoundaryFixedPosition() {
        leftBoundaryWall.getBody().translate(LEFT_WALL_BOUNDARY_FIX_POS_X, LEFT_WALL_BOUNDARY_FIX_POS_Y);
    }

    private void setCeilingFixedPosition() {
        ceiling.getBody().translate(CELLING_FIX_POS_X, CELLING_FIX_POS_Y);
    }

    private void setFloorFixedPosition() {
        floor.getBody().translate(FLOOR_FIX_POS_X, FLOOR_FIX_POS_Y);
    }

    private void setPlayersStartPositions() {
        leftPlayer.getBody().translate(LEFT_PLAYER_POS_X, GAME_OBJECT_POS_Y);
        rightPlayer.getBody().translate(RIGHT_PLAYER_POS_X, GAME_OBJECT_POS_Y);
    }

    private void setGoalsFixedPositions() {
        leftGoal.getBody().translate(LEFT_GOAL_FIX_POS_X, GAME_OBJECT_POS_Y);
        rightGoal.getBody().translate(RIGHT_GOAL_FIX_POS_X, GAME_OBJECT_POS_Y);
    }

    private void setBallStartPosition() {
        ball.getBody().translate(BALL_POS_X, GAME_OBJECT_POS_Y);
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

    public Floor getFloor() {
        return floor;
    }

    public CeilingBoundary getCeiling() {
        return ceiling;
    }

    public BoundaryWall getLeftBoundaryWall() {
        return leftBoundaryWall;
    }

    public BoundaryWall getRightBoundaryWall() {
        return rightBoundaryWall;
    }
}
