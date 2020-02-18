package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;

/**
 * This Position class is parameterized in the type T of its elements.
 *
 * @param <T> The given position.
 */
public class Position <T> {

    /**
     * Initializes this Position with the given x and y position.
     * @param x The given x position.
     * @param y The given y position.
     * @effect | setX(x)
     * @effect | setY(y)
     */
    public Position(T x, T y) {
        setX(x);
        setY(y);
    }

// #####################################################################################################################

    /**
     * Return the x position of this Position.
     */
    @Raw
    public T getX() {
        return x;
    }

    /**
     * Set the x position to the given x position.
     *
     * @param x The given x position.
     * @post | new.x = x
     */
    public void setX(T x) {
        this.x = x;
    }

    /**
     * Variable registering the x position of this Position.
     */
    private T x;

// #####################################################################################################################

    /**
     * Return the x position of this Position.
     */
    @Raw
    public T getY() {
        return y;
    }

    /**
     * Set the y position to the given y position.
     *
     * @param y
     * @post | new.y = y
     */
    public void setY(T y) {
        this.y = y;
    }

    /**
     * Variable registering the y position of this Position.
     */
    private T y;
}
