package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.game.controller.exeptions.CraftingException;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.materials.EnderPearl;
import cz.cvut.fel.pjv.game.model.items.materials.FirePowder;
import cz.cvut.fel.pjv.game.model.world.CraftingTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CraftingTest {
    Player player;
    CraftingTable table;
    @BeforeEach
    void setUp() {
        player = new Player(100, 100, 32, 32, 100);
        table = new CraftingTable(120, 120, 32, 32);
    }

    @Test
    void testSuccessfulCrafting() throws CraftingException {
        LogConfig.getLogger().info("Crafting test started");

        player.takeItem(new EnderPearl(0,0,32,32));
        player.takeItem(new FirePowder(0,0,32,32));

        table.craft(player);

        LogConfig.getLogger().info("Crafting test completed successfully");
        assertTrue(player.haveItem(ImageId.ENDER_EYE), "Result item should be in inventory");

        assertAll("Player items",
                () -> assertTrue(player.haveItem(ImageId.ENDER_EYE), "Result item should be in inventory"),
                () -> assertFalse(player.haveItem(ImageId.PEARL), "Ingredient 'Pearl' should be consumed"),
                () -> assertFalse(player.haveItem(ImageId.FIRE_POWDER), "Ingredient 'Fire Powder' should be consumed")
        );
    }

    @Test
    void testCraftingFailsWithoutIngredients() {
        LogConfig.getLogger().info("Crafting test started");
        player.takeItem(new EnderPearl(0,0,32,32));

        assertThrows(CraftingException.class, () -> {
            table.craft(player);
        }, "Crafting should throw exception when ingredients are missing");
    }

}
