package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

import java.util.Random;

/**
 * The DoubleCollisionStrategy class implements the CollisionStrategy interface
 * to define the collision behavior when a brick encounters another object twice
 * in the Bricker game.
 */
public class DoubleCollisionStrategy implements CollisionStrategy {

    private static final int BEHAVIOR_CAP = 2;
    private static final int PUCK_STRATEGY = 0;
    private static final int PADDLE_STRATEGY = 1;
    private static final int CAMERA_STRATEGY = 2;
    private static final int HEART_STRATEGY = 3;
    private static final int RANDOM_RANGE = 5;

    private final BrickerGameManager brickerGameManager;
    private final Random rand = new Random();

    /**
     * Constructs a new DoubleCollisionStrategy.
     *
     * @param brickerGameManager The BrickerGameManager instance to interact with the game environment.
     */
    public DoubleCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision when a brick encounters another object twice.
     *
     * @param thisObj  The first game object involved in the collision.
     * @param otherObj The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        Brick brick = (Brick) thisObj;
        // Check if the maximum collisions limit is reached
        if (brick.getCollisionStrategiesCounter().value() == Brick.MAX_COLLISIONS) {
            return;
        }
        // Execute collision strategies twice
        for (int i = 0; i < BEHAVIOR_CAP; i++) {
            // Generate a random number to choose a collision strategy
            int random = rand.nextInt(RANDOM_RANGE);
            switch (random) {
                case PUCK_STRATEGY:
                    new PuckCollisionStrategy(brickerGameManager).onCollision(thisObj, otherObj);
                    break;
                case PADDLE_STRATEGY:
                    new PaddleCollisionStrategy(brickerGameManager).onCollision(thisObj, otherObj);
                    break;
                case CAMERA_STRATEGY:
                    new CameraCollisionStrategy(brickerGameManager).onCollision(thisObj, otherObj);
                    break;
                case HEART_STRATEGY:
                    new HeartCollisionStrategy(brickerGameManager).onCollision(thisObj, otherObj);
                    break;
                default:
                    new DoubleCollisionStrategy(brickerGameManager).onCollision(thisObj, otherObj);
            }
        }
    }
}