package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * The PaddleCollisionStrategy class implements the CollisionStrategy interface
 * to define the collision behavior when a brick collides with a paddle in the Bricker game.
 */
public class PaddleCollisionStrategy implements CollisionStrategy {

    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new PaddleCollisionStrategy.
     *
     * @param brickerGameManager The BrickerGameManager instance to interact with the game environment.
     */
    public PaddleCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between a brick and a paddle.
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
        // Create a second paddle and increment the collision strategies counter
        brickerGameManager.secondPaddleCreator();
        brick.incrementCollisionStrategiesCounter();
    }
}