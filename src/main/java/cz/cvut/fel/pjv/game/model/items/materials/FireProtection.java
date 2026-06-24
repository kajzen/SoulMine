package cz.cvut.fel.pjv.game.model.items.materials;

import cz.cvut.fel.pjv.engine.model.ImageId;

/**
 * Represents a Fire Protection material item.
 */
public class FireProtection extends Material {
    public FireProtection(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.FIRE_PROTECTION;
    }
}
