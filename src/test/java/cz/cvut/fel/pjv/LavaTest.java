package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.engine.physics.CollisionDetector;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.world.LavaTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LavaTest {

    Player player;
    LavaTile lava;

    @BeforeEach
    void setUp() {
        player = new Player(132, 100, 32, 32, 100);
        lava = new LavaTile(100, 100, 32, 32);
    }

    @Test
    void testLavaNearPlayer() {
        LogConfig.getLogger().info("Lava test started");

        if (CollisionDetector.checkRectCollision(player, lava)){
            lava.onCollision(player);
            assertTrue(player.isStillAlive(), "Player should not be killed by lava");
        }
        LogConfig.getLogger().info("Lava test completed successfully");

    }

    @Test
    void testPlayerInLava(){
        LogConfig.getLogger().info("Lava test started");
        player.setX(131);
        if (CollisionDetector.checkRectCollision(player, lava)){
            lava.onCollision(player);
            assertFalse(player.isStillAlive(), "Player should be killed by lava");
        }
        LogConfig.getLogger().info("Lava test completed successfully");
    }
}
