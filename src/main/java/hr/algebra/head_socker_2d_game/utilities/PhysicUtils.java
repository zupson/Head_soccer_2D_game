package hr.algebra.head_socker_2d_game.utilities;

import hr.algebra.head_socker_2d_game.model.entities.Ball;
import hr.algebra.head_socker_2d_game.model.entities.Goal;
import hr.algebra.head_socker_2d_game.model.entities.Player;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public class PhysicUtils {
    private PhysicUtils() {
    }

    public static void setupPlayerPhysics(Player player) {
        Body body = player.getBody();
        double headRadius = player.getHeight() / 2.0; // recimo 0.5
        double footWidth = 0.4;
        double footHeight = 0.2;

        // Glava
        BodyFixture headFixture = new BodyFixture(Geometry.createCircle(headRadius));
        headFixture.setDensity(1.0);
        headFixture.setFriction(0.4);
        headFixture.setRestitution(0.5);

        // Kopačka, odmah ispod glave
        BodyFixture footFixture = new BodyFixture(Geometry.createRectangle(footWidth, footHeight));
        footFixture.getShape().translate(0, -headRadius - footHeight / 2);
        footFixture.setDensity(1.0);
        footFixture.setFriction(0.6);
        footFixture.setRestitution(0.1);

        body.addFixture(headFixture);
        body.addFixture(footFixture);
        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.05);
        body.setAngularDamping(0.05);
        body.setUserData(player);
    }

    public static void setupGoalPhysics(Goal goal) {
        Body body = goal.getBody();
        double width = goal.getWidth();
        double height = goal.getHeight();
        double depth = 1.0;

        // Donji horizontalni dio gola (poprečna prečka)
        BodyFixture crossbar = new BodyFixture(Geometry.createRectangle(width, depth));
        crossbar.getShape().translate(0, height / 2);  // Prečka je na sredini visine
        body.addFixture(crossbar);

        // Uspravan vertikalni dio gola (stubovi)
        BodyFixture leftPost = new BodyFixture(Geometry.createRectangle(depth, height));
        leftPost.getShape().translate(-width / 2, 0);
        body.addFixture(leftPost);

        BodyFixture rightPost = new BodyFixture(Geometry.createRectangle(depth, height));
        rightPost.getShape().translate(width / 2, 0);
        body.addFixture(rightPost);

        body.setMass(MassType.INFINITE);
        body.setUserData(goal);
    }

    public static void setupBallPhysics(Ball ball) {
        Body body = ball.getBody();
        double radius = ball.getHeight() / 2.5;

        BodyFixture ballFixture = new BodyFixture(Geometry.createCircle(radius));
        ballFixture.setDensity(0.4);
        ballFixture.setFriction(0.5);
        ballFixture.setRestitution(0.9);

        body.addFixture(ballFixture);
        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.01);
        body.setAngularDamping(0.01);
        body.setBullet(true);
        body.setUserData(ball);
    }
}