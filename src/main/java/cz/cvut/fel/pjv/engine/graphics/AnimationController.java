package cz.cvut.fel.pjv.engine.graphics;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * AnimationController a class used to control character animations, such as walking, attacking, and idling
 */
public class AnimationController {
    Image[] runFrames = new Image[4];
    Image[] idleFrames = new Image[4];
    Image[] heartFrames = new Image[3];

    private int hitTimer;
    private int runFrame = 0;
    private int idleFrame = 0;
    private int frameCounter = 0;
    private boolean facingLeft = false;
    private boolean isHitting;
    private double swordAngle = 45;

    public AnimationController() {

        runFrames[0] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_run_1.png")));
        runFrames[1] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_run_2.png")));
        runFrames[2] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_run_3.png")));
        runFrames[3] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_run_4.png")));

        idleFrames[0] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_idle_1.png")));
        idleFrames[1] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_idle_2.png")));
        idleFrames[2] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_idle_3.png")));
        idleFrames[3] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/knight_idle_4.png")));

        // В Renderer
        heartFrames[0] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/full_heart.png")));
        heartFrames[1] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/half_heart.png")));
        heartFrames[2] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/frames/empty_heart.png")));
    }

    public void update(double dx, double dy) {
        if (isHitting){
            hitTimer--;
            if (hitTimer == 0){
                isHitting = false;
            }
        }
        if(swordAngle < 45){
            swordAngle+= 3;
        }
        boolean isMoving = dx != 0 || dy != 0;

        frameCounter++;
        int FRAME_SPEED = 8;
        if (frameCounter >= FRAME_SPEED) {
            frameCounter = 0;
            if (isMoving) {
                runFrame = (runFrame + 1) % 4;
            } else {
            idleFrame = 3;
        }
        if (dx < 0) setFacingLeft(true);
        if (dx > 0) setFacingLeft(false);
    }
}

    protected Image getCurrentImage(double dx, double dy){
        if (dx != 0 || dy != 0){
            return runFrames[runFrame];
        } else{
            return idleFrames[idleFrame];
        }
    }


    //Hit animation
    public void triggerHit(){
        isHitting = true;
        hitTimer = 15;
        swordAngle = 0;
    }
    protected Image getHeartImage(int index) {
        return heartFrames[index]; // 0 - full, 1 - half, 2 - empty
    }

    protected boolean isFacingLeft() {
        return facingLeft;
    }

    protected void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
    protected double getSwordAngle(){
        return swordAngle;
    }
}
