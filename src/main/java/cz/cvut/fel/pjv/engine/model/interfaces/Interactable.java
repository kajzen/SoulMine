package cz.cvut.fel.pjv.engine.model.interfaces;

import cz.cvut.fel.pjv.game.model.entities.Player;

/**
 * Interface for interactable objects
 */
public interface Interactable {
    void interact(Player player);
}
