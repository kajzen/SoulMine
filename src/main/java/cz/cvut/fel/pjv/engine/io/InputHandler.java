package cz.cvut.fel.pjv.engine.io;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;


//This class will filter user input depending on whether the mouse or keyboard is pressed.
public class InputHandler {
    protected Set<KeyCode> activeKeys = new HashSet<>();
    protected double mouseX;
    protected double mouseY;
    protected boolean mousePressed;
    private boolean mouseClicked;

    public void addKey(KeyCode code){
        activeKeys.add(code);
    }
    public void removeKey(KeyCode code){
        activeKeys.remove(code);
    }
    public boolean isKeyPressed(KeyCode code){
        return activeKeys.contains(code);
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void onMousePressed() {
        mousePressed = true;
        mouseClicked = true;
    }

    public void onMouseReleased() {
        mousePressed = false;
    }

    public boolean consumeMouseClick() {
        if (mouseClicked) {
            mouseClicked = false;
            return true;
        }
        return false;
    }
    public boolean isEPressed() {
        return isKeyPressed(KeyCode.E);
    }

    public boolean isEscPressed() {
        return isKeyPressed(KeyCode.ESCAPE);
    }

    private boolean spaceConsumed = false;
    private boolean eConsumed = false;

    public boolean isSpaceJustPressed() {
        if (activeKeys.contains(KeyCode.SPACE)) {
            if (!spaceConsumed) {
                spaceConsumed = true;
                return true;
            }
        } else {
            spaceConsumed = false;
        }
        return false;
    }


    public boolean isEJustPressed() {
        if (activeKeys.contains(KeyCode.E)) {
            if (!eConsumed) {
                eConsumed = true;
                return true;
            }
        } else {
            eConsumed = false;
        }
        return false;
    }

}
