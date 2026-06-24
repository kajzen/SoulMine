package cz.cvut.fel.pjv.engine.io;

import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.game.model.entities.Enemy;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.materials.Material;
import cz.cvut.fel.pjv.game.model.world.Portal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Class responsible for saving and loading the game state
 */
public class GameSerializer {

    private static final String SAVE_FILE = "save_game1.json";


    //Method that saves the game state to a file
    public static void saveGame(Player player, List<Enemy> enemies, List<Tile> tiles, List<Material> materials, List<Material> eyes) {
        JSONObject root = new JSONObject();

        JSONObject playerJson = getJsonObject(player);
        root.put("player", playerJson);

        // Saving enemies
        JSONArray enemiesJson = new JSONArray();
        for (Enemy e : enemies) {
            JSONObject enemyJson = new JSONObject();
            enemyJson.put("type", e.getClass().getSimpleName().toUpperCase());
            enemyJson.put("x", e.getX());
            enemyJson.put("y", e.getY());
            enemyJson.put("hp", e.getCurrentHp());
            enemiesJson.put(enemyJson);
        }
        root.put("enemies", enemiesJson);

        //Saving portal condition
        JSONArray portalsJson = new JSONArray();
        for (Tile t : tiles) {
            if (t instanceof Portal p) {
                JSONObject portalJson = new JSONObject();
                portalJson.put("x", p.getX());
                portalJson.put("y", p.getY());
                portalJson.put("haveEye", p.isHaveEye());
                portalsJson.put(portalJson);
            }
        }
        root.put("portals", portalsJson);

        //Saving materials (items on the floor) condition
        JSONArray materialsJson = new JSONArray();
        for (Material m : materials) {
            JSONObject mJson = new JSONObject();
            mJson.put("type", m.getImageId().name());
            mJson.put("x", m.getX());
            mJson.put("y", m.getY());
            materialsJson.put(mJson);
        }
        root.put("materials", materialsJson);

        //Saving eyes (if any portal having eyes on them) condition
        JSONArray eyesJson = new JSONArray();
        for (Material e : eyes) {
            JSONObject eJson = new JSONObject();
            eJson.put("type", e.getImageId().name());
            eJson.put("x", e.getX());
            eJson.put("y", e.getY());
            eyesJson.put(eJson);
        }
        root.put("eyes", eyesJson);

        //Saving to file
        try (FileWriter file = new FileWriter(SAVE_FILE)) {
            file.write(root.toString(4));
            file.flush();
            LogConfig.getLogger().info("Game has been successfully saved to a file: " + SAVE_FILE);
        } catch (IOException e) {
            LogConfig.getLogger().severe("Error while saving the game: " + e.getMessage());
        }
    }

    /**
     * Method that loads the game state from a file
     * @return null if the file does not exist, otherwise the game state as a string
     */
    public static String loadFromFile() {
        try {
            if (Files.exists(Paths.get(SAVE_FILE))) {
                return new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
            } else {
                LogConfig.getLogger().warning("Save file does not exist: " + SAVE_FILE);
                return null;
            }
        } catch (IOException e) {
            LogConfig.getLogger().severe("Error while loading the game: " + e.getMessage());
            return null;
        }
    }


    //Getting player's json object'
    private static JSONObject getJsonObject(Player player) {
        JSONObject playerJson = new JSONObject();
        playerJson.put("x", player.getX());
        playerJson.put("y", player.getY());
        playerJson.put("hp", player.getCurrentHp());
        playerJson.put("fireProtected", player.getFireProtected());
        JSONArray inventoryJson = new JSONArray();
        for (Material m : player.getMaterials()) {
            inventoryJson.put(m.getImageId().name());
        }
        playerJson.put("inventory", inventoryJson);
        return playerJson;
    }
}