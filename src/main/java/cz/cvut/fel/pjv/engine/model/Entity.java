package cz.cvut.fel.pjv.engine.model;

import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.engine.model.interfaces.Damageable;
import cz.cvut.fel.pjv.engine.model.interfaces.Updatable;

import java.util.List;

import static cz.cvut.fel.pjv.engine.physics.CollisionDetector.checkRectCollision;

/**
 * Represents an entity abstract class in the game.
 */
public abstract class Entity extends GameObject implements Updatable, Damageable{

    protected double maxHp, currentHp;
    protected boolean active;
    protected double speed;
    protected double dx;
    protected double dy;

    public Entity(double x, double y, double width, double height, double maxHp) {
        super(x, y, width, height);
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.active = true;
    }

    /**
     * Moves the entity based on its current velocity and checks for collisions with tiles.
     * @param tiles List of tiles to check for collisions
     */
    public void move(List<Tile> tiles){
        // Check X axis
        Tile playerGhostX = new Tile(this.getX() + this.getDx(), this.getY(), this.getWidth(), this.getHeight());
        boolean entityCanMoveX = true;
        for (Tile t : tiles) {
            if ((t.isSolid) && checkRectCollision(playerGhostX, t)) {
                entityCanMoveX = false;
                break;
            }
        }
        if (entityCanMoveX) {
            this.setX(this.getX() + this.getDx());
        }
        // Check Y axis
        Tile entityGhostY = new Tile(this.getX(), this.getY() + this.getDy(), this.getWidth(), this.getHeight());
        boolean entityCanMoveY = true;
        for (Tile t : tiles) {
            if ((t.isSolid) && checkRectCollision(entityGhostY, t)) {
                entityCanMoveY = false;
                break;
            }
        }
        if (entityCanMoveY) {
            this.setY(this.getY() + this.getDy());
        }
    }

    /**
     * Takes damage from an entity.
     * @param damage - amount of damage to be taken
     */
    public void takeDamage(double damage) {
        if(damage <= 0) return;
        this.currentHp -= damage;

        if (this.currentHp <= 0) {
            this.currentHp = 0;
            if (this.active) {
                this.active = false;
                LogConfig.getLogger().info(this.getClass().getSimpleName() + " died");
            }
        }
    }

    public void setX(double v) {
        this.x = v;
    }

    public void setY(double v) {
        this.y = v;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
    protected void setSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed(){
        return speed;
    }

    public double getMaxHp(){
        return maxHp;
    }


    public double getCurrentHp(){
        return currentHp;
    }

    public void setCurrentHp(double currentHp){
        this.currentHp = currentHp;
    }

    public boolean isStillAlive(){
        return this.active;
    }
}
