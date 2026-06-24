package cz.cvut.fel.pjv.engine.physics;

import cz.cvut.fel.pjv.engine.model.GameObject;

/**
 * Detects collisions between two objects
 */
public class CollisionDetector {

    //Circle logic
    public static boolean isWithinRange(GameObject a, GameObject b, double range) {
        double aCenterX = a.getX() + a.getWidth()  / 2.0;
        double aCenterY = a.getY() + a.getHeight() / 2.0;
        double bCenterX = b.getX() + b.getWidth()  / 2.0;
        double bCenterY = b.getY() + b.getHeight() / 2.0;

        double dx = aCenterX - bCenterX;
        double dy = aCenterY - bCenterY;

        return (dx * dx + dy * dy) < range * range;
    }
    //RectCollision logic
    public static boolean checkRectCollision(GameObject a, GameObject b){
        return (a.getX() < b.getX() + b.getWidth()   &&  // a's left edge is left of b's right edge
                b.getX() < a.getX() + a.getWidth()   &&  // b's left edge is left of a's right edge
                a.getY() < b.getY() + b.getHeight()  &&  // a's top  edge is above b's bottom edge
                b.getY() < a.getY() + a.getHeight());    // b's top  edge is above a's bottom edge
    }
}
