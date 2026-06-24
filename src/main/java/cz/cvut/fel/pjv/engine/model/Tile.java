package cz.cvut.fel.pjv.engine.model;

import cz.cvut.fel.pjv.engine.model.interfaces.Interactable;
import cz.cvut.fel.pjv.game.model.entities.Player;

/**
 * Represents a tile in the game world.
 */
//Looking back, it was better to have Tile - abstract class and TileImpl - concrete class, but I realized it too late
public class Tile extends  GameObject implements Interactable {
    protected String interactionText = null;
    protected boolean showHint = false;
    protected String interactionButton = null;
    protected boolean isInteractable = false;
    protected boolean isSolid = false;

    public Tile(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    public void setShowHint(boolean show) { this.showHint = show; }

    public String getInteractionText() { return interactionText; }

    public boolean shouldShowHint() { return showHint; }

    public String getInteractionButton() {
        return interactionButton;
    }

    public boolean getIsInteractable() {
        return isInteractable;
    }


    public void setSolid(boolean solid) {
        isSolid = solid;
    }

    //Interactable tiles implement this method
    @Override
    public void interact(Player player) {

    }

    //Honestly using this only for lava tile
    public void onCollision(Player player) {
    }
}
