package hr.algebra.head_soccer_2d_game.server.main;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.server.documentation.DocumentationGenerator;
import hr.algebra.head_soccer_2d_game.server.game.GameEngine;
import hr.algebra.head_soccer_2d_game.server.game.GameLoop;
import hr.algebra.head_soccer_2d_game.server.game.GameSingleton;
import hr.algebra.head_soccer_2d_game.server.game.factory.GameManagersFactory;
import hr.algebra.head_soccer_2d_game.server.manager.GameObjectManager;
import hr.algebra.head_soccer_2d_game.server.manager.GamePhysicManager;
import hr.algebra.head_soccer_2d_game.server.manager.GameStateManager;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteService;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteServiceImpl;
import hr.algebra.head_soccer_2d_game.server.threads.RmiServerThread;
import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

@Slf4j
public class ServerApplication {
    private static final int RANDOM_PORT_HINT = 0;

    public static void main(String[] args) throws InterruptedException {
        DocumentationGenerator.generateDocumentation(getBusinessLogicClasses());

        new GameEngine().init();
        new RmiServerThread().start();
        Thread.currentThread().join();
    }

    private static List<Class<?>> getBusinessLogicClasses() {
        return List.of(GameEngine.class,
                GameLoop.class,
                GameSingleton.class,
                GameManagersFactory.class,
                GameObjectManager.class,
                GamePhysicManager.class,
                GameStateManager.class);
    }

    public static void addRmiServer() {
        try {
            Registry registry = createRegistry();
            bindChatService(registry);
            log.info("Object registered in RMI registry");
        } catch (RemoteException e) {
            log.error("RMI server error", e);
        }
    }

    private static Registry createRegistry() throws RemoteException {
        return LocateRegistry.createRegistry(ConfigReader.getIntegerValueForKey(ConfigKey.RMI_PORT));
    }

    private static void bindChatService(Registry registry) throws RemoteException {
        ChatRemoteService chatRemoteService = new ChatRemoteServiceImpl();
        ChatRemoteService remote = (ChatRemoteService) UnicastRemoteObject.exportObject(chatRemoteService,
                RANDOM_PORT_HINT);
        registry.rebind(ChatRemoteService.REMOTE_OBJECT_NAME, remote);
    }
}