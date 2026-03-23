package hr.algebra.head_soccer_2d_game.server.main;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.server.game.GameEngine;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteService;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteServiceImpl;
import hr.algebra.head_soccer_2d_game.shared.utilities.ChatUtils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerApplication {
    private static final int RANDOM_PORT_HINT = 0;

    public static void main(String[] args) {
        new GameEngine().init();
        new Thread(ServerApplication::addRmiServer).start();
    }

    private static void addRmiServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(ConfigReader.getIntegerValueForKey(ConfigKey.RMI_PORT));
            ChatRemoteService chatRemoteService = new ChatRemoteServiceImpl();
            ChatRemoteService remote = (ChatRemoteService) UnicastRemoteObject.exportObject(chatRemoteService, RANDOM_PORT_HINT);
            registry.rebind(ChatRemoteService.REMOTE_OBJECT_NAME, remote);
            System.out.println("Object registered in RMI registry");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}