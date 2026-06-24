package cz.cvut.fel.pjv.game.model.items;

import cz.cvut.fel.pjv.engine.model.GameObject;

/**
 * Represents an item in the game. All items are extended from this class.
 */
public class Item extends GameObject {
    public Item(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

}
