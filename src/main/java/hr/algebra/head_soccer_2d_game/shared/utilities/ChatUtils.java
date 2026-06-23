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
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Optional;
@Slf4j
@UtilityClass
public class ChatUtils {

    public static Timeline getChatRefreshTimeline(ChatRemoteService chatRemoteService,
                                                  TextArea chatMessageTextArea) {
        Timeline chatMessagesRefreshTimeLine = new Timeline(
                new KeyFrame(Duration.ZERO,
                        e -> refreshChatMessages(chatRemoteService, chatMessageTextArea)),
                new KeyFrame(Duration.seconds(1)));

        chatMessagesRefreshTimeLine.setCycleCount(Animation.INDEFINITE);
        return chatMessagesRefreshTimeLine;
    }

    private static void refreshChatMessages(ChatRemoteService chatRemoteService, TextArea chatMessageTextArea) {
        try {
            List<String> chatMessages = chatRemoteService.getAllMessages();
            chatMessageTextArea.setText(String.join("\n", chatMessages));
        } catch (RemoteException ex) {
            log.error("Chat error: {}", ex.getMessage());
        }
    }

    public static void sendChatMessage(ChatRemoteService chatRemoteService, TextField chatMessageTextField) {
        String chatMessage = chatMessageTextField.getText();
        try {
            chatRemoteService.sendChatMessage(HeadSoccerApplication.getPlayerType() + ": " + chatMessage);
        } catch (RemoteException ex) {
            log.error("Send chat error: {}", ex.getMessage());
        }
    }

    public static Optional<ChatRemoteService> initializeChatRemoteService() {
        try {
            Registry registry = LocateRegistry.getRegistry(
                    ConfigReader.getStringValue(ConfigKey.HOSTNAME),
                    ConfigReader.getIntegerValueForKey(ConfigKey.RMI_PORT));
            return Optional.of((ChatRemoteService) registry.lookup(ChatRemoteService.REMOTE_OBJECT_NAME));
        } catch (RemoteException | NotBoundException e) {
            log.error("RMI init error: {}", e.getMessage());
            return Optional.empty();
        }
    }
}