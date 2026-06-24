package cz.cvut.fel.pjv.game.model.enemies;

import cz.cvut.fel.pjv.engine.model.Entity;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.game.model.entities.Enemy;
import cz.cvut.fel.pjv.game.model.items.materials.EnderPearl;

/**
 * Represents an EnderMan enemy in the game.
 */
public class EnderMan extends Enemy {
    public EnderMan(double x, double y, double width, double height, double maxHp) {
        super(x, y, width, height, maxHp);
        this.imageId = ImageId.ENDERMAN;
        this.specialItem = new EnderPearl(getX(), getY(), 32, 32);
        setDetectRange(200);
        setAggroRange(50);
        setSpeed(2);
    }
    protected double damage;

    /**
     * Attack logic
     * @param target - Always the player
     */
    @Override
    public void attack(Entity target) {
        if (attackCooldown == 0) {
            damage = 10;
            target.takeDamage(damage);
            attackCooldown = 180;
        }
    }
    @Override
    public void setDetectRange(double detectRange) {
        this.detectRange = detectRange;
    }

}

