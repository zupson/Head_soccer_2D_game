package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.GameDataSnapshot;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
@Slf4j
@UtilityClass
public class FileUtils {
    private static final String SAVE_GAME_FILE_PATH = "game_save.dat";

    public static void saveGameToFile(GameDataSnapshot gameData) throws IOException {
        if (gameData == null) {
            log.warn("saveGame called with null snapshot, skipping.");
            return;
        }
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_GAME_FILE_PATH))) {
            out.writeObject(gameData);
        }
    }

    public static void deleteSavedGameData() {
        File file = new File(SAVE_GAME_FILE_PATH);
        if (!file.delete()) {
            log.warn("Failed to delete save file: {}", SAVE_GAME_FILE_PATH);
        }
    }
}