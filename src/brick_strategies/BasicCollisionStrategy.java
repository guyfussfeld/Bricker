package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * The BasicCollisionStrategy class defines a basic collision handling mechanism for bricks
 * in the Bricker game. It implements the CollisionStrategy interface to remove bricks from
 * the game when they collide with another object, decrementing the game's brick counter.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    /**
     * Flag indicating whether a collision resulted in the removal of the brick.
     */
    private Boolean collision;

    /**
     * Instance of BrickerGameManager to interact with the game environment and manage game state.
     */
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new BasicCollisionStrategy.
     *
     * @param brickerGameManager The BrickerGameManager instance to interact with the game environment.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
        collision = false;
    }


    /**
     * Handles the collision between two game objects, removing the brick from the game
     * if it collides with another object and updating the game state.
     *
     * @param thisObj  The game object representing the brick involved in the collision.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Attempt to remove the brick from the game
        if (brickerGameManager.removeStaticObject(thisObj)) {
            // If removal is successful, decrement the brick counter and mark the collision as true
            brickerGameManager.decrementBricksCounter();
            collision = true;
        }
    }

    /**
     * Returns true if the collision resulted in breaking the brick, else false.
     *
     * @return Boolean indicating whether the collision broke the brick.
     */
    public Boolean getCollision() {
        return collision;
    }
}