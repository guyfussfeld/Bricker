package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The SecondPaddle class represents an additional paddle in the Bricker game.
 * It extends the Paddle class and adds functionality to handle collisions
 * and track the number of collisions with balls and pucks.
 */
public class SecondPaddle extends Paddle {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "SecondPaddle";

    /**
     * The maximum number of collisions allowed before removal.
     */
    public static final int COLLISIONS_ALLOWED = 4;

    /**
     * Counter for tracking collisions with balls and pucks.
     */
    protected Counter collisionCounter;

    /**
     * The game manager for the Bricker game.
     */
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new SecondPaddle object.
     *
     * @param topLeftCorner      The top-left corner position of the paddle.
     * @param dimensions         The dimensions of the paddle.
     * @param renderable         The renderable for the paddle.
     * @param inputListener      The input listener for controlling the paddle.
     * @param windowDimensions   The dimensions of the game window.
     * @param brickerGameManager The game manager for the Bricker game.
     */
    public SecondPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        UserInputListener inputListener, Vector2 windowDimensions,
                        BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.collisionCounter = new Counter(); // Initialize the collision counter
        this.brickerGameManager = brickerGameManager; // Set the game manager
        setTag(TAG); // Set the tag for this object
    }

    /**
     * Handles collision with other game objects.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision); // Call the superclass method
        // Increment collision counter if the collision involves a ball or a puck
        if (other.getTag().equals(Ball.TAG) || other.getTag().equals(Puck.TAG)) {
            collisionCounter.increment();
            // Remove the paddle if collision limit is reached
            if (collisionCounter.value() == COLLISIONS_ALLOWED) {
                // Remove the paddle and decrement the paddle counter
                if (brickerGameManager.removeDefaultObject(this)) {
                    brickerGameManager.decrementPaddlesCounter();
                }
            }
        }
    }
}