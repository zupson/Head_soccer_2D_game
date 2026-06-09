package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.*;
import hr.algebra.head_soccer_2d_game.shared.enums.Side;
import lombok.experimental.UtilityClass;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

@UtilityClass
public class PhysicUtils {
    private static final double POST_WIDTH = 0.1;

    public static void setupPlayerPhysics(Player player) {
        var headFixture = createCircleFixture(player, 1.0, 0.5, 0.5);
        var footFixture = createRectangleFixture(player);
        var body = player.getBody();
        body.addFixture(headFixture);
        body.addFixture(footFixture);
        setupBody(player, 0.2, 1.0, false);
        body.setUserData(player);
    }

    public static void setupGoalPhysics(Goal goal) {
        BodyFixture crossbar = createGoalCrossbar(goal);
        BodyFixture postFixture = createGoalPost(goal);
        postFixture.setSensor(true);
        postFixture.setUserData(goal);
        setupGoalBody(goal, postFixture, crossbar);
    }

    private static BodyFixture createGoalCrossbar(Goal goal) {
        BodyFixture crossbar = new BodyFixture(Geometry.createRectangle(goal.getWidth(), POST_WIDTH));
        crossbar.getShape().translate(0, goal.getHeigh() / 2 - POST_WIDTH / 2);
        return crossbar;
    }

    private static BodyFixture createGoalPost(Goal goal) {
        BodyFixture postFixture = null;
        if (goal.getSide() == Side.LEFT) {
            postFixture = new BodyFixture(Geometry.createRectangle(POST_WIDTH, goal.getHeigh()));
            postFixture.getShape().translate(-(goal.getWidth()) / 2 + POST_WIDTH / 2, 0);
            return postFixture;
        } else if (goal.getSide() == Side.RIGHT) {
            postFixture = new BodyFixture(Geometry.createRectangle(POST_WIDTH, goal.getHeigh()));
            postFixture.getShape().translate(goal.getWidth() / 2 - POST_WIDTH / 2, 0);
            return postFixture;
        }
        throw new IllegalArgumentException("Unknown side: " + goal.getSide());
    }

    private static void setupGoalBody(Goal goal, BodyFixture postFixture, BodyFixture crossbar) {
        var body = goal.getBody();
        body.setMass(MassType.INFINITE);
        body.setUserData(goal);
        body.addFixture(postFixture);
        body.addFixture(crossbar);
    }

    public static void setupBallPhysics(Ball ball) {
        var ballFixture = createCircleFixture(ball, 0.6, 0.3, 0.9);
        ballFixture.setUserData(ball);
        var body = ball.getBody();
        body.addFixture(ballFixture);
        setupBody(ball, 0.3, 0.3, true);
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

    private static BodyFixture createRectangleFixture(Simulable s) {
        double height = s.getHeigh();
        double width = s.getWidth();
        var fixture = new BodyFixture(new Rectangle(width, height));
        fixture.setDensity(1.0);
        fixture.setFriction(1.0);
        fixture.setRestitution(0.0);
        return fixture;
    }

    private static void setupBody(Simulable s, double linearDamping, double angularDamping, boolean isBullet) {
        s.getBody().setLinearDamping(linearDamping);
        s.getBody().setAngularDamping(angularDamping);
        s.getBody().setMass(MassType.NORMAL);
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