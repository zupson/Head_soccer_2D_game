package hr.algebra.head_soccer_2d_game.shared.threads;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class ReceiverThread extends Thread {
    private final int port;
    private final Consumer<Socket> processor;
    private final ExecutorService threadPool;

    public ReceiverThread(int port, Consumer<Socket> processor, ExecutorService threadPool) {
        this.port = port;
        this.processor = processor;
        this.threadPool = threadPool;
        setDaemon(true);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));
            while (!isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> processor.accept(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
