package cz.cvut.fel.pjv.game.model.world;

import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.game.controller.CraftingRecipe;
import cz.cvut.fel.pjv.game.controller.CraftingSystem;
import cz.cvut.fel.pjv.game.controller.exeptions.CraftingException;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.materials.EnderEye;

/**
 * Represents a crafting table in the game. Allows player to craft items.
 */
public class CraftingTable extends Tile {
    public CraftingTable(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.CRAFTING_TABLE;
        this.interactionText = "Press 'E' to craft";
        this.isInteractable = true;
        this.interactionButton = "E";
    }
    @Override
    public void interact(Player player) {
        player.setCrafting(true);
    }

    /**
     * Crafts an item using the player's inventory.
     * @param player - player who is crafting the item
     * @throws CraftingException - if the player doesn't have enough ingredients
     */
    public void craft(Player player) throws CraftingException {
        CraftingSystem craftingSystem = new CraftingSystem();
        craftingSystem.craftEnderEye(new CraftingRecipe(ImageId.PEARL, ImageId.FIRE_POWDER, new EnderEye(0,0,0,0)), player);
    }

}
