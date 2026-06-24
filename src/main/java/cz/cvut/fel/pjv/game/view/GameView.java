package cz.cvut.fel.pjv.game.view;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents the game view.
 */
public class GameView {
    //TODO
    private final Canvas canvas; //Our window
    private final GraphicsContext gc; //Pencil

    public GameView(int width, int height) {
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
    }

    public Parent getRoot() {
        return new Group(canvas);
    }

    // Getter to give it to Controller
    public GraphicsContext getGraphicsContext() {
        return gc;
    }
    }
