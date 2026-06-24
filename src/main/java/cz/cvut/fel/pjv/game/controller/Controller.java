package cz.cvut.fel.pjv.game.controller;

import cz.cvut.fel.pjv.Launcher;
import cz.cvut.fel.pjv.engine.graphics.Renderer;
import cz.cvut.fel.pjv.engine.io.*;
import cz.cvut.fel.pjv.engine.model.ImageId;
import cz.cvut.fel.pjv.engine.model.Tile;
import cz.cvut.fel.pjv.game.controller.exeptions.CraftingException;
import cz.cvut.fel.pjv.game.model.enemies.EnderMan;
import cz.cvut.fel.pjv.game.model.entities.Enemy;
import cz.cvut.fel.pjv.game.model.entities.NPC;
import cz.cvut.fel.pjv.game.model.entities.Player;
import cz.cvut.fel.pjv.game.model.items.Weapon;
import cz.cvut.fel.pjv.game.model.items.materials.Material;
import cz.cvut.fel.pjv.game.model.world.ActivatedPortal;
import cz.cvut.fel.pjv.game.model.world.CraftingTable;
import cz.cvut.fel.pjv.game.model.world.LavaTile;
import cz.cvut.fel.pjv.game.model.world.Portal;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.pjv.engine.physics.CollisionDetector.checkRectCollision;
import static cz.cvut.fel.pjv.engine.physics.CollisionDetector.isWithinRange;

/**
 * The main controller class that handles the game logic and user input.
 */
public class Controller {

    private Player player;
    private NPC npc;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();
    private final List<Material> materials = new ArrayList<>();
    private final List<Material> pickedUpMaterials = new ArrayList<>();
    private final List<Material> eyes = new ArrayList<>();

    private Renderer renderer;
    private GraphicsContext gc;
    private InputHandler inputHandler;
    private AnimationTimer animationTimer;
    private Runnable onDeathCallback;
    private Runnable onWinCallback;
    private Launcher launcher;
    private GameTimeService timeService;

