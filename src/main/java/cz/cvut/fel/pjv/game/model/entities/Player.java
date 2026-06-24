package cz.cvut.fel.pjv.game.model.entities;

import cz.cvut.fel.pjv.engine.graphics.AnimationController;
import cz.cvut.fel.pjv.engine.io.InputHandler;
import cz.cvut.fel.pjv.engine.model.Entity;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.game.model.items.Weapon;
import cz.cvut.fel.pjv.game.model.items.materials.Material;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

/**
 * Represents the player in the game.
 */
public class Player extends Entity {
    public Player(double x, double y, double width, double height, double maxHp) {
        super(x, y, width, height, maxHp);
        speed = 3;
        this.imageId = ImageId.PLAYER;
    }
    private boolean isFireProtected;
    private boolean crafting;
    private boolean talking;
    private final AnimationController animationController = new AnimationController();
    List<Material> materials = new ArrayList<>();


    /**
     * Handles player input based on the provided input handler.
     * @param inputHandler The input handler to process player input.
     */
    public void handleInput(InputHandler inputHandler) {
        dx = 0;
        dy = 0;
        if (inputHandler.isKeyPressed(KeyCode.W)) {
            this.dy = -speed;
        }
        if (inputHandler.isKeyPressed(KeyCode.S)) {
            this.dy = speed;
        }
        if (inputHandler.isKeyPressed(KeyCode.A)) {
            this.dx = -speed;
        }
        if (inputHandler.isKeyPressed(KeyCode.D)) {
            this.dx = speed;
        }

    }

    public void takeItem(Material material){
        materials.add(material);
    }

    /**
     * Handles player attack based on the provided input handler.
     * @param inputHandler The input handler to process player attack.
     * @return Create a weapon for the attack.
     */
    //Not sure if this is the best way to do it, was one of the first things I tried
    public Weapon attack(InputHandler inputHandler){
        double attackDx = inputHandler.getMouseX() - this.getX();
        double attackDy = inputHandler.getMouseY() - this.getY();
        double length = sqrt(attackDx * attackDx + attackDy * attackDy);
        double attackRange = 60;
        if (length > 0) {
            attackDx = (attackDx / length) * attackRange;
            attackDy = (attackDy / length) * attackRange;
        }
        Weapon weapon = new Weapon(this.getX() + attackDx, this.getY() + attackDy, 20, 20);
        weapon.setWeaponDamage(60);
        return weapon;
    }

    @Override
    public void update() {
        animationController.update(getDx(), getDy());
    }

    /**
     * Counts the number of items of a specific type.
     * @param imageId - ImageId of the item to count
     * @return The count of items with the specified ImageId
     */
    public int countItems(ImageId imageId){
        int counter = 0;
        for (Material m : materials){
            if (m.getImageId().equals(imageId)){
                counter++;
            }
        }
        return counter;
    }

    //Player inventory
    public List<Material> getMaterials() {
        return materials;
    }

    //Remove item from inventory by ImageId
    public void removeItem(ImageId imageId){
        for (Material m : materials) {
            if (m.getImageId().equals(imageId)) {
                materials.remove(m);
                break;
            }
        }
    }
    //Check if player has item in inventory
    public boolean haveItem(ImageId imageId){
        for (Material m : materials) {
            if (m.getImageId().equals(imageId)) {
                return true;
            }
        }
        return false;
    }

    // Getters and setters

    public void setCrafting(boolean crafting) {
        this.crafting = crafting;
    }
    public AnimationController getAnimationController() {
        return animationController;
    }
    public boolean isCrafting() {
        return crafting;
    }
    public void setFireProtected(boolean fireProtected) {
        isFireProtected = fireProtected;
    }
    public boolean getFireProtected(){
        return isFireProtected;
    }
    public void setTalking(boolean talking) {
        this.talking = talking;
    }
    public boolean isTalking() {
        return talking;
    }
}
