package cz.cvut.fel.pjv.game.model.items;

import cz.cvut.fel.pjv.engine.model.GameObject;
import cz.cvut.fel.pjv.engine.model.ImageId;

/**
 * Represents a weapon item in the game. Implemented only for sword.
 */
public class Weapon extends GameObject {
    public Weapon(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.SWORD;
    }
    private double damage;

    public void setWeaponDamage(double damage){
        this.damage = damage;
    }

    public double getWeaponDamage(){
        return damage;
    }
}
