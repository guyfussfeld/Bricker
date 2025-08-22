package bricker.main;

import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import danogl.util.Counter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The BrickerGameManager class extends the GameManager class and manages the main game logic,
 * initialization of game objects, and handling of game state for the Bricker game.
 *
 * @author Guy & Arie Gaming Co.
 */
public class BrickerGameManager extends GameManager {

    // Game configuration constants
    private static final String WINDOW_TITLE = "Bricker";
    private static final String WIN_PROMPT_MSG = "You win! Play again?";
    private static final String LOSE_PROMPT_MSG = "You lose! Play again?";
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int BALL_INITIAL_SPEED = 200;
    private static final int HEART_OFFSET_FROM_BOTTOM = 25;
    private static final int MAX_LIVES = 4;
    private static final int DANGER_ZONE = 2;
    private static final int LAST_CHANCE = 1;
    private static final int NUMBER_OF_ARGUMENTS = 2;
    private static final int DEFAULT_ROWS_OF_BRICKS = 7;
    private static final int DEFAULT_BRICKS_PER_ROW = 8;
    private static final int PADDLE_OFFSET_FROM_BOTTOM = 40;
    private static final int ICONS_SPACE = 5;
    private static final int PUCKS_TO_SPAWN = 2;
    private static final int MAX_AMOUNT_OF_PADDLES = 2;
    private static final int MAX_CAMERA_COLLISION = 5;  // Hits to follow the ball including initial hit
    private static final float CENTER_RATIO = 0.5f;
    private static final float CAMERA_SCALE_FACTOR = 1.2f;

    /**
     * Constant for the deault number of lives the player starts with.
     */
    public static final int DEFAULT_LIVES = 3;
    /**
     * Constant for the spacing between bricks.
     */
    public static final int BRICK_SPACING = 10;
    /**
     * Constant for the height of a brick.
     */
    public static final int BRICK_HEIGHT = 15;
    /**
     * Constant for the width and height of a UI icon.
     */
    public static final int UI_ICON_SIZE = 20;
    /**
     * Constant for the thickness of the walls around the game area.
     */
    public static final int WALL_THICKNESS = 10;

    // Instance variables
    private final Vector2 windowDimensions;
    private final int numOfBricksRows;
    private final int numOfBricksPerRow;
    private int cameraStopCounter;
    private GameObject[] lifeHearts;
    private Ball gameBall;
    private final Random rand = new Random();
    private UserInputListener inputListener;
    private WindowController windowController;
    private final Counter paddleCount;
    private final Counter lifeCount;
    private Counter brickCount;
    private GameObjectsFactory gameObjectsFactory;
    private TextRenderable livesTextDisplay;

    /**
     * Constructs a new BrickerGameManager with the specified window title, dimensions,
     * number of brick rows,
     * and number of bricks per row.
     *
     * @param windowTitle       The title of the game window.
     * @param windowDimensions  The dimensions of the game window.
     * @param numOfBricksRows   The number of rows of bricks.
     * @param numOfBricksPerRow The number of bricks per row.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numOfBricksRows,
                              int numOfBricksPerRow) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.numOfBricksRows = numOfBricksRows;
        this.numOfBricksPerRow = numOfBricksPerRow;
        this.lifeCount = new Counter(DEFAULT_LIVES);
        this.paddleCount = new Counter();
    }

    /**
     * Initializes the game by setting up the game objects, input listeners,
     * and other game-related components.
     *
     * @param imageReader      Reader for loading images.
     * @param soundReader      Reader for loading sounds.
     * @param inputListener    Listener for user input.
     * @param windowController Controller for the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.gameObjectsFactory = new GameObjectsFactory(this, imageReader, soundReader, inputListener);

        // Initialize game objects
        backgroundCreator();
        ballCreator();
        paddleCreator();
        wallsCreator();
        bricksCreator();
        createLifeUI();
    }

    //*******************************
    //   INITIAL OBJECT CREATORS
    //*******************************

    /**
     * Creates the background for the game.
     */
    private void backgroundCreator() {
        Background bg = (Background) gameObjectsFactory.buildObject(Background.TAG);
        bg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(bg, Layer.BACKGROUND);
    }

    /**
     * Creates the ball for the game.
     */
    private void ballCreator() {
        gameBall = (Ball) gameObjectsFactory.buildObject(Ball.TAG);
        resetBallPosition();
        gameObjects().addGameObject(gameBall);
    }

