package cz.cvut.fel.pjv.engine.io;


import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.game.model.enemies.EnderMan;
import cz.cvut.fel.pjv.game.model.enemies.Ifrit;
import cz.cvut.fel.pjv.game.model.enemies.Witch;
import cz.cvut.fel.pjv.game.model.entities.Enemy;
import cz.cvut.fel.pjv.game.model.entities.NPC;
import cz.cvut.fel.pjv.game.model.world.ActivatedPortal;
import cz.cvut.fel.pjv.game.model.world.CraftingTable;
import cz.cvut.fel.pjv.game.model.world.LavaTile;
import cz.cvut.fel.pjv.game.model.world.Portal;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for loading the game
 */
public class LevelLoader {
    public static GameLevel load(String  path) throws Exception {
        InputStream is = LevelLoader.class.getResourceAsStream(path);
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(path.replaceFirst("^/", ""));
        }
        if (is == null) {
            throw new FileNotFoundException("Missing level file: " + path);
        }

        String content = new String(is.readAllBytes());
        JSONObject root = new JSONObject(content);

        JSONObject spawn = root.getJSONObject("playerSpawn");
        double px = spawn.getDouble("x");
        double py = spawn.getDouble("y");

        List<Tile> tiles = new ArrayList<>();
        for (Object o : root.getJSONArray("tiles")) {
            JSONObject t = (JSONObject) o;
            Tile tile;
            switch (t.getString("type")){
                case "WALL":
                    tile = new Tile(t.getDouble("x"), t.getDouble("y"), 48, 48);
                    tile.setImageId(ImageId.WALL);
                    tile.setSolid(true);
                    break;
                case "PORTAL":
                    tile = new Portal(t.getDouble("x"), t.getDouble("y"), 48, 48);
                    tile.setImageId(ImageId.PORTAL);
                    break;
                case "ACT_PORTAL":
                    tile = new ActivatedPortal(t.getDouble("x"), t.getDouble("y"), 48, 48);
                    tile.setImageId(ImageId.ACT_PORTAL);
                    break;
                case "CRAFTING_TABLE":
                    tile = new CraftingTable(t.getDouble("x"), t.getDouble("y"), 48, 48);
                    tile.setImageId(ImageId.CRAFTING_TABLE);
                    tile.setSolid(true);
                    break;
                case "LAVA":
                    tile = new LavaTile(t.getDouble("x"), t.getDouble("y"), 48, 48);
                    tile.setImageId(ImageId.LAVA);
                    break;
                case "FLOOR":
                default:
                    tile = new Tile(t.getDouble("x"), t.getDouble("y"), 48, 48);
                    tile.setImageId(ImageId.FLOOR);
                    break;
            }
            tiles.add(tile);
        }

        List<Enemy> enemies = new ArrayList<>();
        List<NPC> npc = new ArrayList<>();
        for (Object o : root.getJSONArray("enemies")) {
            JSONObject e = (JSONObject) o;
            if (e.getString("type").equals("ENDERMAN")) {
                enemies.add(new EnderMan(e.getDouble("x"), e.getDouble("y"), 48, 48, 200));
            } else if (e.getString("type").equals("IFRIT")) {
                enemies.add(new Ifrit(e.getDouble("x"), e.getDouble("y"), 48, 48, 200));
            }
            else if (e.getString("type").equals("WITCH")) {
                enemies.add(new Witch(e.getDouble("x"), e.getDouble("y"), 48, 48, 200));
            }
        }
        for (Object o : root.getJSONArray("NPC")) {
            JSONObject e = (JSONObject) o;
            if (e.getString("type").equals("NPC")) {
                npc.add(new NPC(e.getDouble("x"), e.getDouble("y"), 200, 48, 48));
            }
        }

        return new GameLevel(px, py, tiles, enemies, npc);
    }
}
