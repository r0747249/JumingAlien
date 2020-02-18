package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;
import static jumpingalien.model.PositionConverter.*;

/**
 * A class of GameObjects containing a position, a velocity, an acceleration, timedifference, orientation
 * hitPoints and a world.
 *
 * @invar The actualPosition of each GameObject must be a valid actualPosition for this GameObject
 *        | canHaveAsActualPosition(getActualPosition())
 *
 * @invar The velocity of each GameObject must be a valid velocity for this GameObject
 *        | canHaveAsVelocity(getVelocity())
 *
 * @invar The acceleration of each GameObject must be a valid acceleration for this GameObject
 *        | canHaveAsAcceleration(getAcceleration())
 *
 * @invar The timeDifference of each GameObject must be a valid timeDifference for any GameObject.
 *        | isValidTimeDifference(getTimeDifference())
 *
 * @invar The orientation of each GameObject must be a valid orientation for any GameObject.
 *        | isValidOrientation(getOrientation())
 *
 * @invar The Hit-Points of each GameObject must be a valid Hit-Points for this GameObject.
 *        | canHaveAsHitPoints(getHitPoints())
 *
 * @invar The world of each GameObject must be a valid world for this GameObject.
 *        | isValidWorld(getWorld())
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public abstract class GameObject {

    /**
     * Initialize this new GameObject with given pixelPosition.
     *
     * @effect The actualPosition is set to the given actualPosition.
     *         | setActualPosition(new double[]{new Position<>(pixelToMeters(pixelPosition[0]), pixelToMeters(pixelPosition[1])).getX(), new Position<>(pixelToMeters(pixelPosition[0]), pixelToMeters(pixelPosition[1])).getY()})
     */
    @Raw @Model
    protected GameObject(int[] pixelPosition) {
        Position<Double> actual = new Position<>(pixelToMeters(pixelPosition[0]), pixelToMeters(pixelPosition[1]));
        setActualPosition(new double[]{actual.getX(), actual.getY()});
    }

// #####################################################################################################################

    /**
     * Return the sprite to be displayed for this GameObject.
     */
    public abstract Sprite getCurrentSprite();

    /**
     * Return the sprites of this GameObject.
     */
    @Basic @Raw @Immutable
    public abstract Sprite[] getSprites();

// #####################################################################################################################

    /**
     * Calculate the new position in meters.
     *
     * @return Returns the new horizontal and vertical position in meters.
     *         | result == new double[]{getActualPosition()[0] + getVelocity()[0] * getTimeDifference() + 0.5 * getAcceleration()[0] * Math.pow(getTimeDifference(), 2),
     *         |                        getActualPosition()[1] + getVelocity()[1] * getTimeDifference() + 0.5 * getAcceleration()[1] * Math.pow(getTimeDifference(), 2)}
     */
    public double[] calculateNewActualPosition() {
        double newActualPositionX = getActualPosition()[0] + getVelocity()[0] * getTimeDifference() + 0.5 * getAcceleration()[0] * Math.pow(getTimeDifference(), 2);
        double newActualPositionY = getActualPosition()[1] + getVelocity()[1] * getTimeDifference() + 0.5 * getAcceleration()[1] * Math.pow(getTimeDifference(), 2);
        return new double[]{newActualPositionX, newActualPositionY};
    }

// #####################################################################################################################

    /**
     * Return the actual position in meters to pixels.
     *
     * @return Return an int[] with the pixel position of the GameObject
     *         | result == new int[]{metersToPixel(getActualPosition()[0]), metersToPixel(getActualPosition()[1])}
     */
    public int[] getPixelPosition() {
        return new int[]{metersToPixel(getActualPosition()[0]), metersToPixel(getActualPosition()[1])};
    }

