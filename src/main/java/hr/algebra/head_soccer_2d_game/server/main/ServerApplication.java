package hr.algebra.head_soccer_2d_game.server.main;

import hr.algebra.head_soccer_2d_game.server.game.GameEngine;

public class ServerApplication {
    public static void main(String[] args) {
        new GameEngine().init();
    }
}