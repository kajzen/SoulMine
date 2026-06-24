package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.game.model.enemies.EnderMan;
import cz.cvut.fel.pjv.game.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityTest {

    Player player;
    EnderMan enderman;

    @BeforeEach
    void setUp() {
        player = new Player(132, 100, 32, 32, 100);
        enderman = new EnderMan(100, 100, 32, 32, 100);
    }

    @Test
    void testPlayerDamageLogic(){
        LogConfig.getLogger().info("Player damage test started");
        player.takeDamage(10);
        assert(player.getCurrentHp() == 90);
        LogConfig.getLogger().info("Player damage test completed successfully");
    }

    @Test
    void testEdgeDamageLogic(){
        LogConfig.getLogger().info("Edge damage test started");
        enderman.takeDamage(101);
        LogConfig.getLogger().info(String.valueOf(enderman.getCurrentHp()));
        assert(enderman.getCurrentHp() == 0 && !enderman.isStillAlive());
        LogConfig.getLogger().info("Edge damage test completed successfully");
    }

    @Test
    void testEdgeDamageLogic1(){
        LogConfig.getLogger().info("Edge damage test started");
        enderman.takeDamage(99);
        LogConfig.getLogger().info(String.valueOf(enderman.getCurrentHp()));
        assert(enderman.getCurrentHp() == 1 && enderman.isStillAlive());
        LogConfig.getLogger().info("Edge damage test completed successfully");
    }

}