    /**
     * Initializes the game by loading the level and starting the game loop.
     * @param gc - graphics context
     * @param inputHandler - input handler
     * @param launcher - launcher
     */
    public void initialize(GraphicsContext gc, InputHandler inputHandler, Launcher launcher) {
        this.renderer = new Renderer();
        this.gc = gc;
        this.inputHandler = inputHandler;
        this.launcher = launcher;

        try {
            GameLevel level = LevelLoader.load("/levels/level1.json");
            this.player = new Player(level.spawnX(), level.spawnY(), 48, 48, 300);
            this.timeService = new GameTimeService(player);
            this.timeService.start(); // Launching a background thread
            this.npc = level.npc().isEmpty() ? null : level.npc().getFirst();
            this.tiles = level.tiles();
            this.enemies = level.enemies();
            enemies.forEach(e -> e.setTarget(player));

        } catch (Exception e) {
            LogConfig.getLogger().severe("Failed to load level: " + e.getMessage());
            Platform.runLater(() -> {
                LogConfig.getLogger().severe("ALERT: Level data missing! Please check the resources folder.");
                Platform.exit();
            });
            return;
        }

       this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight()); //clear
                handleInputs();
                updateGame();
                interactable();
                activatePortal();
                renderGame();
            }
        };
        animationTimer.start();
    }

    //The job is to go through all the objects and tell them, "Hey, another frame has passed, update your state"
    private void updateGame() {
        player.move(tiles);// --- Player Movement & Collision ---
        enemies.forEach(e -> e.move(tiles));// --- Enemies Movement & Collision ---
        player.update();
        if (npc != null) npc.update();
        enemies.forEach(e -> e.update());
        deathIsComing();
    }
    //renderGame() is a method that will draw all the objects on the screen
    private void renderGame(){
        tiles.forEach(t -> renderer.draw(t, gc));
        enemies.forEach(e -> renderer.draw(e, gc));
        materials.forEach(m -> renderer.draw(m, gc));
        if (npc != null) renderer.draw(npc, gc);
        if (player.isCrafting()) {
            renderer.drawCraftingMenu(gc, player);
        }
        if (player.isTalking()) {
            renderer.showDialog(gc);
        }
        renderer.drawTime(gc, timeService);
        renderer.drawAnimated(player, gc);
        renderer.drawPlayerHUD(gc, player);
        renderer.drawHotbar(gc, player);
    }

    // A method that will check every frame whether it's time for someone to die
    private void deathIsComing(){
        enemies.removeIf(enemy -> {
            if(!enemy.isStillAlive()){
                if(enemy.getSpecialItem() != null){
                    Material specialItem = enemy.getSpecialItem();
                    specialItem.setX(enemy.getX());
                    specialItem.setY(enemy.getY());
                    materials.add(specialItem);
                    materials.add(specialItem);
                    materials.add(specialItem);
                    materials.add(specialItem);
                }
                return true;
            }
            return false;
        });
        if (!player.isStillAlive()) {
            if (timeService != null) {
                timeService.cancel(); // Stoping the stream
            }
            animationTimer.stop();
            if (onDeathCallback != null) {
                onDeathCallback.run();
            }
        }
    }

    /**
     * Handles user inputs such as escape key and mouse clicks.
     */
    private void handleInputs(){
        if (inputHandler.isEscPressed()) {
            if (timeService != null) {
                timeService.cancel(); // Stopping the stream
            }
            animationTimer.stop();
            launcher.showPauseMenu(animationTimer);
        }
        if(inputHandler.consumeMouseClick()){
            Weapon hit = player.attack(inputHandler);
            for(Enemy e : enemies){
                if (checkRectCollision(hit, e)){
                    e.takeDamage(hit.getWeaponDamage());
                }
            }
            player.getAnimationController().triggerHit();
        }
        if (!inputHandler.isKeyPressed(KeyCode.E)){
            player.setCrafting(false);
            player.setTalking(false);
        }

        player.handleInput(inputHandler);
    }

    /**
     * Handles interactions with interactable objects
     */
    private void interactable(){
        boolean spaceJustPressed = inputHandler.isSpaceJustPressed();
        boolean eJustPressed = inputHandler.isEJustPressed();
        boolean ePressed = inputHandler.isEPressed();

        for (Material m : materials) {
            if (isWithinRange(player, m, 50)) {
                m.setShouldShowHint(true);
                if (eJustPressed) {
                    pickedUpMaterials.add(m);
                    player.takeItem(m);
                }
            }
            else  m.setShouldShowHint(false);
        }
        materials.removeAll(pickedUpMaterials);
        for (Tile t : tiles){
            boolean isNear = isWithinRange(t, player, 80);

            if (isNear && t.getIsInteractable()){

                if (t instanceof LavaTile){
                    t.setShowHint(player.haveItem(ImageId.FIRE_PROTECTION));
                } else if (t instanceof  Portal portal) {
                    t.setShowHint(!portal.isHaveEye() && player.haveItem(ImageId.ENDER_EYE));
                } else t.setShowHint(true);

                if (t.getInteractionButton().equals("E")){
                    if (eJustPressed){
                        t.interact(player);
                    }
                    if (ePressed && t instanceof CraftingTable craftingTable) {
                        if (spaceJustPressed) {
                            try {
                                craftingTable.craft(player);
                            } catch (CraftingException e) {
                                LogConfig.getLogger().severe("Crafting exception: " + e.getMessage());
                            }
                        }
                    }
                }
                else if (t.getInteractionButton().equals("SPACE")){
                    if (spaceJustPressed){
                        t.interact(player);
                    }
                }
            }
            else t.setShowHint(false);

            if (checkRectCollision(t, player)){
                t.onCollision(player);
            }

        }
        if (npc != null){
            if (isWithinRange(player, npc, 60)){
                npc.setShowHint(true);
                if (eJustPressed){
                    npc.interact(player);
                }
            }
            else npc.setShowHint(false);
        }
    }

    /**
     * Sets the callback to be executed when the player dies.
     * @param callback The callback to be executed.
     */
    public void setOnDeathCallback(Runnable callback) {
        this.onDeathCallback = callback;
    }
    public void setOnWinCallback(Runnable callback) {
        this.onWinCallback = callback;
    }

    /**
     * Activates the portal if all the required items are collected.
     */
    private void activatePortal(){
        int counter = 0;
        for (Tile t : tiles) {
            if (t instanceof Portal portal){
                if (portal.isHaveEye()){
                    counter++;
                }

            }
        }
        if(counter==12){
            for (Tile t : tiles) {
                if(t instanceof ActivatedPortal activatedPortal){
                    activatedPortal.setActivate(true);
                    if (checkRectCollision(player, t)){
                        animationTimer.stop();
                        if (onWinCallback != null) {
                            onWinCallback.run();
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a material from an image id.
     * @param id - ImageId
     * @return Material
     */
    private Material createMaterialFromId(ImageId id) {
        return switch (id) {
            case PEARL -> new cz.cvut.fel.pjv.game.model.items.materials.EnderPearl(0, 0, 32, 32);
            case FIRE_POWDER -> new cz.cvut.fel.pjv.game.model.items.materials.FirePowder(0, 0, 32, 32);
            case ENDER_EYE -> new cz.cvut.fel.pjv.game.model.items.materials.EnderEye(0, 0, 32, 32);
            case FIRE_PROTECTION -> new cz.cvut.fel.pjv.game.model.items.materials.FireProtection(0, 0, 32, 32);
            default -> new Material(0, 0, 32, 32);
        };
    }
    //Saving the game
    public void saveCurrentGame() {
        GameSerializer.saveGame(this.player, this.enemies, this.tiles, this.materials, this.eyes);
    }
    //Loading the game
    public void loadGame(String jsonData) {
        if (jsonData == null) return;
        JSONObject root = new JSONObject(jsonData);

        //Loading player
        JSONObject pJson = root.getJSONObject("player");
        player.setX(pJson.getDouble("x"));
        player.setY(pJson.getDouble("y"));
        player.setFireProtected(pJson.getBoolean("fireProtected"));
        player.setCurrentHp(pJson.getDouble("hp"));

        player.getMaterials().clear();
        JSONArray inv = pJson.getJSONArray("inventory");
        for (int i = 0; i < inv.length(); i++) {
            ImageId id = ImageId.valueOf(inv.getString(i));
            player.takeItem(createMaterialFromId(id));
        }

        JSONArray materialsJson = root.getJSONArray("materials");
        materials.clear();
        for (int i = 0; i < materialsJson.length(); i++) {
            JSONObject mJson = materialsJson.getJSONObject(i);
            ImageId id = ImageId.valueOf(mJson.getString("type"));
            Material m = createMaterialFromId(id);
            m.setX(mJson.getDouble("x"));
            m.setY(mJson.getDouble("y"));
            materials.add(m);
        }
        JSONArray eyesJson = root.getJSONArray("eyes");
        for (int i = 0; i < eyesJson.length(); i++) {
            JSONObject eJson = eyesJson.getJSONObject(i);
            ImageId id = ImageId.valueOf(eJson.getString("type"));
            Material m = createMaterialFromId(id);
            m.setX(eJson.getDouble("x"));
            m.setY(eJson.getDouble("y"));
            eyes.add(m);
        }

        //loading portals
        JSONArray portals = root.getJSONArray("portals");
        for (int i = 0; i < portals.length(); i++) {
            JSONObject portJson = portals.getJSONObject(i);
            for (Tile t : tiles) {
                if (t instanceof Portal p && p.getX() == portJson.getDouble("x") && p.getY() == portJson.getDouble("y")) {
                    p.setHaveEye(portJson.getBoolean("haveEye"));
                }
            }
        }

        JSONArray enemiesJson = root.getJSONArray("enemies");
        enemies.clear();

        for (int i = 0; i < enemiesJson.length(); i++) {
            JSONObject eJson = enemiesJson.getJSONObject(i);
            String type = eJson.getString("type");
            double x = eJson.getDouble("x");
            double y = eJson.getDouble("y");
            double hp = eJson.getDouble("hp");

            Enemy e = null;
            switch (type) {
                case "ENDERMAN" -> e = new EnderMan(x, y, 48, 48, 200);
                case "IFRIT"    -> e = new cz.cvut.fel.pjv.game.model.enemies.Ifrit(x, y, 48, 48, 150);
                case "WITCH"    -> e = new cz.cvut.fel.pjv.game.model.enemies.Witch(x, y, 48, 48, 100);
            }

            if (e != null) {
                e.setCurrentHp(hp);
                e.setTarget(this.player);
                enemies.add(e);
            }
        }

    }
//Stopping all services
    public void stopAllServices() {
        if (timeService != null) {
            timeService.cancel(); // Stoping the stream
        }
    }
}
