package hr.algebra.head_soccer_2d_game.manager;

import hr.algebra.head_soccer_2d_game.controller.GoalListener;
import hr.algebra.head_soccer_2d_game.model.entities.Ball;
import hr.algebra.head_soccer_2d_game.model.entities.Goal;
import hr.algebra.head_soccer_2d_game.utilities.PhysicUtils;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.dynamics.contact.SolvedContact;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.World;
import org.dyn4j.world.listener.ContactListener;


public class GamePhysicManager implements ContactListener<Body> {
    private final World<Body> world;
    private GoalListener goalListener;

    public GamePhysicManager(GoalListener goalListener) {
        world = new World<>();
        world.setGravity(0, -9.81);
        world.addContactListener(this);
    }

    public void initPhysics(GameObjectManager gom) {
        setFloorPhysic(gom);
        setBoundaryWallsPhysics(gom);
        setBoundaryCeilingPhysics(gom);
        setGoalsPhysic(gom);
        setPlayersPhysic(gom);
        setBallPhysic(gom);

        addFloorToWorld(gom);
        addCeilingToWorld(gom);
        addWallsToWorld(gom);
        addGoalsToWorld(gom);
        addPlayersToWorld(gom);
        addBallToWorld(gom);
    }

    private void setBoundaryWallsPhysics(GameObjectManager gom) {
        PhysicUtils.setupBoundaryWallPhysics(gom.getRightBoundaryWall());
        PhysicUtils.setupBoundaryWallPhysics(gom.getLeftBoundaryWall());
    }

    private void setBoundaryCeilingPhysics(GameObjectManager gom) {
        PhysicUtils.setupBoundaryCeilingPhysics(gom.getCeiling());
    }

    private void setFloorPhysic(GameObjectManager gom) {
        PhysicUtils.setupFloorPhysics(gom.getFloor());
    }

    private static void setPlayersPhysic(GameObjectManager gom) {
        PhysicUtils.setupPlayerPhysics(gom.getLeftPlayer());
        PhysicUtils.setupPlayerPhysics(gom.getRightPlayer());
    }

    private static void setGoalsPhysic(GameObjectManager gom) {
        PhysicUtils.setupGoalPhysics(gom.getLeftGoal());
        PhysicUtils.setupGoalPhysics(gom.getRightGoal());
    }

    private static void setBallPhysic(GameObjectManager gom) {
        PhysicUtils.setupBallPhysics(gom.getBall());
    }

    public void setGoalListener(GoalListener goalListener) {
        this.goalListener = goalListener;
    }

    private void addFloorToWorld(GameObjectManager gom) {
        world.addBody(gom.getFloor().getBody());
    }

    private void addCeilingToWorld(GameObjectManager gom) {
        world.addBody(gom.getCeiling().getBody());
    }

    private void addWallsToWorld(GameObjectManager gom) {
        world.addBody(gom.getRightBoundaryWall().getBody());
        world.addBody(gom.getLeftBoundaryWall().getBody());
    }

    private void addPlayersToWorld(GameObjectManager gom) {
        world.addBody(gom.getLeftPlayer().getBody());
        world.addBody(gom.getRightPlayer().getBody());
    }

    private void addGoalsToWorld(GameObjectManager gom) {
        world.addBody(gom.getRightGoal().getBody());
        world.addBody(gom.getLeftGoal().getBody());
    }

    private void addBallToWorld(GameObjectManager gom) {
        world.addBody(gom.getBall().getBody());
    }

    public void update(double deltaTime) {
        world.update(deltaTime);
    }

    @Override
    public void begin(ContactCollisionData<Body> contactCollisionData, Contact contact) {
        var f1 = contactCollisionData.getFixture1();
        var f2 = contactCollisionData.getFixture2();
        checkGoal(f1, f2);
        checkGoal(f2, f1);
    }

    private void checkGoal(BodyFixture sensor, BodyFixture other) {
        if (!sensor.isSensor()) return;
        var sensorBody = sensor.getUserData();
        var otherBody = other.getUserData();
        if (sensorBody instanceof Goal goal && otherBody instanceof Ball ball) {
            if(goalListener != null){
                goalListener.onGoalScored(goal.getSide(), goal.getScore());
            }
            System.out.println("GOAL SCORE: " + goal.getScore() + " " + goal.getSide());
        }
    }

    @Override
    public void persist(ContactCollisionData<Body> contactCollisionData, Contact contact, Contact contact1) {

    }

    @Override
    public void end(ContactCollisionData<Body> contactCollisionData, Contact contact) {

    }

    @Override
    public void destroyed(ContactCollisionData<Body> contactCollisionData, Contact contact) {

    }

    @Override
    public void collision(ContactCollisionData<Body> contactCollisionData) {

    }

    @Override
    public void preSolve(ContactCollisionData<Body> contactCollisionData, Contact contact) {

    }

    @Override
    public void postSolve(ContactCollisionData<Body> contactCollisionData, SolvedContact solvedContact) {

    }
}