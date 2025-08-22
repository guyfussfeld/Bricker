package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.gui.Sound;

/**
 * The Ball class represents a ball object in the Bricker game. It extends the GameObject class and
 * adds functionality for handling collisions with sound effects and a counter.
 */
public class Ball extends GameObject {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "Ball";

    /**
     * The counter for the number of collisions.
     */
    private int collisionCounter;

    /**
     * The sound played upon collision.
     */
    private final Sound collisionSound;

    /**
     * Constructs a new Ball object.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound The sound to play upon collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = 0;
        setTag(TAG);
    }

    /**
     * Called when a collision occurs with this ball object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Change the velocity of the ball to reflect the collision
        Vector2 newVal = getVelocity().flipped(collision.getNormal());
        setVelocity(newVal);
        // Increment the collision counter
        collisionCounter++;
        // Play the collision sound
        collisionSound.play();
    }

    /**
     * Gets the counter for the number of collisions.
     *
     * @return The counter for the number of collisions.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }
}