package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.engine.io.GameSerializer;
import cz.cvut.fel.pjv.game.model.enemies.EnderMan;
import cz.cvut.fel.pjv.game.model.entities.Enemy;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.materials.EnderPearl;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameSerializerTest {

    @Test
    @DisplayName("Full Save/Load cycle integrity check")
    void testSaveLoadIntegrity() {
        // 1. Setup
        Player originalPlayer = new Player(150.5, 200.0, 48, 48, 300);
        originalPlayer.setCurrentHp(120);
        originalPlayer.takeItem(new EnderPearl(0,0,32,32));

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new EnderMan(500, 500, 48, 48, 200));

        // 2. Execution
        GameSerializer.saveGame(originalPlayer, enemies, new ArrayList<>(), originalPlayer.getMaterials(), new ArrayList<>());

        // 3. Loading back
        String savedData = GameSerializer.loadFromFile();
        assertNotNull(savedData, "Save file should exist");


        JSONObject root = new JSONObject(savedData);
        JSONObject pJson = root.getJSONObject("player");

        assertEquals(150.5, pJson.getDouble("x"), "Coordinates should match");
        assertEquals(120.0, pJson.getDouble("hp"), "HP should match");
        assertEquals(1, pJson.getJSONArray("inventory").length(), "Inventory size should match");
    }
}
