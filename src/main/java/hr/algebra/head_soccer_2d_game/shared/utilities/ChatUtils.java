package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.client.jndi.ConfigKey;
import hr.algebra.head_soccer_2d_game.client.jndi.ConfigReader;
import hr.algebra.head_soccer_2d_game.client.main.HeadSoccerApplication;
import hr.algebra.head_soccer_2d_game.server.rmi.ChatRemoteService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Optional;

public class ChatUtils {

    public static Timeline getChatRefreshTimeline(ChatRemoteService chatRemoteService, TextArea chatMessageTextArea) {
        Timeline chatMessagesRefreshTimeLine = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            try {
                List<String> chatMessages = chatRemoteService.getAllMessages();

                StringBuilder textMessagesBuilder = new StringBuilder();
                for (String message : chatMessages) {
                    textMessagesBuilder.append(message).append("\n");
                }

                chatMessageTextArea.setText(textMessagesBuilder.toString());

            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }), new KeyFrame(Duration.seconds(1)));

        chatMessagesRefreshTimeLine.setCycleCount(Animation.INDEFINITE);
        return chatMessagesRefreshTimeLine;
    }

    public static void sendChatMessage(ChatRemoteService chatRemoteService, TextField chatMessageTextField) {
        String chatMessage = chatMessageTextField.getText();
        try {
            chatRemoteService.sendChatMessage(HeadSoccerApplication.playerType + ": " + chatMessage);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<ChatRemoteService> initializeChatRemoteService() {
        Optional<ChatRemoteService> chatRemoteServiceOptional = Optional.empty();
        try {
            Registry registry = LocateRegistry.getRegistry(
                    ConfigReader.getStringValue(ConfigKey.HOSTNAME),
                    ConfigReader.getIntegerValueForKey(ConfigKey.RMI_PORT));
            chatRemoteServiceOptional = Optional.of((ChatRemoteService) registry.lookup(ChatRemoteService.REMOTE_OBJECT_NAME));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return chatRemoteServiceOptional;
    }
}
