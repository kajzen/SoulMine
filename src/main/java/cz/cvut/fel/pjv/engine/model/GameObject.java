package cz.cvut.fel.pjv.engine.model;

/**
 * Represents a game object abstract class in the game. This class provides basic properties and methods for game objects - all objects in the game.
 */
public abstract class GameObject {

    protected double x,y;

    protected ImageId imageId;
    protected double width, height;

    protected GameObject(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getWidth() {return width;}
    public double getHeight() {return height;}

    public ImageId getImageId() { return imageId; }
    public void setImageId(ImageId imageId) { this.imageId = imageId; }
}
