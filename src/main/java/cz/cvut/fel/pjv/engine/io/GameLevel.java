package cz.cvut.fel.pjv.engine.io;

import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.game.model.entities.Enemy;
import cz.cvut.fel.pjv.game.model.entities.NPC;

import java.util.List;

/**
 * Represents a game level with spawn coordinates, tiles, enemies, and NPCs.
 */
public record GameLevel(double spawnX, double spawnY, List<Tile> tiles, List<Enemy> enemies, List<NPC> npc) {
}
