package cz.cvut.fel.pjv.game.model.items.materials;

import cz.cvut.fel.pjv.engine.model.ImageId;

/**
 * Represents a Fire Powder material item.
 */
public class FirePowder extends  Material{
    public FirePowder(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.FIRE_POWDER;
    }
}
