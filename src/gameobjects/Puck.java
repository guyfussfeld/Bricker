package bricker.gameobjects;

import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Puck class represents a puck object in the Bricker game. It extends the Ball class and is used to
 * create puck objects with specific dimensions, position, renderable, and collision sound.
 */
public class Puck extends Ball {

    /**
     * The tag identifying this type of game object.
     */
    public static final String TAG = "Puck";

    /**
     * Constructs a new Puck object.
     *
     * @param topLeftCorner  The top-left corner position of the puck.
     * @param dimensions     The dimensions of the puck.
     * @param renderable     The renderable for the puck.
     * @param collisionSound The sound to play upon collision.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable, collisionSound); // Call the superclass constructor
        setTag(TAG); // Set the tag for this object
    }
}