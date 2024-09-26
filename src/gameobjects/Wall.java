package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Wall class represents a wall in the Bricker game. It extends the GameObject class and provides
 * functionality to set up a wall with specific dimensions, position, and renderable.
 */
public class Wall extends GameObject {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "Wall";

    /**
     * Constructs a new Wall object.
     *
     * @param topLeftCorner The top-left corner position of the wall.
     * @param dimensions    The dimensions of the wall.
     * @param renderable    The renderable for the wall.
     */
    public Wall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable); // Call the superclass constructor
        setTag(TAG); // Set the tag for this object
    }
}