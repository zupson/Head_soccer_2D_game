package hr.algebra.head_socker_2d_game.manager;

import hr.algebra.head_socker_2d_game.utilities.PhysicUtils;
import org.dyn4j.world.World;

public class GamePhysicManager {
    private World world;

    public GamePhysicManager() {
        world = new World();
        world.setGravity(0, -9.81);
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

    public World getWorld() {
        return world;
    }

    public void update(double deltaTime) {
        world.update(deltaTime);
    }
}