package hr.algebra.head_soccer_2d_game.server.manager;

import hr.algebra.head_soccer_2d_game.shared.constant.DimenConstants;
import hr.algebra.head_soccer_2d_game.shared.constant.PosConstants;
import hr.algebra.head_soccer_2d_game.server.model.entities.*;
import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import org.dyn4j.dynamics.Body;

import java.util.ArrayList;
import java.util.List;

public class GameObjectManager {

    private final List<Player> players = new ArrayList<>();
    private final List<GameObject> gameFiledObjects = new ArrayList<>();

    private Boundary floor;
    private Boundary ceiling;
    private Boundary leftBoundaryWall;
    private Boundary rightBoundaryWall;
    private Player leftPlayer;
    private Player rightPlayer;
    private Goal leftGoal;
    private Goal rightGoal;
    private Ball ball;

    public void initGameObjectManager() {
        createFloorModel();
        createCeilingModel();
        createLeftBoundaryWallModel();
        createRightBoundaryWallModel();
        createPlayersModel();
        createGoalsModel();
        createBallModel();

        setFloorFixedPosition();
        setCeilingFixedPosition();
        setLeftWallBoundaryFixedPosition();
        setRightWallBoundaryFixedPosition();
        setGoalsFixedPositions();
        setPlayersStartPositions();
        setBallStartPosition();
    }

    private void createRightBoundaryWallModel() {
        rightBoundaryWall = new Boundary(new Body(), DimenConstants.VERTICAL_BOUNDARY_WIDTH.getValue(), DimenConstants.VERTICAL_BOUNDARY_HEIGHT.getValue());
        gameFiledObjects.add(rightBoundaryWall);
    }

    private void createLeftBoundaryWallModel() {
        leftBoundaryWall = new Boundary(new Body(), DimenConstants.VERTICAL_BOUNDARY_WIDTH.getValue(), DimenConstants.VERTICAL_BOUNDARY_HEIGHT.getValue());
        gameFiledObjects.add(leftBoundaryWall);
    }

    private void createCeilingModel() {
        ceiling = new Boundary(new Body(), DimenConstants.HORIZONTAL_BOUNDARY_WIDTH.getValue(), DimenConstants.HORIZONTAL_BOUNDARY_HEIGHT.getValue());
        gameFiledObjects.add(ceiling);
    }

    private void createFloorModel() {
        floor = new Boundary(new Body(), DimenConstants.HORIZONTAL_BOUNDARY_WIDTH.getValue(), DimenConstants.HORIZONTAL_BOUNDARY_HEIGHT.getValue());
        gameFiledObjects.add(floor);
    }

    private void createPlayersModel() {
        leftPlayer = new Player(new Body(), DimenConstants.PLAYER_WIDTH.getValue(), DimenConstants.PLAYER_HEIGHT.getValue(), Side.LEFT);
        players.add(leftPlayer);

        rightPlayer = new Player(new Body(), DimenConstants.PLAYER_WIDTH.getValue(), DimenConstants.PLAYER_HEIGHT.getValue(), Side.RIGHT);
        players.add(rightPlayer);
    }

    private void createGoalsModel() {
        leftGoal = new Goal(new Body(), DimenConstants.GOAL_WIDTH.getValue(), DimenConstants.GOAL_HEIGHT.getValue(), Side.LEFT);
        gameFiledObjects.add(leftGoal);

        rightGoal = new Goal(new Body(), DimenConstants.GOAL_WIDTH.getValue(), DimenConstants.GOAL_HEIGHT.getValue(), Side.RIGHT);
        gameFiledObjects.add(rightGoal);
    }

    private void createBallModel() {
        ball = new Ball(new Body(), DimenConstants.BALL_WIDTH.getValue(), DimenConstants.BALL_HEIGHT.getValue());
        gameFiledObjects.add(ball);
    }

    private void setRightWallBoundaryFixedPosition() {
        rightBoundaryWall.getBody().translate(PosConstants.RIGHT_WALL_FIX_POS_X.getValue(), PosConstants.RIGHT_WALL_FIX_POS_Y.getValue());
    }

    private void setLeftWallBoundaryFixedPosition() {
        leftBoundaryWall.getBody().translate(PosConstants.LEFT_WALL_FIX_POS_X.getValue(), PosConstants.LEFT_WALL_FIX_POS_Y.getValue());
    }

    private void setCeilingFixedPosition() {
        ceiling.getBody().translate(PosConstants.CELLING_FIX_POS_X.getValue(), PosConstants.CELLING_FIX_POS_Y.getValue());
    }

    private void setFloorFixedPosition() {
        floor.getBody().translate(PosConstants.FLOOR_FIX_POS_X.getValue(), PosConstants.FLOOR_FIX_POS_Y.getValue());
    }

    public void setPlayersStartPositions() {
        resetBody(leftPlayer.getBody(),
                PosConstants.LEFT_PLAYER_POS_X.getValue(),
                PosConstants.GAME_OBJECT_POS_Y.getValue());

        resetBody(rightPlayer.getBody(),
                PosConstants.RIGHT_PLAYER_POS_X.getValue(),
                PosConstants.GAME_OBJECT_POS_Y.getValue());
    }

    private void resetBody(Body body, double x, double y) {
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
        body.clearForce();

        body.getTransform().setTranslation(x, y);
        body.getTransform().setRotation(0);

        body.setAtRest(false);
    }

    private void setGoalsFixedPositions() {
        leftGoal.getBody().translate(PosConstants.LEFT_GOAL_FIX_POS_X.getValue(), PosConstants.GAME_OBJECT_POS_Y.getValue());
        rightGoal.getBody().translate(PosConstants.RIGHT_GOAL_FIX_POS_X.getValue(), PosConstants.GAME_OBJECT_POS_Y.getValue());
    }

    public void setBallStartPosition() {
        resetBody(ball.getBody(),
                PosConstants.BALL_POS_X.getValue(),
                PosConstants.GAME_OBJECT_POS_Y.getValue());
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

    public Boundary getFloor() {
        return floor;
    }

    public Boundary getCeiling() {
        return ceiling;
    }

    public Boundary getLeftBoundaryWall() {
        return leftBoundaryWall;
    }

    public Boundary getRightBoundaryWall() {
        return rightBoundaryWall;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<GameObject> getGameFiledObjects() {
        return gameFiledObjects;
    }
}