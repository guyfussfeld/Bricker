package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * The CollisionStrategyFactory class generates collision strategies based on a random selection.
 */
public class CollisionStrategyFactory {


    // Constants representing different cases for strategy selection
    private static final int PUCK_STRATEGY = 0;
    private static final int PADDLE_STRATEGY = 1;
    private static final int CAMERA_STRATEGY = 2;
    private static final int HEART_STRATEGY = 3;
    private static final int DOUBLE_STRATEGY = 4;
    private static final int RANDOM_RANGE = 10;

    // Random number generator for strategy selection
    private final Random rand = new Random();

    /**
     * Generates a collision strategy based on a random selection.
     *
     * @param brickerGameManager The BrickerGameManager instance to interact with the game environment.
     * @return A CollisionStrategy instance based on the random selection.
     */
    public CollisionStrategy getCollisionStrategy(BrickerGameManager brickerGameManager) {
        int random = rand.nextInt(RANDOM_RANGE);
        switch (random) {
            case PUCK_STRATEGY:
                return new PuckCollisionStrategy(brickerGameManager);
            case PADDLE_STRATEGY:
                return new PaddleCollisionStrategy(brickerGameManager);
            case CAMERA_STRATEGY:
                return new CameraCollisionStrategy(brickerGameManager);
            case HEART_STRATEGY:
                return new HeartCollisionStrategy(brickerGameManager);
            case DOUBLE_STRATEGY:
                return new DoubleCollisionStrategy(brickerGameManager);
            default: // BASIC_STRATEGY with a probability of 50%
                return new BasicCollisionStrategy(brickerGameManager);
        }
    }
}