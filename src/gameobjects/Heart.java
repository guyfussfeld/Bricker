package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Heart class represents a heart object in the Bricker game. It extends the GameObject class and
 * adds functionality for falling hearts that interact with the paddle to add lives to the player.
 */
public class Heart extends GameObject {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "Heart";

    /**
     * The speed at which the heart falls.
     */
    private static final int HEART_INITIAL_SPEED = 100;

    /**
     * The game manager for the Bricker game.
     */
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new Heart object.
     *
     * @param topLeftCorner      The top-left corner position of the heart.
     * @param dimensions         The dimensions of the heart.
     * @param renderable         The renderable for the heart.
     * @param brickerGameManager The game manager for the Bricker game.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable); // Call the superclass constructor
        setTag(TAG); // Set the tag for this object
        this.brickerGameManager = brickerGameManager; // Initialize the game manager
        setVelocity(new Vector2(0, HEART_INITIAL_SPEED)); // Set the initial velocity of the heart
    }

    /**
     * Determines if this object should collide with another game object.
     *
     * @param other The other game object.
     * @return True if the other game object is a paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Paddle.TAG); // Check if the other object is a paddle
    }

    /**
     * Called when a collision occurs with this heart object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Add a life to the player and remove the heart object
        brickerGameManager.incrementLife();
        brickerGameManager.removeDefaultObject(this);
    }
}