    /**
     * Creates the bricks for the game and adds them to the game objects.
     * The number of bricks created is determined by the number of brick rows
     * and the number of bricks per row specified during initialization.
     * Each brick is positioned based on the specified spacing between bricks.
     */
    private void bricksCreator() {
        brickCount = new Counter(numOfBricksPerRow * numOfBricksRows);

        // Start position for the first row of bricks
        float currentY = WALL_THICKNESS + BRICK_SPACING;

        for (int row = 0; row < numOfBricksRows; row++) {
            // Start position for the first brick in the row
            float currentX = WALL_THICKNESS + BRICK_SPACING;

            for (int col = 0; col < numOfBricksPerRow; col++) {
                // Calculate position for the current brick
                Vector2 brickPosition = new Vector2(currentX, currentY);

                Brick brick = (Brick) gameObjectsFactory.buildObject(Brick.TAG);
                brick.setTopLeftCorner(brickPosition);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);

                // Move to the next position for the next brick
                currentX += BRICK_SPACING + brick.getWidth();
            }
            // Move to the next row of bricks
            currentY += BRICK_SPACING + BRICK_HEIGHT;
        }
    }


    /**
     * Creates the paddle for the player and adds it to the game objects.
     * The paddle is positioned at the bottom center of the game window.
     */
    private void paddleCreator() {
        Paddle paddle = (Paddle) gameObjectsFactory.buildObject(Paddle.TAG);
        // Calculate the center position for the paddle
        Vector2 center = new Vector2(windowDimensions.x() * CENTER_RATIO,
                (int) windowDimensions.y() - PADDLE_OFFSET_FROM_BOTTOM);
        paddle.setCenter(center);
        gameObjects().addGameObject(paddle);
        // Increment the paddle count to track the number of paddles and avoid crossing the limit
        paddleCount.increment();
    }

    /**
     * Creates walls around the game area.
     * Four walls: upper, left, right, and bottom, when the last one is a UI layer and non-collidable.
     * Configured based on window dimensions and wall thickness.
     */
    private void wallsCreator() {
        Wall upperWall = (Wall) gameObjectsFactory.buildObject(Wall.TAG);
        gameObjects().addGameObject(upperWall);

        Wall leftWall = (Wall) gameObjectsFactory.buildObject(Wall.TAG);
        leftWall.setDimensions(new Vector2(WALL_THICKNESS, windowDimensions.y() + WALL_THICKNESS));
        leftWall.setTopLeftCorner(Vector2.ZERO);
        gameObjects().addGameObject(leftWall);

        Wall rightWall = (Wall) gameObjectsFactory.buildObject(Wall.TAG);
        rightWall.setDimensions(new Vector2(WALL_THICKNESS, windowDimensions.y() + WALL_THICKNESS));
        rightWall.setTopLeftCorner(new Vector2(windowDimensions.x() - WALL_THICKNESS, 0));
        gameObjects().addGameObject(rightWall);

        Wall bottomWall = (Wall) gameObjectsFactory.buildObject(Wall.TAG);
        bottomWall.setDimensions(new Vector2(windowDimensions.x(), WALL_THICKNESS));
        bottomWall.setTopLeftCorner(new Vector2(0, windowDimensions.y()));
        gameObjects().addGameObject(bottomWall, Layer.UI);
    }

    /**
     * Creates UI elements representing player lives: hearts and a text display.
     */
    private void createLifeUI() {
        Vector2 currPosition = new Vector2(WALL_THICKNESS, windowDimensions.y() - HEART_OFFSET_FROM_BOTTOM);

        // Create lives text UI
        livesTextDisplay = new TextRenderable(String.valueOf(DEFAULT_LIVES));
        refreshLivesTextColor();

        TextUI textUI = (TextUI) gameObjectsFactory.buildObject(TextUI.TAG);
        textUI.setTopLeftCorner(currPosition);
        gameObjects().addGameObject(textUI, Layer.UI);

        // Create hearts UI

        // Initialize hearts array with the maximum number of lives to avoid resizing
        lifeHearts = new GameObject[MAX_LIVES];

        // Create hearts UI objects in the right positions on the screen
        for (int i = 0; i < DEFAULT_LIVES; i++) {
            currPosition = new Vector2(currPosition.x() + UI_ICON_SIZE + ICONS_SPACE, currPosition.y());

            HeartUI heartUI = (HeartUI) gameObjectsFactory.buildObject(HeartUI.TAG);
            heartUI.setTopLeftCorner(currPosition);
            gameObjects().addGameObject(heartUI, Layer.UI);
            lifeHearts[i] = heartUI;
        }
    }


    //************************
    //   COLLISION EFFECTS
    //************************

    /**
     * Creates multiple pucks when a collision occurs with the specified collider object.
     * Pucks are spawned around the collision point with random velocities.
     *
     * @param collider The object that initiated the collision.
     */
    public void puckBallsCreator(GameObject collider) {
        for (int i = 0; i < PUCKS_TO_SPAWN; i++) {
            double angle = rand.nextDouble() * Math.PI;
            float velocityX = (float) Math.cos(angle) * BALL_INITIAL_SPEED;
            float velocityY = (float) Math.sin(angle) * BALL_INITIAL_SPEED;
            Puck puck = (Puck) gameObjectsFactory.buildObject(Puck.TAG);
            puck.setVelocity(new Vector2(velocityX, velocityY));
            puck.setCenter(collider.getCenter());
            gameObjects().addGameObject(puck);
        }
    }

    /**
     * Creates a second paddle when a collision occurs.
     */
    public void secondPaddleCreator() {
        if (paddleCount.value() == MAX_AMOUNT_OF_PADDLES) {
            return;
        }
        SecondPaddle secondPaddle = (SecondPaddle) gameObjectsFactory.buildObject(SecondPaddle.TAG);
        secondPaddle.setCenter(windowDimensions.mult(CENTER_RATIO));
        gameObjects().addGameObject(secondPaddle);
        // Increment the paddle count to track the number of paddles and avoid crossing the limit
        paddleCount.increment();
    }

    /**
     * Sets the camera to follow a game object after a collision.
     *
     * @param target The game object to be followed by the collision camera.
     *               This object will be centered in the camera's view.
     */
    public void activateCollisionCamera(GameObject target) {
        if (target.getTag().equals(Ball.TAG) && camera() == null) {
            // Create a camera centered on the target object
            setCamera(new Camera(target, Vector2.ZERO,
                    windowController.getWindowDimensions().mult(CAMERA_SCALE_FACTOR),
                    windowController.getWindowDimensions()));
            // Set the maximum duration for camera movement
            cameraStopCounter = gameBall.getCollisionCounter() + MAX_CAMERA_COLLISION;
        }
    }

    /**
     * Creates a heart when a collision occurs.
     *
     * @param collider The object that initiated the collision.
     */
    public void heartCreator(GameObject collider) {
        Heart heart = (Heart) gameObjectsFactory.buildObject(Heart.TAG);
        heart.setCenter(collider.getCenter());
        gameObjects().addGameObject(heart);
    }

    /**
     * Increases the player's life count by one and updates the user interface.
     * This method adds a life to the player if the maximum number of lives has not been reached.
     * It updates the graphical representation of the player's lives by displaying an additional heart icon
     * on the user interface.
     */
    public void incrementLife() {
        if (lifeCount.value() == MAX_LIVES) {
            return;
        }
        // Get the position of the last heart icon
        Vector2 lastHeartPos = lifeHearts[lifeCount.value() - 1].getTopLeftCorner();

        // Calculate the position for the new heart icon
        Vector2 newHeartPos = new Vector2(lastHeartPos.x() + UI_ICON_SIZE + ICONS_SPACE, lastHeartPos.y());
        HeartUI heartUI = (HeartUI) gameObjectsFactory.buildObject(HeartUI.TAG);
        heartUI.setTopLeftCorner(newHeartPos);
        gameObjects().addGameObject(heartUI, Layer.UI);

        // Update the life count and refresh the UI
        lifeHearts[lifeCount.value()] = heartUI;
        lifeCount.increment();
        livesTextDisplay.setString(Integer.toString(lifeCount.value()));
        refreshLivesTextColor();
    }

    //************************
    //     GAME UPDATES
    //************************

    /**
     * Updates the game state on each frame.
     *
     * @param deltaTime The time that has passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            handleWinCondition();
        }
        if (gameBall.getCollisionCounter() >= cameraStopCounter) {
            setCamera(null);
        }
        evaluateGameEnd();
        removeOutOfBoundsObjects();
    }

    //************************
    //   HELPER METHODS
    //************************

    /**
     * Removes a static game object.
     *
     * @param gameObj The object to remove.
     * @return True if the object was removed, false otherwise.
     */
    public Boolean removeStaticObject(GameObject gameObj) {
        return gameObjects().removeGameObject(gameObj, Layer.STATIC_OBJECTS);
    }

    /**
     * Removes a default game object.
     *
     * @param gameObj The object to remove.
     * @return True if the object was removed, false otherwise.
     */
    public Boolean removeDefaultObject(GameObject gameObj) {
        return gameObjects().removeGameObject(gameObj, Layer.DEFAULT);
    }

    /**
     * Decrements the paddles counter.
     */
    public void decrementPaddlesCounter() {
        paddleCount.decrement();
    }

    /**
     * Decrements the bricks counter.
     */
    public void decrementBricksCounter() {
        brickCount.decrement();
    }

    /**
     * Removes game objects that are out of bounds.
     */
    private void removeOutOfBoundsObjects() {
        for (GameObject gameObject : gameObjects()) {
            if (gameObject.getCenter().y() > windowDimensions.y()) {
                gameObjects().removeGameObject(gameObject);
            }
        }
    }

    /**
     * Respawns the ball at the center of the window with a random velocity.
     */
    private void resetBallPosition() {
        gameBall.setCenter(windowDimensions.mult(CENTER_RATIO));
        float ballVelX = BALL_INITIAL_SPEED;
        float ballVelY = BALL_INITIAL_SPEED;
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        gameBall.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Checks if the game has ended (either win or lose conditions).
     */
    private void evaluateGameEnd() {
        // Check if the ball is out of bounds
        double ballHeight = gameBall.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            if (lifeCount.value() > 0) {
                handleSingleLifeLose();
            }
            if (lifeCount.value() == 0) {
                handleLoseCondition();
            }
        }

        // Check if all bricks are gone
        if (brickCount.value() == 0) {
            handleWinCondition();
        }
    }

    /**
     * Handles the win condition.
     */
    private void handleWinCondition() {
        showGameOverPrompt(WIN_PROMPT_MSG);
    }

    /**
     * Handles the losing condition.
     */
    private void handleLoseCondition() {
        showGameOverPrompt(LOSE_PROMPT_MSG);
    }

    /**
     * Displays a game over prompt and resets or closes the game based on the player's choice.
     *
     * @param prompt The message to display in the prompt.
     */
    private void showGameOverPrompt(String prompt) {
        if (windowController.openYesNoDialog(prompt)) {
            paddleCount.reset();
            lifeCount.reset();
            lifeCount.increaseBy(DEFAULT_LIVES);
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /**
     * Handles the loss of a single life.
     */
    private void handleSingleLifeLose() {
        // Reset ball
        resetBallPosition();
        // Update lives counter and UI
        lifeCount.decrement();
        gameObjects().removeGameObject(lifeHearts[lifeCount.value()], Layer.UI);
        // Update lives UI text
        livesTextDisplay.setString(Integer.toString(lifeCount.value()));
        refreshLivesTextColor();
    }

    /**
     * Updates the color of the lives text based on the number of lives remaining.
     */
    private void refreshLivesTextColor() {
        switch (lifeCount.value()) {
            case DANGER_ZONE:
                livesTextDisplay.setColor(Color.YELLOW);
                break;
            case LAST_CHANCE:
                livesTextDisplay.setColor(Color.RED);
                break;
            default:
                livesTextDisplay.setColor(Color.GREEN);
        }
    }

    /**
     * return the window dimensions.
     */
    public Vector2 getWindowDimensions() {
        return windowDimensions;
    }


    /**
     * return the number of bricks per row.
     */
    public int numOfBricksPerRow() {
        return numOfBricksPerRow;
    }


    /**
     * return the text display for the lives.
     */
    public TextRenderable getLivesTextDisplay() {
        return livesTextDisplay;
    }

    /**
     * The main method to run the game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        int numOfBricksRows = DEFAULT_ROWS_OF_BRICKS;
        int numOfBricksPerRow = DEFAULT_BRICKS_PER_ROW;

        if (args.length == NUMBER_OF_ARGUMENTS) {
            numOfBricksRows = Integer.parseInt(args[0]);
            numOfBricksPerRow = Integer.parseInt(args[1]);
        }

        BrickerGameManager brickerGameManager = new BrickerGameManager(WINDOW_TITLE,
                new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT), numOfBricksRows, numOfBricksPerRow);
        brickerGameManager.run();
    }
}

