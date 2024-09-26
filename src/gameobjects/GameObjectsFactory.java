package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The GameObjectsFactory class is responsible for creating various game objects in the Bricker game.
 * It provides methods to instantiate objects based on their tags, ensuring that game elements are
 * created with appropriate properties and behaviors.
 */
public class GameObjectsFactory {

    // Constants for defining dimensions and paths for game assets
    private static final int BALL_WIDTH = 20;
    private static final int BALL_HEIGHT = 20;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_WIDTH = 100;
    private static final float PUCK_FACTOR = 0.75f;
    private static final String BG_IMG_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BALL_SOUND_PATH = "assets/Bubble5_4.wav";
    private static final String BALL_IMG_PATH = "assets/ball.png";
    private static final String BRICK_IMG_PATH = "assets/brick.png";
    private static final String PADDLE_IMG_PATH = "assets/paddle.png";
    private static final String PUCK_IMG_PATH = "assets/mockBall.png";
    /**
     * The path to the heart image used for displaying lives in the game.
     */
    public static final String HEART_IMG_PATH = "assets/heart.png";

    private final BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;

    /**
     * Constructs a new GameObjectsFactory.
     *
     * @param brickerGameManager The game manager responsible for handling the game state and logic.
     * @param imageReader        The image reader used for loading images from files.
     * @param soundReader        The sound reader used for loading and playing sound effects.
     * @param inputListener      The input listener that captures user input.
     */
    public GameObjectsFactory(BrickerGameManager brickerGameManager, ImageReader imageReader,
                              SoundReader soundReader, UserInputListener inputListener) {
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowDimensions = brickerGameManager.getWindowDimensions();
        this.inputListener = inputListener;
    }

    /**
     * Builds a game object based on its tag.
     *
     * @param tag The tag identifying the type of game object to build.
     *            Possible tags include: "Background", "Ball", "Brick", "Heart",
     *            "Paddle", "Puck", and "SecondPaddle".
     * @return The constructed game object. Returns a specific type of game object
     *         based on the provided tag. If the tag is not recognized, it returns null.
     */
    public GameObject buildObject(String tag) {
        switch (tag) {
            case Background.TAG:
                Renderable bgImg = imageReader.readImage(BG_IMG_PATH, false);
                return new Background(Vector2.ZERO, windowDimensions, bgImg);
            case Ball.TAG:
                Sound ballCollisionSound = soundReader.readSound(BALL_SOUND_PATH);
                Renderable ballImage = imageReader.readImage(BALL_IMG_PATH, true);
                return new Ball(Vector2.ZERO, new Vector2(BALL_WIDTH, BALL_HEIGHT),
                        ballImage, ballCollisionSound);
            case Brick.TAG:
                return createBrick();
            case Heart.TAG:
                Renderable heartImage = imageReader.readImage(HEART_IMG_PATH, true);
                return new Heart(Vector2.ZERO, new Vector2(BrickerGameManager.UI_ICON_SIZE,
                        BrickerGameManager.UI_ICON_SIZE), heartImage, brickerGameManager);
            case Paddle.TAG:
                Renderable paddleImage = imageReader.readImage(PADDLE_IMG_PATH, false);
                return new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                        inputListener, windowDimensions);
            case Puck.TAG:
                Sound puckCollisionSound = soundReader.readSound(BALL_SOUND_PATH);
                Renderable puckImage = imageReader.readImage(PUCK_IMG_PATH, true);
                return new Puck(Vector2.ZERO, new Vector2(BALL_HEIGHT * PUCK_FACTOR,
                        BALL_WIDTH * PUCK_FACTOR), puckImage, puckCollisionSound);
            case SecondPaddle.TAG:
                Renderable secondPaddleImg = imageReader.readImage(PADDLE_IMG_PATH, false);
                return new SecondPaddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                        secondPaddleImg, inputListener, windowDimensions, brickerGameManager);
            case Wall.TAG:
                return new Wall(Vector2.ZERO, new Vector2(windowDimensions.x(),
                        BrickerGameManager.WALL_THICKNESS),
                        new RectangleRenderable(Color.BLACK));
            case HeartUI.TAG:
                return new HeartUI(Vector2.ZERO, new Vector2(BrickerGameManager.UI_ICON_SIZE,
                        BrickerGameManager.UI_ICON_SIZE), imageReader.readImage(HEART_IMG_PATH, true));
            case TextUI.TAG:
                return new TextUI(Vector2.ZERO, new Vector2(BrickerGameManager.UI_ICON_SIZE,
                        BrickerGameManager.UI_ICON_SIZE), brickerGameManager.getLivesTextDisplay());
            default:
                return null;
        }
    }

    /**
     * Creates a brick object with a specific collision strategy.
     * This method is separated to encapsulate the logic for brick creation,
     * which involves reading images and setting up collision strategies.
     *
     * @return A new Brick object initialized with default dimensions and collision strategy.
     */
    private GameObject createBrick() {
        Renderable brickImage = imageReader.readImage(BRICK_IMG_PATH, false);
        CollisionStrategyFactory collisionStrategyFactory = new CollisionStrategyFactory();
        CollisionStrategy collisionStrategy =
                collisionStrategyFactory.getCollisionStrategy(brickerGameManager);
        float brickWidth = calculateBrickWidth();

        return new Brick(Vector2.ZERO, new Vector2(brickWidth, BrickerGameManager.BRICK_HEIGHT),
                brickImage, collisionStrategy, brickerGameManager);
    }


    /**
     * Calculates the width of each brick based on the number of bricks per row,
     * the window dimensions, spacing between bricks, and the wall thickness.
     * This ensures that bricks are evenly spaced and fit within the game window.
     *
     * @return The calculated width of each brick.
     */
    private float calculateBrickWidth() {
        int numOfBricksPerRow = brickerGameManager.numOfBricksPerRow();
        // Calculate the width of each brick based on the number of bricks per row,
        // the window dimensions, spacing between bricks and 2 wall thicknesses.
        float totalBrickWidth = windowDimensions.x() - (BrickerGameManager.WALL_THICKNESS * 2);
        float spacing = BrickerGameManager.BRICK_SPACING * (numOfBricksPerRow + 1);
        return (totalBrickWidth - spacing) / numOfBricksPerRow;
    }
}