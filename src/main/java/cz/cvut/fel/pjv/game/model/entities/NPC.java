package cz.cvut.fel.pjv.game.model.entities;

import cz.cvut.fel.pjv.engine.model.Entity;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.engine.model.interfaces.Interactable;

/**
 * Represents an NPC in the game.
 */
public class NPC extends Entity implements Interactable {
    protected boolean showHint = false;
    protected String interactionText = "Press 'E' to talk";
    public NPC(double x, double y, double maxHp, double width, double height) {
        super(x, y, width, height, maxHp);
        this.imageId = ImageId.NPC;
    }

    @Override
    public void update() {}

    @Override
    public void interact(Player player) {
        player.setTalking(true);
    }

    public boolean shouldShowHint() {
        return showHint;
    }
    public String getInteractionText() {
        return interactionText;
    }
    public void setShowHint(boolean show) { this.showHint = show; }

}
