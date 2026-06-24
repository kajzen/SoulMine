package cz.cvut.fel.pjv.game.model.entities;

import cz.cvut.fel.pjv.engine.model.Entity;
import cz.cvut.fel.pjv.game.model.items.materials.Material;
import static cz.cvut.fel.pjv.engine.physics.CollisionDetector.isWithinRange;
import static java.lang.Math.sqrt;

/**
 * Base class for all enemies
 */
public abstract class Enemy extends Entity {
    public Enemy(double x, double y, double width, double height, double maxHp) {
        super(x, y, width, height, maxHp);
    }
    protected double aggroRange; // Aggro range
    protected double detectRange; // Detecting range
    protected int attackCooldown = 0;
    protected Player target;
    protected Material specialItem;
    protected double dx;
    protected double dy;

    public double getDy() {
        return dy;
    }
    public double getDx() {
        return dx;
    }

    public Material getSpecialItem() {
        return specialItem;
    }

    public abstract void attack(Entity target);
    public abstract void setDetectRange(double detectRange);

    @Override
    public void update() {
        if(isWithinRange(target, this, detectRange)){
            dx = target.getX() - this.getX();
            dy = target.getY() - this.getY();
            double length = sqrt(dx * dx + dy * dy);
            if (length > 0){
                dx = (dx / length) * getSpeed();
                dy = (dy / length) * getSpeed();
            }
            if (attackCooldown > 0){
                attackCooldown--;
            }
            if (isWithinRange(target, this, getAggroRange()) ) {
                attack(target);
            }

        }

    }
    public void setTarget(Player target){
        this.target = target;
    }

    public double getAggroRange() {
        return aggroRange;
    }

    public void setAggroRange(double aggroRange) {
        this.aggroRange = aggroRange;
    }
}
