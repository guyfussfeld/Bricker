package bricker.brick_strategies;

import danogl.GameObject;

/**
 * The CollisionStrategy interface defines a strategy for handling collisions between game objects.
 */
public interface CollisionStrategy {

    /**
     * Handles the collision between two game objects.
     *
     * @param thisObj  The first game object involved in the collision.
     * @param otherObj The second game object involved in the collision.
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}