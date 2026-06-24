package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.engine.physics.CollisionDetector;
import cz.cvut.fel.pjv.game.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollisionTest {

    Tile wall;
    Player player;

    @BeforeEach
    void setUp() {
        wall = new Tile(100, 100, 50, 50);
        player = new Player(120, 120, 32, 32, 100);
    }

    @Test
    void testRectCollisionDetected() {
        LogConfig.getLogger().info("Collision test started");
        assertTrue(CollisionDetector.checkRectCollision(wall, player), "Should collide with wall");
        LogConfig.getLogger().info("Collision test completed successfully");
    }

    @Test
    void testRectCollisionNotDetected() {
        LogConfig.getLogger().info("Collision test started");
        player.setX(150);
        player.setY(150);
        assertFalse(CollisionDetector.checkRectCollision(wall, player), "Should not collide with wall");
        LogConfig.getLogger().info("Collision test completed successfully");
    }
}
