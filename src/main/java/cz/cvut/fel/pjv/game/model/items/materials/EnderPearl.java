package cz.cvut.fel.pjv.game.model.items.materials;

import cz.cvut.fel.pjv.engine.model.ImageId;

/**
 * Represents an Ender Pearl material item.
 */
public class EnderPearl extends Material {

    public EnderPearl(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.PEARL;
    }
}

