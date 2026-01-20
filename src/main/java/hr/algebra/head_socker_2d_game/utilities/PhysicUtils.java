package hr.algebra.head_socker_2d_game.utilities;

import hr.algebra.head_socker_2d_game.model.entities.*;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

public class PhysicUtils {
    private PhysicUtils() {
    }

    public static void setupPlayerPhysics(Player player) {
        Body body = player.getBody();

        double headRadius = player.getHeight() / 2.0; // glava
        double footWidth = player.getWidth();        // širina kopačke = širina igrača
        double footHeight = player.getHeight() / 4;  // cca 1/4 visine

        // --- Glava ---
        BodyFixture headFixture = new BodyFixture(Geometry.createCircle(headRadius));
        headFixture.setDensity(1.0);
        headFixture.setFriction(0.5);       // blago trenje
        headFixture.setRestitution(0.1);    // malo odskoka
        headFixture.getShape().translate(0, footHeight/2 + headRadius); // pomak gore
        body.addFixture(headFixture);

        // --- Kopačka / tijelo ---
        BodyFixture footFixture = new BodyFixture(Geometry.createRectangle(footWidth, footHeight));
        footFixture.setDensity(1.0);
        footFixture.setFriction(1.0);       // visoko trenje s floor-om
        footFixture.setRestitution(0.0);
        footFixture.getShape().translate(0, 0); // centrirano
        body.addFixture(footFixture);

        // --- Masa i fizika ---
        body.setMass(MassType.NORMAL);

        // Sprječava rotaciju na starijim verzijama Dyn4j
        body.setAngularDamping(1.0);
        body.setAngularVelocity(0);

        body.setLinearDamping(0.2);      // smanjuje klizanje
        body.setUserData(player);
    }


    public static void setupGoalPhysics(Goal goal) {
        Body body = goal.getBody();
        double width = goal.getWidth();
        double height = goal.getHeight();
        double postWidth = 0.1;

        BodyFixture crossbar = new BodyFixture(Geometry.createRectangle(width, postWidth));
        crossbar.getShape().translate(0, height / 2 - postWidth / 2); // gore
        body.addFixture(crossbar);

        BodyFixture leftPost = new BodyFixture(Geometry.createRectangle(postWidth, height));
        leftPost.getShape().translate(-width/2 + postWidth/2, 0);
        body.addFixture(leftPost);

        BodyFixture rightPost = new BodyFixture(Geometry.createRectangle(postWidth, height));
        rightPost.getShape().translate(width/2 - postWidth/2, 0);
        body.addFixture(rightPost);

        body.setMass(MassType.INFINITE);
        body.setUserData(goal);
    }

    public static void setupBallPhysics(Ball ball) {
        Body body = ball.getBody();
        double radius = ball.getHeight() / 2.0; // loptu proporcionalno visini

        BodyFixture ballFixture = new BodyFixture(Geometry.createCircle(radius));
        ballFixture.setDensity(0.6);
        ballFixture.setFriction(0.3);
        ballFixture.setRestitution(0.8); // dobra elastičnost

        body.addFixture(ballFixture);
        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.3);
        body.setAngularDamping(0.3);
        body.setBullet(true);              // za preciznu fiziku lopte
        body.setUserData(ball);

        body.setAtRest(true);
        body.setLinearVelocity(0,0);
        body.setAngularVelocity(0);

    }

    public static void setupFloorPhysics(Floor floor) {
        Body body = floor.getBody();
        body.addFixture(new Rectangle(floor.getWidth(), floor.getHeight()));
        body.setMass(MassType.INFINITE);
        body.getFixtures().forEach(f -> f.setFriction(5.0)); // visoko trenje da player stoji
    }

    public static void setupBoundaryWallPhysics(BoundaryWall wall) {
        Body body = wall.getBody();

        // Kreiramo fixture za cijeli zid (Rectangle)
        BodyFixture wallFixture = new BodyFixture(
                new Rectangle(wall.getWidth(), wall.getHeight())
        );

        wallFixture.setFriction(5.0);      // visoko trenje da se player "ne kliza" po zidu
        wallFixture.setRestitution(0.8);   // elastičnost odbijanja lopte
        body.addFixture(wallFixture);

        // Statično tijelo
        body.setMass(MassType.INFINITE);

        // Sprema referencu na model
        body.setUserData(wall);
    }


    public static void setupBoundaryCeilingPhysics(CeilingBoundary ceiling) {
        Body body = ceiling.getBody();

        BodyFixture ceilingFixture = new BodyFixture(
                new Rectangle(ceiling.getWidth(), ceiling.getHeight())
        );

        ceilingFixture.setFriction(0.5);      // malo trenja
        ceilingFixture.setRestitution(0.8);   // elastično odbijanje lopte ili igrača
        body.addFixture(ceilingFixture);

        // Statično tijelo
        body.setMass(MassType.INFINITE);

        body.setUserData(ceiling);
    }

}