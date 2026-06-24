package cz.cvut.fel.pjv.engine.graphics;

import cz.cvut.fel.pjv.engine.io.GameTimeService;
import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.engine.model.GameObject;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.game.model.entities.NPC;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.materials.EnderEye;
import cz.cvut.fel.pjv.game.model.items.materials.Material;
import cz.cvut.fel.pjv.game.model.world.ActivatedPortal;
import cz.cvut.fel.pjv.game.model.world.Portal;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

/**
 * Renderer class responsible for drawing game objects on the screen
 */
public class Renderer {
    private final Map<ImageId, Image> cache = new HashMap<>();
    private Image load(String path) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    public Renderer() {
        cache.put(ImageId.PLAYER, load("/assets/knight.png"));
        cache.put(ImageId.SWORD, load("/assets/weapon_sword.png"));
        cache.put(ImageId.WALL,   load("/assets/wall.png"));
        cache.put(ImageId.FLOOR,  load("/assets/floor.png"));
        cache.put(ImageId.ENDERMAN,  load("/assets/enderman.png"));
        cache.put(ImageId.PEARL, load("/assets/ender_pearl.png"));
        cache.put(ImageId.CRAFTING_TABLE, load("/assets/table.png"));
        cache.put(ImageId.FIRE_POWDER, load("/assets/fire_powder.png"));
        cache.put(ImageId.ENDER_EYE, load("/assets/ender_eye.png"));
        cache.put(ImageId.IFRIT, load("/assets/ifrit.png"));
        cache.put(ImageId.NPC, load("/assets/wizzard.png"));
        cache.put(ImageId.WITCH, load("/assets/witch.png"));
        cache.put(ImageId.FIRE_PROTECTION, load("/assets/fire_protection.png"));
        cache.put(ImageId.LAVA, load("/assets/lava.png"));
        cache.put(ImageId.PORTAL, load("/assets/portal.png"));
        cache.put(ImageId.ACT_PORTAL, load("/assets/act_portal.png"));
    }


    //Main draw method
    public void draw(GameObject obj, GraphicsContext gc) {
        if (obj instanceof ActivatedPortal actPortal && !actPortal.getActivate()) { //If portal is not activated, do not draw it
            return;
        }
        Image img = cache.get(obj.getImageId());
        gc.setImageSmoothing(false);
        if (img != null) {
            gc.drawImage(img, obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            switch (obj) {
                case Portal portal -> {
                    if (portal.isHaveEye()) {
                        draw(new EnderEye(portal.getX() + 5, portal.getY() + 5, 36, 36), gc); //Draw ender eye on portal if player places it
                    }
                    if (portal.shouldShowHint() && portal.getInteractionText() != null) {
                        drawText(gc, portal.getInteractionText(), obj.getX(), obj.getY(), Color.WHITE, 20);
                    }
                }
                case Tile tile -> {
                    if (tile.shouldShowHint() && tile.getInteractionText() != null) {
                        drawText(gc, tile.getInteractionText(), obj.getX(), obj.getY(), Color.WHITE, 20);
                    }
                }
                case NPC npc -> {
                    if (npc.shouldShowHint() && npc.getInteractionText() != null) {
                        drawText(gc, npc.getInteractionText(), obj.getX(), obj.getY(), Color.WHITE, 20);
                    }
                }
                case Material material -> {
                    if (material.shouldShowHint() && material.getInteractionText() != null) {
                        drawText(gc, material.getInteractionText(), obj.getX(), obj.getY(), Color.WHITE, 20);
                    }
                }
                default -> {}
            }
        } else {
            LogConfig.getLogger().severe("Failed to draw object: " + obj.getClass().getSimpleName()); //If image is not found, log error
        }
    }

    //Draw animated player
    public void drawAnimated(Player player, GraphicsContext gc) {
        AnimationController anim = player.getAnimationController();
        Image img = anim.getCurrentImage(player.getDx(), player.getDy());
        gc.setImageSmoothing(false);

        if (anim.isFacingLeft()) {
            gc.save();
            gc.scale(-1, 1);
            gc.drawImage(img, -player.getX() - player.getWidth(), player.getY(), player.getWidth(), player.getHeight());
            gc.restore();
        } else {
            gc.drawImage(img, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        }

        Image sword = cache.get(ImageId.SWORD);
        double swordCenterX = anim.isFacingLeft()
                ? player.getX() - 16
                : player.getX() + player.getWidth() + 16; //Java said to me to do it this way, but it works
        double swordCenterY = player.getY() + 12 + 16;
        gc.save();
        gc.translate(swordCenterX, swordCenterY);
        double angle;
        if (anim.isFacingLeft()) {
            angle = -anim.getSwordAngle();
        } else {
            angle = anim.getSwordAngle();
        }
        gc.rotate(angle);
        gc.drawImage(sword, -16, -16, 32, 32);
        gc.restore();
    }

    //Draw player health bar
    public void drawPlayerHUD(GraphicsContext gc, Player player) {
        AnimationController anim = player.getAnimationController();

        double hpPerHeart = 50.0;
        int maxHearts = (int) (player.getMaxHp() / hpPerHeart);
        double currentHp = player.getCurrentHp();

        for (int i = 0; i < maxHearts; i++) {
            double threshold = (i + 1) * hpPerHeart;
            Image heartImg;

            if (currentHp >= threshold) {
                heartImg = anim.getHeartImage(0); // Full
            } else if (currentHp >= threshold - (hpPerHeart / 2)) {
                heartImg = anim.getHeartImage(1); // Half
            } else {
                heartImg = anim.getHeartImage(2); // Empty
            }

            gc.drawImage(heartImg, 20 + i * (32 + 5), 20, 32, 32);
        }

    }
    //Draw game time
    public void drawTime(GraphicsContext gc, GameTimeService timeService){
        String timeText = String.format("Time: %02d:%02d",
                (timeService.getSecondsPassed() / 60),
                (timeService.getSecondsPassed() % 60));
        drawText(gc, timeText, 20, 80, Color.WHITE, 20);
    }
    //Special draw method for text
    public void drawText(GraphicsContext gc, String text, double x, double y, Color color, int size) {
        gc.setFill(color);
        gc.setFont(new Font("Consolas", size));
        gc.fillText(text, x, y);
    }

    //Draw crafting menu
    public void drawCraftingMenu(GraphicsContext gc, Player player) {
        Image pearl = cache.get(ImageId.PEARL);
        Image firePowder = cache.get(ImageId.FIRE_POWDER);
        Image enderEye = cache.get(ImageId.ENDER_EYE);

        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, 1280, 720);

        gc.setFill(Color.DARKSLATEGRAY);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        double menuX = 340, menuY = 160, menuW = 600, menuH = 400;
        gc.fillRect(menuX, menuY, menuW, menuH);
        gc.strokeRect(menuX, menuY, menuW, menuH);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 30));
        gc.fillText("Crafting system", menuX + 220, menuY + 50);

