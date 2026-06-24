package cz.cvut.fel.pjv.game.controller;

import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.game.model.items.materials.Material;

/**
 * Represents a crafting recipe.
 */
public class CraftingRecipe {

    public final ImageId ingredient1; // first ingredient

    public final ImageId ingredient2; // second ingredient

    public final Material result;      // Result

    public CraftingRecipe(final ImageId in1, final ImageId in2, final Material res) {
        this.ingredient1 = in1;

        this.ingredient2 = in2;

        this.result = res;
    }
}