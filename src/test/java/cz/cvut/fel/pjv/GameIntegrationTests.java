package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.game.controller.exeptions.CraftingException;
import cz.cvut.fel.pjv.game.model.enemies.EnderMan;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.Weapon;
import cz.cvut.fel.pjv.game.model.items.materials.EnderPearl;
import cz.cvut.fel.pjv.game.model.items.materials.FirePowder;
import cz.cvut.fel.pjv.game.model.items.materials.FireProtection;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.game.model.world.CraftingTable;
import cz.cvut.fel.pjv.game.model.world.LavaTile;
import cz.cvut.fel.pjv.game.model.world.Portal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameIntegrationTests {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(0, 0, 32, 32, 200);
    }

    @Test
    @DisplayName("Integration: Player collects materials, crafts Eye, and activates Portal")
    void testFullPortalActivationFlow() throws CraftingException {
        player.takeItem(new EnderPearl(0, 0, 32, 32));
        player.takeItem(new FirePowder(0, 0, 32, 32));

        CraftingTable table = new CraftingTable(0, 0, 32, 32);
        table.craft(player);

        assertTrue(player.haveItem(ImageId.ENDER_EYE), "Player should have crafted the Eye");
        assertFalse(player.haveItem(ImageId.PEARL), "Pearl should be consumed");

        Portal portal = new Portal(0, 0, 32, 32);
        portal.interact(player);

        assertTrue(portal.isHaveEye(), "Portal should be activated with the Eye");
        assertFalse(player.haveItem(ImageId.ENDER_EYE), "Player should no longer have the Eye");
    }

    @Test
    @DisplayName("Integration: Player uses Fire Protection to survive Lava collision")
    void testPlayerSurvivesLavaWithPotionFlow() {
        player.takeItem(new FireProtection(0, 0, 32, 32));

        LavaTile lava = new LavaTile(0, 0, 32, 32);

        lava.interact(player);

        lava.onCollision(player);

        assertEquals(200, player.getCurrentHp(), "Player HP should remain full because of protection");
        assertTrue(player.getFireProtected(), "Player should have fire protection status active");
        assertFalse(player.haveItem(ImageId.FIRE_PROTECTION), "Potion should be consumed");
    }

    @Test
    @DisplayName("Integration: Player attacks and kills an Enderman")
    void testPlayerKillsEnemyFlow() {
        EnderMan enemy = new EnderMan(0, 0, 32, 32, 50); //50 HP
        Weapon sword = new Weapon(0, 0, 20, 20);
        sword.setWeaponDamage(30); //30

        enemy.takeDamage(sword.getWeaponDamage());
        assertEquals(20, enemy.getCurrentHp(), "Enemy should have 20 HP left");
        assertTrue(enemy.isStillAlive(), "Enemy should still be alive");

        enemy.takeDamage(sword.getWeaponDamage());
        assertEquals(0, enemy.getCurrentHp(), "Enemy HP should not drop below 0");
        assertFalse(enemy.isStillAlive(), "Enemy should be dead");
    }

    @Test
    @DisplayName("Integration: Enemy attacks Player and Player takes damage")
    void testEnemyAttacksPlayerFlow() {
        EnderMan enemy = new EnderMan(0, 0, 32, 32, 100);
        enemy.attack(player);

        assertEquals(190, player.getCurrentHp(), "Player should have taken 10 damage from Enderman");
    }
}