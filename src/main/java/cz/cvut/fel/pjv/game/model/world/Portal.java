package cz.cvut.fel.pjv.game.model.world;

import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.engine.model.interfaces.Interactable;
import cz.cvut.fel.pjv.game.model.entities.Player;

/**
 * Represents a portal in the game. Allows the player to put the Ender Eye in the portal to activate it.
 */
public class Portal extends Tile implements Interactable {
    public Portal(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.PORTAL;
        this.isInteractable = true;
        this.interactionText = "Press 'E' to place the Eye";
        this.interactionButton = "E";
    }

    private boolean haveEye = false;

    public void setHaveEye(boolean haveEye){
        this.haveEye = haveEye;
    }

    public boolean isHaveEye() {
        return haveEye;
    }

    /**
     * Interact with the portal by placing the Ender Eye if the player has it and the portal doesn't already have one.
     * @param player - player who is interacting with the portal
     */
    @Override
    public void interact(Player player) {
        if(player.haveItem(ImageId.ENDER_EYE) && !this.haveEye){
            this.haveEye = true;
            player.removeItem(ImageId.ENDER_EYE);
        }
        else if (this.haveEye){
            LogConfig.getLogger().info("This portal already has Ender Eye");
        }
        else LogConfig.getLogger().info("Not enough items to place Ender Eye");
    }
}
