package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@UtilityClass
public class FileUtils {
    private static final String SAVE_GAME_FILE_PATH = "game_save.dat";

    public static CompletableFuture<Void> saveGameToFileAsync(GameDataSnapshot gameData){

        return CompletableFuture.runAsync(() -> {
            if (gameData == null) {
                log.warn("saveGame called with null snapshot, skipping.");
                return;
            }
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_GAME_FILE_PATH))) {
                out.writeObject(gameData);
            } catch (IOException e) {
                log.error("Failed to save game to file: {}", e.getMessage());
            }
        });
    }

    public static void deleteSavedGameData() {
        File file = new File(SAVE_GAME_FILE_PATH);
        if (file.exists() && !file.delete()) {
            log.warn("Failed to delete save file: {}", SAVE_GAME_FILE_PATH);
        }
    }

    public static boolean savedGameExists() {
        return new File(SAVE_GAME_FILE_PATH).exists();
    }

    public static CompletableFuture<Optional<GameDataSnapshot>> loadGameFromFileAsync() {
        return CompletableFuture.supplyAsync(()->{
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_GAME_FILE_PATH))) {
                return Optional.of((GameDataSnapshot) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                log.error("Failed to load game from file: {}", e.getMessage());
                return Optional.empty();
            }
        });
    }
}