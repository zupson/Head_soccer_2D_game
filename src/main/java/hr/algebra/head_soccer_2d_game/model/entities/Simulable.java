package hr.algebra.head_soccer_2d_game.model.entities;

import org.dyn4j.dynamics.Body;

public interface Simulable {
    double getWidth();
    double getHeigh();
    Body getBody();
}