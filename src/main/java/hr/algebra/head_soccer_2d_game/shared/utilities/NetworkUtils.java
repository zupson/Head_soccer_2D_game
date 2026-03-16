package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.entities.GameCommand;
import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.model.entities.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.constant.NetworkConstants;
import hr.algebra.head_soccer_2d_game.shared.event.GameCommandListener;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.event.PlayerInputListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkUtils {
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(4); // 2 - Playerinput(player1, player2), 1 - gameCommand, 1 - za svaki slucaj

    public static void sendSnapshot(Serializable gameDataSnapshot, int port) {
        try (Socket clientSocket = new Socket(NetworkConstants.HOST.getValue().toString(), port)) {
            writeSnapshot(clientSocket, gameDataSnapshot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeSnapshot(Socket clientSocket, Serializable gameDataSnapshot) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(gameDataSnapshot);
    }


    public static void receiveSnapshot(int port, GameDataListener gameDataListener) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(port));
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> processSnapshot(clientSocket, gameDataListener));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void processSnapshot(Socket clientSocket, GameDataListener gameDataListener) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            GameDataSnapshot gameDataSnapshot = (GameDataSnapshot) ois.readObject();
            gameDataListener.onGameDataChanged(gameDataSnapshot);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void receivePlayerInput(int port, PlayerInputListener playerInputListener) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(port));
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> processPlayerInput(clientSocket, playerInputListener));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void processPlayerInput(Socket clientSocket, PlayerInputListener playerInputListener) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            PlayerInput playerInputData = (PlayerInput) ois.readObject();
            playerInputListener.onPlayerInput(playerInputData);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void receiveGameCommand(int port, GameCommandListener gameCommandListener) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(port));
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(() -> processGameCommand(gameCommandListener, clientSocket));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void processGameCommand(GameCommandListener gameCommandListener, Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            GameCommand command = (GameCommand) ois.readObject();
            gameCommandListener.onGameCommand(command);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}