package hr.algebra.head_soccer_2d_game.shared.utilities;

import hr.algebra.head_soccer_2d_game.server.model.entities.GameDataSnapshot;

import java.io.*;

public class FileUtils {
    private FileUtils() {
    }

    private static final String SAVE_GAME_FILE_PATH = "game_save.dat";

    public static void saveGame(GameDataSnapshot gameData) throws IOException {
        if (gameData == null) {
            System.err.println("WARNING: saveGame called with null snapshot, skipping.");
            return;
        }
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_GAME_FILE_PATH))) {
            out.writeObject(gameData);
        }
    }

    public static GameDataSnapshot loadGame() throws IOException, ClassNotFoundException {
        try(ObjectInputStream in = new ObjectInputStream( new FileInputStream(SAVE_GAME_FILE_PATH))) {
            return (GameDataSnapshot) in.readObject();
        }
    }

    public static void deleteSave() {
        new File(SAVE_GAME_FILE_PATH).delete();
    }
}