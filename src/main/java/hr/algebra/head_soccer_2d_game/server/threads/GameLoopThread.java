package hr.algebra.head_soccer_2d_game.server.threads;

import hr.algebra.head_soccer_2d_game.server.game.GameLoop;

public class GameLoopThread extends Thread {
    private final GameLoop gameLoop;

    public GameLoopThread(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        setDaemon(true);
    }
    @Override
    public void run() {
        while (!isInterrupted()) {
            gameLoop.timerTick();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
