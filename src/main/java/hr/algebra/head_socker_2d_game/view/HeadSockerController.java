package hr.algebra.head_socker_2d_game.view;

import hr.algebra.head_socker_2d_game.game.context.GameContext;
import hr.algebra.head_socker_2d_game.game.loop.GameLoop;
import hr.algebra.head_socker_2d_game.manager.GameObjectManager;
import hr.algebra.head_socker_2d_game.manager.GamePhysicManager;
import hr.algebra.head_socker_2d_game.manager.GameStateManager;
import hr.algebra.head_socker_2d_game.model.entities.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

public class HeadSockerController {
    @FXML
    private Canvas gameCanvas;
    double SCENE_WIDTH = 1000;
    double SCENE_HEIGHT = 800;


    private GraphicsContext graphicContext;
    private GameObjectManager gameObjectManager;
    private GameStateManager gameStateManager;

    @FXML
    private void initialize() {

        GameContext currentInstance = GameContext.getCurrentInstance();
        GamePhysicManager gamePhysicManager = currentInstance.getGamePhysicManager();
        gameObjectManager = currentInstance.getGameObjectManager();
        gameStateManager = currentInstance.getGameStateManager();

        gameStateManager.setCurrentState(GameState.RUNNING); // <<< OVO

        graphicContext = gameCanvas.getGraphicsContext2D();

        // Čekaj da scena bude postavljena prije dodavanja KeyEvent listenera
        gameCanvas.setWidth(SCENE_WIDTH);
        gameCanvas.setHeight(SCENE_HEIGHT);

        gameCanvas.setFocusTraversable(true);
        gameCanvas.requestFocus();
        gameCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::handleKeyPress);
                newScene.setOnKeyReleased(this::handleKeyRelease);
            }
        });


        AnimationTimer gameLoop;
        gameLoop = new GameLoop(gameObjectManager, gamePhysicManager, gameStateManager, this);
        gameLoop.start();
    }


    private void handleKeyRelease(KeyEvent e) {
        Body body = gameObjectManager.getLeftPlayer().getBody();

        if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        KeyCode key = keyEvent.getCode();

        Body body = gameObjectManager.getLeftPlayer().getBody();
        double moveSpeed = 3.0; // brzina po ticku

        if (key == KeyCode.RIGHT) {
            body.applyImpulse(new Vector2(moveSpeed, 0));
        } else if (key == KeyCode.LEFT) {
            body.applyImpulse(new Vector2(-moveSpeed, 0));
        } else if (key == KeyCode.UP) {
            if (isPlayerOnFloor(gameObjectManager.getLeftPlayer())) {
                Vector2 vel = body.getLinearVelocity();
                vel.y = 8.0; // instant jump
                body.setLinearVelocity(vel);
            }
        }

    }

    private boolean isPlayerOnFloor(Player p) {
        double playerY = p.getBody().getTransform().getTranslationY();
        double floorY = gameObjectManager.getFloor().getBody().getTransform().getTranslationY() + GameObjectManager.FLOOR_HEIGHT / 2;

        // Malo tolerance da ne treba biti precizno
        return Math.abs(playerY - (floorY + GameObjectManager.PLAYER_HEIGHT / 2)) <= 0.05;
    }


    public void render() {
        if (graphicContext == null || gameObjectManager == null) return;

        final double SCALE = 100.0; // prilagodi po potrebi
        graphicContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());


        drawBoundary(gameObjectManager.getCeiling(), Color.SKYBLUE, SCALE);
        drawBoundary(gameObjectManager.getLeftBoundaryWall(), Color.SKYBLUE, SCALE);
        drawBoundary(gameObjectManager.getRightBoundaryWall(), Color.SKYBLUE, SCALE);

        drawFloor(gameObjectManager.getFloor(), Color.GREEN, SCALE);

        drawLeftGoal(gameObjectManager.getLeftGoal(), Color.BLACK, SCALE);
        drawRightGoal(gameObjectManager.getRightGoal(), Color.BLACK, SCALE);

        drawBall(gameObjectManager.getBall(), Color.GREY, SCALE);

        drawPlayer(gameObjectManager.getLeftPlayer(), Color.BLUE, SCALE);
        drawPlayer(gameObjectManager.getRightPlayer(), Color.RED, SCALE);
    }


    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private void drawFloor(Floor floor, Color color, double scale) {
        double width = floor.getWidth() * scale;
        double height = floor.getHeight() * scale;

        // Dyn4j centar floor-a
        double centerX = floor.getBody().getTransform().getTranslationX() * scale;
        double centerY = floor.getBody().getTransform().getTranslationY() * scale;

        // Flip Y: canvasHeight - centarY → donja strana
        double canvasY = gameCanvas.getHeight() - (centerY + height / 2);

        double canvasX = centerX - width / 2;

        graphicContext.setFill(color);
        graphicContext.fillRect(canvasX, canvasY, width, height);
    }

    private void drawBoundary(GameObject boundary, Color color, double scale) {
        double width = boundary.getWidth() * scale;
        double height = boundary.getHeight() * scale;

        // Dyn4j centar objekta
        double centerX = boundary.getBody().getTransform().getTranslationX() * scale;
        double centerY = boundary.getBody().getTransform().getTranslationY() * scale;

        // Flip Y: canvasHeight - centarY → donja strana
        double canvasY = gameCanvas.getHeight() - (centerY + height / 2);
        double canvasX = centerX - width / 2;

        graphicContext.setFill(color);
        graphicContext.fillRect(canvasX, canvasY, width, height);
    }


    private void drawLeftGoal(Goal g, Color color, double scale) {
        double baseX = GameObjectManager.GOAL_WIDTH * scale / 2;  // Postavi lijevi gol uz lijevi rub canvasa
        double baseY = gameCanvas.getHeight() - (g.getBody().getTransform().getTranslationY() * scale);

        // Dimenzije gola
        double goalWidth = GameObjectManager.GOAL_WIDTH * scale;
        double goalHeight = GameObjectManager.GOAL_HEIGHT * scale;

        graphicContext.setFill(color);

        // Donji horizontalni dio gola (poprečni prečka)
        double crossbarHeight = goalHeight / 10.0;
        graphicContext.fillRect(baseX - goalWidth / 2, baseY - goalHeight, goalWidth, crossbarHeight);

        // Uspravan vertikalni dio gola (stub)
        double postWidth = goalWidth / 3.0; // cca 20% širine gola
        graphicContext.fillRect(baseX - goalWidth / 2, baseY - goalHeight, postWidth, goalHeight);
    }

    private void drawRightGoal(Goal g, Color color, double scale) {
        double baseX = gameCanvas.getWidth() - GameObjectManager.GOAL_WIDTH * scale / 2;  // Postavi desni gol uz desni rub
        double baseY = gameCanvas.getHeight() - (g.getBody().getTransform().getTranslationY() * scale);

        // Dimenzije gola
        double goalWidth = GameObjectManager.GOAL_WIDTH * scale;
        double goalHeight = GameObjectManager.GOAL_HEIGHT * scale;

        graphicContext.setFill(color);

        // Donji horizontalni dio gola (poprečna prečka)
        double crossbarHeight = goalHeight / 10.0;

        // Pomak x da prečka ide lijevo (od desnog kraja)
        double crossbarX = baseX + goalWidth / 2 - goalWidth; // desna strana - goalWidth
        graphicContext.fillRect(crossbarX, baseY - goalHeight, goalWidth, crossbarHeight);

        // Uspravan vertikalni dio gola (stub)
        double postWidth = goalWidth / 3.0;
        // stub je sad uz desnu stranu prečke
        double postX = baseX + goalWidth / 2 - postWidth;
        graphicContext.fillRect(postX, baseY - goalHeight, postWidth, goalHeight);
    }


    private void drawBall(Ball b, Color color, double scale) {
        double x = b.getBody().getTransform().getTranslationX() * scale - (GameObjectManager.BALL_WIDTH * scale) / 2;
        double y = gameCanvas.getHeight() - (b.getBody().getTransform().getTranslationY() * scale) - (GameObjectManager.BALL_HEIGHT * scale);

        x = clamp(x, 0, gameCanvas.getWidth() - GameObjectManager.BALL_WIDTH * scale);
        y = clamp(y, 0, gameCanvas.getHeight() - GameObjectManager.BALL_HEIGHT * scale);

        graphicContext.setFill(color);
        graphicContext.fillOval(x, y, GameObjectManager.BALL_WIDTH * scale, GameObjectManager.BALL_HEIGHT * scale);
    }

    // SIDE-VIEW Y-FLIP za igrača
    private void drawPlayer(Player p, Color color, double scale) {
        // Izračunamo X i Y koordinatu igrača
        double x = p.getBody().getTransform().getTranslationX() * scale;
        double y = gameCanvas.getHeight() - (p.getBody().getTransform().getTranslationY() * scale);
        double headRadius = GameObjectManager.PLAYER_HEIGHT / 2.0 * scale;

        double footWidth = GameObjectManager.PLAYER_WIDTH * scale;
        double footHeight = GameObjectManager.PLAYER_HEIGHT / 4.0 * scale;

        // Ovdje klampamo y tako da igrač ostane na tlu
        y = clamp(y, 0, gameCanvas.getHeight() - footHeight);
        x = clamp(x, 0, gameCanvas.getWidth() - GameObjectManager.PLAYER_WIDTH * scale);

        // Crtanje glave (krug)
        graphicContext.setFill(color);
        graphicContext.fillOval(x - headRadius, y - headRadius * 2, headRadius * 2, headRadius * 2);  // Glava

        // Crtanje kopačke (pravokutnik)
        graphicContext.fillRect(x - footWidth / 2, y, footWidth, footHeight);  // Kopačka
    }

}
