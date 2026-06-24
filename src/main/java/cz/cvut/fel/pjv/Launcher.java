package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.engine.io.GameSerializer;
import cz.cvut.fel.pjv.engine.io.InputHandler;
import cz.cvut.fel.pjv.engine.io.LogConfig;
import cz.cvut.fel.pjv.game.controller.Controller;
import cz.cvut.fel.pjv.game.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Launcher class for the game application.
 */
public class Launcher extends Application {

    private Stage primaryStage;
    private Controller controller = new Controller();

    /**
     * Initializes the game application and sets up the main menu.
     * @param stage - primary stage of the application
     */
    @Override
    public void start(Stage stage) {
        stage.setOnCloseRequest(event -> {
            if (controller != null) {
                controller.stopAllServices(); // Stop all services if User exits the game. Reason - prevent resource leaks and ensure a clean shutdown and GameTimeService is stopped.
            }
            Platform.exit();
            System.exit(0);
        });
        this.primaryStage = stage;
        showMenu();
    }

    /**
     * Displays the main menu with options for starting a new game, loading an existing game, exiting the application, and toggling logs.
     */
    private void showMenu() {
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #1a1a1a;");

        Button btnNewGame = new Button("NEW GAME");
        Button btnLoadGame = new Button("LOAD GAME");
        Button btnExit = new Button("EXIT");
        Button btnLogs = new Button("LOGS: OFF");

        String style = "-fx-base: #333333; -fx-text-fill: white; -fx-font-size: 18px; -fx-min-width: 200px;";
        btnNewGame.setStyle(style);
        btnLoadGame.setStyle(style);
        btnExit.setStyle(style);
        btnLogs.setStyle(style);
        LogConfig.setup(false);

        btnNewGame.setOnAction(e -> startGame());
        btnLoadGame.setOnAction(e -> loadExistingGame());
        btnExit.setOnAction(e -> primaryStage.close());

        btnLogs.setOnAction(e -> {
            LogConfig.setup(true);
            btnLogs.setText("LOGS: ON");
            btnLogs.setDisable(true);
        });

        menuLayout.getChildren().addAll(btnNewGame, btnLoadGame, btnLogs, btnExit);

        Scene menuScene = new Scene(menuLayout, 1280, 720);
        primaryStage.setTitle("SoulMine - Main Menu");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    /**
     * Starts the game by creating a new GameView, InputHandler, and Controller.
     */
    private void startGame() {
        GameView gameView = new GameView(1280, 720);
        InputHandler inputHandler = new InputHandler();
        this.controller = new Controller();

        StackPane root = new StackPane();
        // Add the game as the first (bottom) layer
        root.getChildren().add(gameView.getRoot());
        Scene gameScene = new Scene(root, 1280, 720);
        setupInput(gameScene, inputHandler);
        this.controller.initialize(gameView.getGraphicsContext(), inputHandler, this);

        controller.setOnDeathCallback(() -> {Platform.runLater(this::showMenu);});
        controller.setOnWinCallback(() -> {Platform.runLater(this::showMenu);});

        primaryStage.setTitle("SoulMine Game");
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    private void setupInput(Scene scene, InputHandler inputHandler) {
        scene.setOnKeyPressed(event -> inputHandler.addKey(event.getCode()));
        scene.setOnKeyReleased(event -> inputHandler.removeKey(event.getCode()));
        scene.setOnMouseMoved(event -> {
            inputHandler.setMouseX(event.getX());
            inputHandler.setMouseY(event.getY());
        });
        scene.setOnMousePressed(event -> inputHandler.onMousePressed());
        scene.setOnMouseReleased(event -> inputHandler.onMouseReleased());
    }

    /**
     * Shows a pause menu with options to resume, save, load, or exit the game.
     * @param timer The animation timer to pause when the pause menu is shown.
     */
    public void showPauseMenu(AnimationTimer timer) {
        VBox pauseRoot = new VBox(20);
        pauseRoot.setAlignment(Pos.CENTER);
        pauseRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        Button btnResume = new Button("RESUME");
        Button btnSave = new Button("SAVE GAME");
        Button btnLoad = new Button("LOAD GAME");
        Button btnExit = new Button("EXIT TO MENU");

        //style
        String style = "-fx-base: #333333; -fx-text-fill: white; -fx-font-size: 18px; -fx-min-width: 200px;";
        btnResume.setStyle(style);
        btnSave.setStyle(style);
        btnLoad.setStyle(style);
        btnExit.setStyle(style);



        // Continue button
        btnResume.setOnAction(e -> {
            ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(pauseRoot);
            timer.start();
        });

        btnSave.setOnAction(e -> {
            controller.saveCurrentGame();
            //saving method
        });

        btnLoad.setOnAction(e -> {
            loadExistingGame();
            //loading logic
        });

        btnExit.setOnAction(e -> showMenu()); //back to main menu

        pauseRoot.getChildren().addAll(btnResume, btnSave, btnLoad, btnExit);

        StackPane layout = (StackPane) primaryStage.getScene().getRoot();
        layout.getChildren().add(pauseRoot);
    }
    private void loadExistingGame() {
        String data = GameSerializer.loadFromFile();
        if (data == null) {

            LogConfig.getLogger().severe("No save file found!");
            return;
        }

        GameView gameView = new GameView(1280, 720);
        InputHandler inputHandler = new InputHandler();
        StackPane root = new StackPane(gameView.getRoot());
        Scene gameScene = new Scene(root, 1280, 720);
        setupInput(gameScene, inputHandler);

        this.controller = new Controller();
        this.controller.initialize(gameView.getGraphicsContext(), inputHandler, this);

        this.controller.loadGame(data);

        controller.setOnDeathCallback(() -> {
            Platform.runLater(this::showMenu);
        });
        controller.setOnWinCallback(() -> {
            Platform.runLater(this::showMenu);
        });

        primaryStage.setScene(gameScene);
        primaryStage.show();
    }
}