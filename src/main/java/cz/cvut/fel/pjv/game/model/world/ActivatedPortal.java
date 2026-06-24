package cz.cvut.fel.pjv.game.model.world;

import cz.cvut.fel.pjv.engine.model.ImageId;

/**
 * Represents an activated portal. (That black stuff inside the portal that has all 12 eyes)
 */
public class ActivatedPortal extends Portal{
    private boolean activate = false;
    public ActivatedPortal(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.ACT_PORTAL;
        this.isInteractable = false; //to not make possible to place eye on it, because it extends Portal class
    }

    public boolean getActivate(){
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
