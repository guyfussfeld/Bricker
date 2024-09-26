package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class TextUI extends GameObject {
    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "TextUI";

    /**
     * Constructs a new TextUI object.
     *
     * @param topLeftCorner The top-left corner position of the TextUI.
     * @param dimensions    The dimensions of the TextUI.
     * @param renderable    The renderable for the TextUI.
     */
    public TextUI(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable); // Call the superclass constructor
        setTag(TAG); // Set the tag for this object
    }
}
