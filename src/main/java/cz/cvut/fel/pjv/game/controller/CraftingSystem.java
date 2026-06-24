package cz.cvut.fel.pjv.game.controller;


import cz.cvut.fel.pjv.game.controller.exeptions.CraftingException;
import cz.cvut.fel.pjv.game.model.entities.Player;


/**
 * Handles the crafting process.
 */
public class CraftingSystem {

    /**
     * Crafts an ender eye.
     * @param craftingRecipe - recipe to craft the eye
     * @param player - player who is crafting the eye
     * @throws CraftingException - if the player doesn't have enough ingredients
     */
    public void craftEnderEye(CraftingRecipe craftingRecipe, Player player) throws CraftingException {
        int countPearls = player.countItems(craftingRecipe.ingredient1);
        int countFirePowders = player.countItems(craftingRecipe.ingredient2);
        if (countPearls > 0 && countFirePowders > 0){
            player.removeItem(craftingRecipe.ingredient1);
            player.removeItem(craftingRecipe.ingredient2);
            player.takeItem(craftingRecipe.result);
        }
        else
        {throw new CraftingException("Not enough ingredients");}

    }



}
