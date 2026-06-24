package cz.cvut.fel.pjv.game.model.world;

import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.engine.model.interfaces.Interactable;
import cz.cvut.fel.pjv.game.model.entities.Player;

/**
 * Represents a lava tile in the game. Can cause damage to a player if not protected.
 */
public class LavaTile extends Tile implements Interactable {
    public LavaTile(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.imageId = ImageId.LAVA;
        this.isInteractable = true;
        this.interactionText = "Press 'SPACE' to use poison";
        this.interactionButton = "SPACE";
    }

    /**
     * If the player has the fire protection item, they will be protected from damage.
     * @param player - player who is interacting with the lava tile
     */
    @Override
    public void interact(Player player) {
        if(player.haveItem(ImageId.FIRE_PROTECTION)){
            player.setFireProtected(true);
            player.removeItem(ImageId.FIRE_PROTECTION);
        }
    }

    /**
     * If the player is not protected from damage, they will take damage.
     * @param player - player who is colliding with the lava tile
     */
    @Override
    public void onCollision(Player player){
        if (!player.getFireProtected()) {
            player.takeDamage(player.getCurrentHp());
        }
    }
}

