package cz.cvut.fel.pjv.game.model.items.materials;

import cz.cvut.fel.pjv.engine.model.GameObject;

/**
 * Represents a material item. All materials are extended from this class.
 */
public class Material extends GameObject {
    public Material(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    protected Material material;
    protected String interactionText= "Press 'E' to pick up";
    protected boolean shouldShowHint = false;

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    public void setShouldShowHint(boolean shouldShowHint) {
        this.shouldShowHint = shouldShowHint;
    }

    public boolean shouldShowHint() {
        return shouldShowHint;
    }

    public String getInteractionText(){
        return interactionText;
    }
}
