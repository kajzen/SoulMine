package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.game.model.enemies.EnderMan;
import cz.cvut.fel.pjv.game.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CombatTest {

    private Player player;
    private EnderMan enderman;

    @BeforeEach
    void setUp() {
        player = new Player(0, 0, 32, 32, 100);
        enderman = new EnderMan(10, 10, 32, 32, 200);
        enderman.setTarget(player);
    }

    @Test
    @DisplayName("Enemy attack cooldown should prevent machine-gun damage")
    void testEnemyAttackCooldown() {
        double initialHp = player.getCurrentHp();
        enderman.attack(player);
        double hpAfterFirstHit = player.getCurrentHp();
        assertTrue(hpAfterFirstHit < initialHp, "Player should take damage from the first hit");
        enderman.attack(player);
        assertEquals(hpAfterFirstHit, player.getCurrentHp(),
                "Player HP should not change during enemy attack cooldown");
    }

    @Test
    @DisplayName("Enemy should be able to attack again after cooldown reset")
    void testEnemyAttackAfterCooldownReset() {
        enderman.attack(player);
        double hpAfterFirstHit = player.getCurrentHp();
        for(int i = 0; i < 185; i++) {
            enderman.update();
        }
        enderman.attack(player);
        assertTrue(player.getCurrentHp() < hpAfterFirstHit,
                "Player should take damage again after cooldown expires");
    }
}