// #####################################################################################################################

    /**
     * Return the actualPosition of this GameObject.
     *
     * @return returns the double[] actualPosition
     *         | result == this.actualPosition
     */
    @Basic @Raw
    public double[] getActualPosition() { return this.actualPosition; }

    /**
     * Check if this GameObject can have the given actualPosition as its actualPosition.
     *
     * @param actualPosition
     *        The actualPosition to check.
     * @return Returns true when the actualPosition is a double[2] an contains 2 numbers that are between 0 and world height and width.
     *         | result == actualPosition != null && actualPosition.length == 2 && (!Double.isNaN(actualPosition[0])) && (!Double.isNaN(actualPosition[1]))
     *         |       && actualPosition[0]>=0 && actualPosition[1]*100+1>=0 && ((getWorld()==null) || actualPosition[0]<pixelToMeters(getWorld().getWidth()))
     *         |       && ((getWorld()==null) || actualPosition[1]<pixelToMeters(getWorld().getHeight()))
     */
    public boolean canHaveAsActualPosition(double[] actualPosition) {
        return actualPosition != null && actualPosition.length == 2 && (!Double.isNaN(actualPosition[0])) && (!Double.isNaN(actualPosition[1]))
                && actualPosition[0]>=0 && actualPosition[1]*100+1>=0 && ((getWorld()==null) || actualPosition[0]<pixelToMeters(getWorld().getWidth()))
                && ((getWorld()==null) || actualPosition[1]<pixelToMeters(getWorld().getHeight()));
    }

    /**
     * Set the actualPosition to the given actualPosition.
     *
     * @param actualPosition
     *        The new actualPosition of this GameObject.
     * @post The actualPosition of this new GameObject is equal to the given actualPosition if the GameObject is not deadnor terminated.
     *       | if (!isDead() && !isTerminated())
     *       |    then new.getActualPosition() == actualPosition
     * @throws IllegalArgumentException
     *         The given actualPosition is not a valid actualPosition for this GameObject.
     *         | ! canHaveAsActualPosition(actualPosition)
     */
    @Raw
    public void setActualPosition(double[] actualPosition) throws IllegalArgumentException {
        if (!isDead() && !isTerminated()) {
            if (!canHaveAsActualPosition(actualPosition))
                throw new IllegalArgumentException();
            this.actualPosition = actualPosition;
        }
    }

    /**
     * Variable registering the actualPosition of this GameObject.
     */
    private double[] actualPosition = new double[]{0, 0};

// #####################################################################################################################

    /**
     * Calculates the new potential Velocity with the velocity formula: vₓ = vₓ + aₓ • Δt.
     *
     * @return The new velocity for this GameObject that is calculated with the velocity formula.
     *         | if (new double[]{newHorizontalVelocity, newVerticalVelocity})
     *         |    then result == new double[]{newHorizontalVelocity, newVerticalVelocity}
     *         |    else then result == correctVelocity(new double[]{newHorizontalVelocity, newVerticalVelocity})
     */
    public double[] calculateNewVelocity() {
        double newHorizontalVelocity = getVelocity()[0] + getAcceleration()[0] * getTimeDifference();
        double newVerticalVelocity = getVelocity()[1] + getAcceleration()[1] * getTimeDifference();
        if (canHaveAsVelocity(new double[]{newHorizontalVelocity, newVerticalVelocity}))
            return new double[]{newHorizontalVelocity, newVerticalVelocity};
        else
            return correctVelocity(new double[]{newHorizontalVelocity, newVerticalVelocity});
    }

    /**
     * Corrects the velocity that is given if no method correctVelocity exist in the subclass.
     *
     * @param newVelocity
     *        The new velocity fot this GameObject.
     * @return Returns the default velocity of 0.
     *         | result == new double[]{0,0}
     */
    public double[] correctVelocity(double[] newVelocity) { return new double[]{0,0}; }

    /**
     * Returns the velocity of this GameObject.
     */
    @Basic
    public double[] getVelocity() { return velocity; }

    /**
     * Returns true if this GameObject can have the given velocity as velocity.
     *  The given velocity must be between the GameObject's minimum and maximum.
     *
     * @param velocity
     *        The velocity to check.
     * @return Returns true when the velocity is between the maximum and minimum value (or is equal to the default value).
     *         | result == (velocity >= minVelocity) && (velocity <= maxvelocity) || (velocity = defaultVelocity)
     */
    public abstract boolean canHaveAsVelocity(double[] velocity);

    /**
     * Sets the velocity to the given velocity when its valid and when the GameObject is not dead nor terminated.
     *
     * @param velocity
     *        The velocity to set.
     * @post The velocity is set to the given velocity if its valid and if this Gameobject is not dead nor terminated.
     *       | if (canHaveAsVelocity(velocity) && !isDead() && !isTerminated())
     *       |    then new.velocity == velocity
     */
    public void setVelocity(double[] velocity) {
        if (canHaveAsVelocity(velocity) && !isDead() && !isTerminated())
            this.velocity = velocity;
    }

    /**
     * Variable registering the horizontal and vertical velocity of this GameObject.
     */
    private double[] velocity = new double[]{0, 0};

