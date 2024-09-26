package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * The CameraCollisionStrategy class implements the CollisionStrategy interface
 * to define the collision behavior when a brick collides with another object in the Bricker game.
 */
public class CameraCollisionStrategy implements CollisionStrategy {

    /** The BrickerGameManager instance to interact with the game environment. */
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new CameraCollisionStrategy.
     *
     * @param brickerGameManager The BrickerGameManager instance to interact with the game environment.
     */
    public CameraCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between a brick and another game object.
     *
     * @param thisObj  The brick object involved in the collision.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        Brick brick = (Brick) thisObj;

        // Check if the brick has reached its maximum collision limit
        if (brick.getCollisionStrategiesCounter().value() == Brick.MAX_COLLISIONS) {
            return;
        }

        // Set the collision camera and increment the collision strategies counter
        brickerGameManager.activateCollisionCamera(otherObj);
        brick.incrementCollisionStrategiesCounter();
    }
}