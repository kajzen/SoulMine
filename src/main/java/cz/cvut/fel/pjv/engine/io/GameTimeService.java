package cz.cvut.fel.pjv.engine.io;

import cz.cvut.fel.pjv.game.model.entities.Player;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Service that counts the time passed in the game. And regenerates the player's health every 5 seconds
 */
public class GameTimeService extends Service<Void> {
    private int secondsPassed = 0;
    private final Player player;

    public GameTimeService(Player player) {
        this.player = player;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (!isCancelled()) {
                    Thread.sleep(1000);
                    secondsPassed++;

                    // We heal player every 5 seconds
                    if (secondsPassed % 5 == 0) {
                        Platform.runLater(() -> {
                            if (player != null && player.isStillAlive() && player.getCurrentHp() < player.getMaxHp()) {
                                player.setCurrentHp(Math.min(player.getMaxHp(), player.getCurrentHp() + 10)); //No more than max hp
                            }
                        });
                    }
                }
                return null;
            }
        };
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }
}
