package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Background class represents a background object in the Bricker game.
 * It extends the GameObject class and serves as a static background in the game.
 */
public class Background extends GameObject {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "Background";

    /**
     * Constructs a new Background object.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Background(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        setTag(TAG);
    }
}