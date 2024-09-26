package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The Paddle class represents a paddle object in the Bricker game. It extends the GameObject class and
 * adds functionality for user-controlled movement.
 */
public class Paddle extends GameObject {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "Paddle";

    /**
     * The movement speed of the paddle.
     */
    private static final float MOVEMENT_SPEED = 300f;


    /**
     * The input listener for controlling the paddle.
     */
    private final UserInputListener inputListener;

    /**
     * The dimensions of the game window.
     */
    private final Vector2 windowDimensions;

    /**
     * The dimensions of the paddle.
     */
    private final Vector2 dimensions;

    /**
     * Constructs a new Paddle object.
     *
     * @param topLeftCorner    The top-left corner position of the paddle.
     * @param dimensions       The dimensions of the paddle.
     * @param renderable       The renderable for the paddle.
     * @param inputListener    The input listener for controlling the paddle.
     * @param windowDimensions The dimensions of the game window.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable); // Call the superclass constructor
        this.inputListener = inputListener; // Initialize the input listener
        this.windowDimensions = windowDimensions; // Initialize the window dimensions
        this.dimensions = dimensions; // Initialize the paddle dimensions
        setTag(TAG); // Set the tag for this object
    }

    /**
     * Updates the state of the paddle based on user input.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Call the superclass update method

        Vector2 movementDir = Vector2.ZERO; // Initialize the movement direction vector

        // Check if the left arrow key is pressed and adjust the movement direction accordingly
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (getTopLeftCorner().x() < BrickerGameManager.WALL_THICKNESS) {
                setTopLeftCorner(new Vector2(BrickerGameManager.WALL_THICKNESS, getTopLeftCorner().y()));
            } else {
                movementDir = movementDir.add(Vector2.LEFT);
            }
        }

        // Check if the right arrow key is pressed and adjust the movement direction accordingly
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            float rightBound = windowDimensions.x() - BrickerGameManager.WALL_THICKNESS - dimensions.x();
            if (getTopLeftCorner().x() > rightBound) {
                setTopLeftCorner(new Vector2(rightBound, getTopLeftCorner().y()));
            } else {
                movementDir = movementDir.add(Vector2.RIGHT);
            }
        }

        // Set the velocity of the paddle based on the movement direction and speed
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}