// #####################################################################################################################

    /**
     * Returns the acceleration of this GameObject.
     */
    @Basic @Raw
    public double[] getAcceleration(){ return this.acceleration;}

    /**
     * Returns true if this GameObject can have the given acceleration as acceleration.
     *
     * @param acceleration
     *        The acceleration to check
     */
    public abstract boolean canHaveAsAcceleration(double[] acceleration);

    /**
     * Sets the acceleration of this GameObject to the given acceleration.
     *
     * @param acceleration
     *        The new acceleration for this GameObject.
     * @post The acceleration of this new GameObject is equal to the given acceleration.
     *       | new.acceleration == acceleration
     * @throws IllegalArgumentException
     *         The given acceleration is not a valid acceleration for this GameObject.
     *         | ! canHaveAsAcceleration(acceleration)
     */
    @Raw
    public void setAcceleration(double[] acceleration) throws IllegalArgumentException {
        if (! canHaveAsAcceleration(acceleration))
            throw new IllegalArgumentException("Not a valid acceleration");
        this.acceleration = acceleration;
    }

    /**
     * Variable registering the acceleration of this GameObject.
     */
    private double[] acceleration = new double[]{0,0};

// #####################################################################################################################

    /**
     * Returns the timeDifference of this GameObject.
     */
    @Basic @Raw
    public double getTimeDifference() { return timeDifference; }

    /**
     * Checks if the given timeDifference is a valid timeDifference for this GameObject.
     *
     * @param timeDifference
     *        The timeDifference to check.
     * @return Returns true if the value of timeDifference is between 0 and 0.2 seconds.
     *         | result == (timeDifference >= 0) && (timeDifference < 0.2)
     */
    private static boolean isValidTimeDifference(double timeDifference) {
        return (timeDifference >= 0) && (timeDifference < 0.2);
    }

    /**
     * Sets the timeDifference of this GameObject to the given timeDifference.
     *
     * @param timeDifference
     *        The new timeDifference for this GameObject.
     * @post If the given timeDifference is a valid timeDifference for any GameObject,
     *       the timeDifference of this new GameObject is equal to the given timeDifference.
     *       | if (isValidTimeDifference(timeDifference))
     *       |   then new.getTimeDifference() == timeDifference
     *       | else if (timeDifference < 0)
     *       |   then new.getTimeDifference() == 0
     *       | else then new.getTimeDifference() == 0.2
     */
    @Raw
    public void setTimeDifference(double timeDifference) {
        if (isValidTimeDifference(timeDifference))
            this.timeDifference = timeDifference;
        else if (timeDifference < 0)
            this.timeDifference = 0;
        else
            this.timeDifference = 0.2;
    }

    /**
     * Variable registering the time of this GameObject.
     */
    private double timeDifference;

