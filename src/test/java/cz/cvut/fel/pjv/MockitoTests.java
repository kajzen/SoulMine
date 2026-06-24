package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.game.controller.CraftingRecipe;
import cz.cvut.fel.pjv.game.controller.CraftingSystem;
import cz.cvut.fel.pjv.game.controller.exeptions.CraftingException;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.materials.EnderEye;
import cz.cvut.fel.pjv.game.model.world.LavaTile;
import cz.cvut.fel.pjv.game.model.world.Portal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockitoTests {

    @Test
    @DisplayName("Mockito: CraftingSystem should remove ingredients and add result to Player")
    void testCraftingSystemUsesPlayerMethods() throws CraftingException {
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.countItems(ImageId.PEARL)).thenReturn(1);
        when(mockPlayer.countItems(ImageId.FIRE_POWDER)).thenReturn(1);

        CraftingSystem craftingSystem = new CraftingSystem();
        CraftingRecipe recipe = new CraftingRecipe(ImageId.PEARL, ImageId.FIRE_POWDER, new EnderEye(0, 0, 32, 32));

        craftingSystem.craftEnderEye(recipe, mockPlayer);

        verify(mockPlayer, times(1)).removeItem(ImageId.PEARL);
        verify(mockPlayer, times(1)).removeItem(ImageId.FIRE_POWDER);
        verify(mockPlayer, times(1)).takeItem(any(EnderEye.class));
    }

    @Test
    @DisplayName("Mockito: LavaTile should consume Fire Protection potion from Player")
    void testLavaTileConsumesPotion() {
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.haveItem(ImageId.FIRE_PROTECTION)).thenReturn(true);

        LavaTile lava = new LavaTile(0, 0, 32, 32);
        lava.interact(mockPlayer);

        verify(mockPlayer, times(1)).setFireProtected(true);
        verify(mockPlayer, times(1)).removeItem(ImageId.FIRE_PROTECTION);
    }

    @Test
    @DisplayName("Mockito: Portal should take Ender Eye from Player upon interaction")
    void testPortalTakesEyeFromPlayer() {
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.haveItem(ImageId.ENDER_EYE)).thenReturn(true);

        Portal portal = new Portal(0, 0, 32, 32);

        portal.interact(mockPlayer);

        verify(mockPlayer, times(1)).removeItem(ImageId.ENDER_EYE);
        assertTrue(portal.isHaveEye(), "Portal should now have the eye");
    }
}