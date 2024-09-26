package bricker.gameobjects;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The Brick class represents a brick object in the Bricker game. It extends the GameObject class and
 * adds functionality for handling collisions using different strategies.
 */
public class Brick extends GameObject {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "Brick";

    /**
     * The maximum number of collisions allowed for the brick.
     */
    public static final int MAX_COLLISIONS = 3;

    /**
     * The collision strategy to use when the brick is hit.
     */
    private final CollisionStrategy collisionStrategy;

    /**
     * The counter for the collision strategies.
     */
    private final Counter collisionStrategiesCounter;

    /**
     * The basic collision strategy used as a fallback.
     */
    private final BasicCollisionStrategy basicCollisionStrategy;

    /**
     * The width of the brick.
     */
    private final float width;

    /**
     * Constructs a new Brick object.
     *
     * @param topLeftCorner      The top-left corner position of the brick.
     * @param dimensions         The dimensions of the brick.
     * @param renderable         The renderable for the brick.
     * @param collisionStrategy  The collision strategy to use when the brick is hit.
     * @param brickerGameManager The game manager for the Bricker game.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.collisionStrategiesCounter = new Counter();
        this.basicCollisionStrategy = new BasicCollisionStrategy(brickerGameManager);
        this.width = dimensions.x();
        setTag(TAG);
    }

    /**
     * Called when a collision occurs with this brick object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // Apply the basic collision strategy
        basicCollisionStrategy.onCollision(this, other);

        // If the basic collision resulted in the removal of the brick, apply the custom collision strategy
        if (basicCollisionStrategy.getCollision()) {
            collisionStrategy.onCollision(this, other);
        }
    }

    /**
     * Gets the counter for the collision strategies.
     *
     * @return The counter for the collision strategies.
     */
    public Counter getCollisionStrategiesCounter() {
        return collisionStrategiesCounter;
    }

    /**
     * Increments the counter for the collision strategies.
     */
    public void incrementCollisionStrategiesCounter() {
        collisionStrategiesCounter.increment();
    }

    /**
     * Gets the width of the brick.
     *
     * @return The width of the brick.
     */
    public float getWidth() {
        return width;
    }
}