        gc.drawImage(pearl, menuX + 96, menuY + 150, 64, 64); // pearl
        drawText(gc, String.valueOf(player.countItems(ImageId.PEARL)), 464, 390, Color.WHITE, 20); //counter

        drawText(gc, "+", 550, 360, Color.WHITE, 50); // +

        gc.drawImage(firePowder, menuX + 280, menuY + 150, 64, 64); // firePowder
        drawText(gc, String.valueOf(player.countItems(ImageId.FIRE_POWDER)), 648, 390, Color.WHITE, 20); //counter

        drawText(gc, "=", 710, 360, Color.WHITE, 50); // =

        gc.drawImage(enderEye, menuX + 420, menuY + 150, 64, 64); //enderEye
        drawText(gc, String.valueOf(player.countItems(ImageId.ENDER_EYE)), 788, 390, Color.WHITE, 20); //counter


        gc.fillText("Press 'space' to craft", menuX + 220, menuY + 330);
    }


    //Dialog with NPC
    public void showDialog(GraphicsContext gc){
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, 1280, 720);

        gc.setFill(Color.DARKSLATEGRAY);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        double menuX = 340, menuY = 160, menuW = 600, menuH = 400;
        gc.fillRect(menuX, menuY, menuW, menuH);
        gc.strokeRect(menuX, menuY, menuW, menuH);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 30));
        gc.fillText("Dialog", menuX + 220, menuY + 50);
        drawText(gc, "To win this game, you need to kill all the enemies in here,", menuX + 20, menuY + 150, Color.WHITE, 15);
        drawText(gc, "and after it pick all items and craft ender eye, to put it on portal", menuX + 20, menuY + 165, Color.WHITE, 15);
        drawText(gc, "to escape. And be careful of lava, if you will step on it without", menuX + 20, menuY + 180, Color.WHITE, 15);
        drawText(gc, "fire protection, you will die.", menuX + 20, menuY + 195, Color.WHITE, 15);
    }

    /**
     * Draws the player's hotbar.
     * @param gc  - The graphics context to draw on.
     * @param player - The player whose hotbar is to be drawn.
     * Draw player's hotbar The inventory is limited to 9 unique slots
     */
    public void drawHotbar(GraphicsContext gc, Player player) {
        double screenWidth = gc.getCanvas().getWidth(); // Get the width of the canvas
        double screenHeight = gc.getCanvas().getHeight(); // Get the height of the canvas
        final int slotsCount = 9;      // Number of slots
        final int slotSize = 64;       // Size of a single square
        final int spacing = 4;         // Distance between squares
        int totalWidth = (slotsCount * slotSize) + ((slotsCount - 1) * spacing);
        double startX = (screenWidth - totalWidth) / 2.0;
        double startY = screenHeight - slotSize - 20;

        if (player == null) return;

        for (int i = 0; i < slotsCount; i++) {
            double currentX = startX + i * (slotSize + spacing);

            gc.setFill(Color.rgb(0, 0, 0, 0.5));
            gc.fillRect(currentX, startY, slotSize, slotSize);

            gc.setStroke(Color.GRAY);
            gc.setLineWidth(3);
            gc.strokeRect(currentX, startY, slotSize, slotSize);
        }

        List<ImageId> uniqueItems = new ArrayList<>();
        for (Material m : player.getMaterials()) {
            if (!uniqueItems.contains(m.getImageId())) {
                uniqueItems.add(m.getImageId()); // Add only if this ID does not already exist
            }
        }

        // Drawing objects in slots
        for (int i = 0; i < uniqueItems.size(); i++) {
            if (i >= slotsCount) break; // Protection: if there are more than 9 items, do not draw outside the hotbar

            ImageId currentId = uniqueItems.get(i);
            int count = player.countItems(currentId);
            Image itemImage = cache.get(currentId);

            double slotX = startX + i * (slotSize + spacing);

            gc.drawImage(itemImage, slotX + 8, startY + 8, slotSize - 16, slotSize - 16);

            if (count > 1) {
                drawText(gc, String.valueOf(count), slotX + slotSize - 20, startY + slotSize - 10, Color.WHITE, 20);
            }
        }
    }
}
