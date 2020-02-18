package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;

/**
 * A supper class of plants involving sprites.
 *
 * @invar | canHaveAsAcceleration(getAcceleration())
 * @invar | isValidSprites(getSprites())
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public abstract class Plant extends GameObject {

    /**
     * Initializes this new Plant with the given pixelPosition and sprites.
     *
     * @param pixelPosition
     *        The pixelPosition for this new Sneezewort
     * @param sprites
     *        The sprites for this new Sneezewort.
     * @post | new.getSprites() = sprites
     * @effect | super(pixelPosition)
     * @throws IllegalArgumentException
     *         | ! isValidSprites(sprites)
     */
    @Raw
    Plant(int[] pixelPosition, Sprite[] sprites) throws IllegalArgumentException {
        super(pixelPosition);
        if (! isValidSprites(sprites))
            throw new IllegalArgumentException();
        this.sprites = sprites.clone();
    }

// #####################################################################################################################

    /**
     * Check whether the given acceleration is a valid acceleration for any Plant.
     *
     * @param acceleration
     *        The given acceleration to check.
     * @return | result == (acceleration[0] == 0) && (acceleration[1] == 0)
     */
    @Override
    public boolean canHaveAsAcceleration(double[] acceleration) {
        return (acceleration[0] == 0) && (acceleration[1] == 0);
    }

// #####################################################################################################################

    /**
     * Return the sprite to be displayed for this Plant.
     *
     * @return | if (getOrientation() == 1)
     *         |    then result == (this instanceof Sneezewort) ? getSprites()[1] : getSprites()[0]
     *         |    else then result == (this instanceof Sneezewort) ? getSprites()[0] : getSprites()[1]
     */
    @Override
    public Sprite getCurrentSprite() {
        if (getOrientation() == 1)
            return (this instanceof Sneezewort) ? getSprites()[1] : getSprites()[0];
        return (this instanceof Sneezewort) ? getSprites()[0] : getSprites()[1];
    }

    /**
     * Return the sprites of this Sneezewort.
     */
    @Basic @Raw @Immutable
    public Sprite[] getSprites() { return this.sprites; }

    /**
     * Check whether this Sneezewort can have the given sprite as its sprite.
     *
     * @param sprites
     *        The given sprites to check
     * @return | result == (sprites != null) && (sprites.length == 2) && (sprites[0] != null) && (sprites[1] != null)
     */
    private static boolean isValidSprites(Sprite[] sprites) {
        return sprites != null && sprites.length == 2 && sprites[0] != null && sprites[1] != null;
    }

    /**
     * Variable registering the sprite of this Mazub.
     */
    private final Sprite[] sprites;
}
