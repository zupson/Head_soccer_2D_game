package hr.algebra.head_soccer_2d_game.server.threads;

import hr.algebra.head_soccer_2d_game.server.main.ServerApplication;

public class RmiServerThread extends Thread{
    public RmiServerThread() {
        setDaemon(true);
    }

    @Override
    public void run() {
        ServerApplication.addRmiServer();
    }
}
