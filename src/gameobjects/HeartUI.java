package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class HeartUI extends GameObject {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "HeartUI";

    /**
     * Constructs a new HeartUI object.
     *
     * @param topLeftCorner The top-left corner position of the HeartUI.
     * @param dimensions    The dimensions of the HeartUI.
     * @param renderable    The renderable for the HeartUI.
     */
    public HeartUI(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable); // Call the superclass constructor
        setTag(TAG); // Set the tag for this object
    }
}
