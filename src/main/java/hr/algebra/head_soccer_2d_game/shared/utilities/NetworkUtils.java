package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.server.model.GameCommand;
import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import hr.algebra.head_soccer_2d_game.server.model.PlayerInput;
import hr.algebra.head_soccer_2d_game.shared.event.GameCommandListener;
import hr.algebra.head_soccer_2d_game.shared.event.GameDataListener;
import hr.algebra.head_soccer_2d_game.shared.event.PlayerInputListener;
import hr.algebra.head_soccer_2d_game.shared.threads.ReceiverThread;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
@UtilityClass
public class NetworkUtils {
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(3);
    public static void sendSnapshot(Serializable gameDataSnapshot, int port) {
        sendObject(gameDataSnapshot, port);
    }

    private static void sendObject(Serializable object, int port) {
        try (Socket socket = new Socket(ConfigReader.getStringValue(ConfigKey.HOSTNAME), port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void receiveSnapshot(int port, GameDataListener gameDataListener) {
        new ReceiverThread(port, socket -> processSnapshot(socket, gameDataListener), threadPool).start();
    }

    private static void processSnapshot(Socket clientSocket, GameDataListener gameDataListener) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            gameDataListener.onGameDataChanged((GameDataSnapshot) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error processing snapshot: {}", e.getMessage());
        }
    }

    public static void receivePlayerInput(int port, PlayerInputListener playerInputListener) {
        new ReceiverThread(port, socket -> processPlayerInput(socket, playerInputListener), threadPool).start();
    }

    private static void processPlayerInput(Socket clientSocket, PlayerInputListener playerInputListener) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            playerInputListener.onPlayerInput((PlayerInput) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error processing player input: {}", e.getMessage());
        }
    }

    public static void receiveGameCommand(int port, GameCommandListener gameCommandListener) {
        new ReceiverThread(port, socket -> processGameCommand(socket, gameCommandListener), threadPool).start();
    }

    private static void processGameCommand(Socket clientSocket, GameCommandListener gameCommandListener) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            gameCommandListener.onGameCommand((GameCommand) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error processing game command: {}", e.getMessage());
        }
    }

    public static void sendGameCommand(int port, GameCommand gameCommand) {
        sendObject(gameCommand, port);
    }
}