// #####################################################################################################################

    /**
     * Returns the orientation of this GameObject.
     */
    @Basic @Raw
    public int getOrientation() {
        return this.orientation;
    }

    /**
     * Check whether the given orientation is a valid orientation for any the GameObject.
     *
     * @param orientation
     *        The orientation to check.
     * @return Returns true if the orientation is 1, 0 or -1.
     *         | result == (orientation == 1) || (orientation == 0) || (orientation == -1)
     */
    public static boolean isValidOrientation(int orientation) {
        return (orientation == 1) || (orientation == 0) || (orientation == -1);
    }

    /**
     * Set the orientation of this GameObject to the given orientation.
     *
     * @param orientation
     *        The new orientation for this GameObject.
     * @pre The given orientation must be a valid orientation for any GameObject.
     *      | isValidOrientation(orientation)
     * @post The orientation of this GameObject is equal to the given orientation.
     *       | new.getOrientation() == orientation
     */
    @Raw
    public void setOrientation(int orientation) {
        assert isValidOrientation(orientation);
        this.orientation = orientation;
    }

    /**
     * Variable registering the orientation of this GameObject.
     */
    private int orientation;

// #####################################################################################################################

    /**
     * Returns the Hit-Points of this GameObject.
     */
    @Basic @Raw
    public int getHitPoints() {
        return this.hitPoints;
    }

    /**
     * Check whether this GameObject can have the given Hit-Points as Hit-Points.
     *
     * @param hitPoints
     *        The Hit-Points to check.
     */
    public abstract boolean canHaveAsHitPoints(int hitPoints);

    /**
     * Sets the Hit-Points to the given Hitpoints.
     * @param hitPoints
     *        The new Hit-Points to be set to.
     * @post | if (canHaveAsHitPoints(hitPoints))
     *       |    then new.hitPoints = hitPoints
     */
    @Raw
    public void setHitPoints(int hitPoints) {
        if (canHaveAsHitPoints(hitPoints))
            this.hitPoints = hitPoints;
        else this.hitPoints = 0;
    }

    /**
     * Variable registering the Hit-Points of this GameObject.
     */
    private int hitPoints;

/// #####################################################################################################################

    /**
     * Returns the isDead state of this GameObject.
     */
    @Basic @Raw
    public boolean isDead() {
        return this.isDead;
    }

    /**
     * Set the isDead of this GameObject to the given isDead.
     *
     * @param  isDead
     *         The new isDead for this GameObject.
     * @post The isDead of this GameObject is equal to the given isDead.
     *       | new.isDead() == isDead
     */
    @Raw
    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    /**
     * Variable registering the isDead of this GameObject.
     */
    private boolean isDead = false;

// #####################################################################################################################

    /**
     * Check wether this GameObject is terminated.
     */
    @Basic @Raw
    public boolean isTerminated() {
        return this.isTerminated;
    }

    /**
     * Terminate this GameObject.
     *
     * @post This GameObject is terminated.
     *       | new.isTerminated()
     * @effect This GameObject is dead
     *         | setIsDead(true)
     * @effect The Set of terminated GameObjects contains this object if the World is not null.
     *         | if (getWorld() != null)
     *         |    then getWorld().hasAsTerminatedGameObject(this)
     * @effect The world will be set to null.
     *         | setWorld(null)
     * @effect The hitPoints of this GameObject are set to 0.
     *         | setHitPoints(0)
     */
    public void terminate() {
        setIsDead(true);
        setWorld(null);
        setHitPoints(0);
        this.isTerminated = true;
    }

    /**
     * Variable registering wether or not this GameObject is terminated.
     */
    private boolean isTerminated;

// #####################################################################################################################

    /**
     * Returns the world of this GameObject
     */
    @Basic
    public World getWorld() { return world; }

    /**
     * Returns if the World is a valid World.
     *
     * @param world
     *        The World to check.
     * @return Returns true when the world is not terminated.
     *         | result == !world.isTerminated()
     */
    public static boolean isValidWorld(World world) { return !world.isTerminated(); }

    /**
     * Sets the world to the given World.
     * @param world
     *        The new World.
     * @post The world world is set to the given world.
     *       | new.world = world
     */
    public void setWorld(World world) { this.world = world; }

    /**
     * Variable registering the World of this GameObject.
     */
    private World world;

// #####################################################################################################################

    /**
     * The time that needs to pass for this GameObject
     *
     * @param dt The timeDifference with which the time must be advanced.
     */
    public abstract void advanceTime(double dt);

// #####################################################################################################################
}