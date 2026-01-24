package hr.algebra.head_soccer_2d_game.utilities;

import hr.algebra.head_soccer_2d_game.model.entities.*;
import hr.algebra.head_soccer_2d_game.model.entities.enums.Side;
import hr.algebra.head_soccer_2d_game.model.entities.Simulable;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

public class PhysicUtils {
    private PhysicUtils() {}

    public static void setupPlayerPhysics(Player player) {
        var headFixture = createCircleFixture(player, 1.0, 0.5, 0.1);
        var footFixture = createRectangleFixture(player, 1.0, 1.0, 0.0);
        var body = player.getBody();
        body.addFixture(headFixture);
        body.addFixture(footFixture);
        setupBody(player, MassType.NORMAL, 0.2, 1.0, false);
        body.setUserData(player);
    }

    public static void setupGoalPhysics(Goal goal) {
        double width = goal.getWidth();
        double height = goal.getHeigh();
        double postWidth = 0.1;
        BodyFixture postFxture = null;

        var crossbar = new BodyFixture(Geometry.createRectangle(width, postWidth));
        crossbar.getShape().translate(0, height / 2 - postWidth / 2);
        if (goal.getSide() == Side.LEFT) {
            postFxture = new BodyFixture(Geometry.createRectangle(postWidth, height));
            postFxture.getShape().translate(-width / 2 + postWidth / 2, 0);
        } else if (goal.getSide() == Side.RIGHT) {
            postFxture = new BodyFixture(Geometry.createRectangle(postWidth, height));
            postFxture.getShape().translate(width / 2 - postWidth / 2, 0);
        }

        var body = goal.getBody();
        body.addFixture(crossbar);
        if (postFxture != null) {
            body.addFixture(postFxture);
        }
        body.setMass(MassType.INFINITE);
        body.setUserData(goal);
    }

    public static void setupBallPhysics(Ball ball) {
        var ballFixture = createCircleFixture(ball, 0.6, 0.3, 0.8);
        var body = ball.getBody();
        body.addFixture(ballFixture);
        setupBody(ball, MassType.NORMAL, 0.3, 0.3, true);
        body.setAtRest(true);
        body.setUserData(ball);
    }

    public static void setupFloorPhysics(Boundary floor) {
        setupFixedRectangle(floor, 5.0, 0);
    }

    public static void setupBoundaryWallPhysics(Boundary wall) {
        setupFixedRectangle(wall, 5.0, 0.8);
    }

    public static void setupBoundaryCeilingPhysics(Boundary ceiling) {
        setupFixedRectangle(ceiling, 0.5, 0.8);
    }


    private static BodyFixture createCircleFixture(Simulable s, double density, double friction, double restitution) {
        double radius = s.getHeigh() / 2.0;
        var fixture = new BodyFixture(Geometry.createCircle(radius));
        fixture.setDensity(density);
        fixture.setFriction(friction);
        fixture.setRestitution(restitution);
        return fixture;
    }

    private static BodyFixture createRectangleFixture(Simulable s, double density, double friction, double restitution) {
        double height = s.getHeigh();
        double width = s.getWidth();
        var fixture = new BodyFixture(new Rectangle(width, height));
        fixture.setDensity(density);
        fixture.setFriction(friction);
        fixture.setRestitution(restitution);
        return fixture;
    }

    private static void setupBody(Simulable s, MassType massType, double linearDamping, double angularDamping, boolean isBullet) {
        s.getBody().setLinearDamping(linearDamping);
        s.getBody().setAngularDamping(angularDamping);
        s.getBody().setMass(massType);
        s.getBody().setBullet(isBullet);
    }

    private static void setupFixedRectangle(Boundary b, double friction, double restitution) {
        var fixture = new BodyFixture(new Rectangle(b.getWidth(), b.getHeigh()));
        fixture.setFriction(friction);
        fixture.setRestitution(restitution);

        var body = b.getBody();
        body.addFixture(fixture);
        body.setMass(MassType.INFINITE);
        body.setUserData(b);
    }
}