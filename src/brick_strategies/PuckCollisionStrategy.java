package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * The PuckCollisionStrategy class implements the CollisionStrategy interface
 * to define the collision behavior when a puck collides with a brick in the Bricker game.
 */
public class PuckCollisionStrategy implements CollisionStrategy {

    /**
     * The BrickerGameManager instance to interact with the game environment.
     */
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new PuckCollisionStrategy.
     *
     * @param brickerGameManager The BrickerGameManager instance to interact with the game environment.
     */
    public PuckCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between a puck and a brick.
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
        // Create new pucks and increment the collision strategies counter
        brickerGameManager.puckBallsCreator(thisObj);
        brick.incrementCollisionStrategiesCounter();
    }
}