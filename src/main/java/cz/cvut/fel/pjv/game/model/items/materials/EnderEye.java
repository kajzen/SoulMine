package cz.cvut.fel.pjv.game.model.items.materials;

import cz.cvut.fel.pjv.engine.model.ImageId;

/**
 * Represents an Ender Eye material item.
 */
public class EnderEye extends Material{
    public EnderEye(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.ENDER_EYE;
    }
}
