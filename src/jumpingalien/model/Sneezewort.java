package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;

/**
 * A class of SneezeWort involving a timer
 *
 * @invar | canHaveAsVelocity(getVelocity())
 * @invar | canHaveAsHitPoints(getHitPoints())
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public class Sneezewort extends Plant{

    /**
     * The default horizontal velocity for this Sneezewort.
     *
     * @return | result == 0.5
     */
    private double DEFAULT_HORIZONTAL_VELOCITY = 0.5;

    /**
     * The maximum time (in seconds) the plant can survive without being eaten.
     */
    private final double LIFE_TIME;

    /**
     * The time before this Sneezewort changes direction, the time is measured in seconds.
     *
     * @return | result == 0.5
     */
    private double TIME_SHIFT_DIRECTION = 0.5;

    /**
     * Initializes this new Sneezewort with the given pixelPosition and sprites.
     *
     * @param pixelPosition
     *        The pixelPosition for this new Sneezewort.
     * @param sprites
     *        The sprites for this new Sneezewort.
     * @post | new.LIFE_TIME = 10
     * @post | new.getSprites = sprites
     * @effect | this.setHitPoints(1)
     * @effect | this.startHovering()
     * @throws IllegalArgumentException
     *         | ! isValidSprites(sprites)
     */
    @Raw
    public Sneezewort(int[] pixelPosition, Sprite[] sprites) throws IllegalArgumentException {
        super(pixelPosition, sprites);
        LIFE_TIME = 10;
        setHitPoints(1);
        startHovering();
    }

// #####################################################################################################################

    /**
     * Checks wether the given velocity is a valid velocity for this Sneezewort.
     *
     * @param velocity
     *        The given velocity to check.
     * @return | result == (Math.abs(velocity[0]) == DEFAULT_HORIZONTAL_VELOCITY) && (velocity[1] == 0)
     */
    @Override
    public boolean canHaveAsVelocity(double[] velocity) {
        return (Math.abs(velocity[0]) == DEFAULT_HORIZONTAL_VELOCITY) && (velocity[1] == 0);
    }

    /**
     * Check whether the given Hit-Points are valid Hit-Points for any Sneezewort.
     *
     * @param hitPoints
     *        The Hit-Points to check.
     * @return | result == (hitPoints == 0) || (hitpoints == 1)
     */
    @Override
    public boolean canHaveAsHitPoints(int hitPoints) {
        return hitPoints == 0 || hitPoints == 1;
    }

    /**
     * Make the Sneezewort hover.
     *
     * @effect | super.setOrientation(-1)
     * @effect | super.setVelocity(new double[]{DEFAULT_HORIZONTAL_VELOCITY*getOrientation(),0})
     * @effect | super.setAcceleration(new double[]{0,0})
     */
    public void startHovering() {
        setOrientation(-1);
        setVelocity(new double[]{DEFAULT_HORIZONTAL_VELOCITY*getOrientation(),0});
        setAcceleration(new double[]{0,0});
    }

    /**
     * An array timer who counts the time passed this is used for the sprites.
     */
    private double[] timer = new double[]{0, 0, 0};

// #####################################################################################################################

    /**
     * Advances the state of the Sneezewort by the given time period and updates the timer, the actualPositon, the velocity,
     * the isDead & isTerminated state, the orientztion, the timeDifference and the hitPoints.
     *
     * @param time
     *        Gives the time that had passed since the last advanceTime was called.
     * @post | new.timer[1] == timer[1] + dt
     * @post | while (dt > 0)
     *       |    new.timer[0] == timer[0]+pixelTime
     * @effect | if (pixelTime > dt)
     *         |    then setTimeDifference(dt)
     *         |    else then setTimeDifference(pixelTime)
     * @effect | if (timer[1] >= LIFE_TIME || isDead())
     *         |    then setIsDead(true)
     * @effect | if (timer[1] >= LIFE_TIME || isDead())
     *         |    then setHitPoints(0)
     * @post | while (dt > 0)
     *       |    if (timer[1] >= LIFE_TIME || isDead())
     *       |       then new.timer[2] = old.timer[2] + pixelTime
     * @effect | if (timer[2] >= 0.6 && (timer[1] >= LIFE_TIME || isDead()))
     *         |    then this.terminate()
     * @effect | if (!isTerminated() && timer[0] > TIME_SHIFT_DIRECTION)
     *         |    then setOrientation(-getOrientation())
     * @effect | if (!isTerminated() && timer[0] > TIME_SHIFT_DIRECTION)
     *         |    then setVelocity(new double[]{getOrientation()*DEFAULT_HORIZONTAL_VELOCITY, 0})
     * @effect | if (!isTerminated() && canHaveAsActualPosition(new double[]{calculateNewActualPosition()[0], getActualPosition()[1]}))
     *         |    then setActualPosition(new double[]{calculateNewActualPosition()[0], getActualPosition()[1]})
     *         |    else then this.terminate()
     * @effect | if (!isTerminated() && getWorld() != null && getWorld().getMazub() != null)
     *         |    then getWorld().managePlantCollision(getWorld().getMazub());
     */
    @Override
    public void advanceTime(double time) {
        float dt = (float) time;
        timer[1]+=dt;
        while (dt > 0) {
            setTimeDifference((0.01)/DEFAULT_HORIZONTAL_VELOCITY);
            if (getTimeDifference() > dt)
                setTimeDifference(dt);
            if (timer[1] >= LIFE_TIME || isDead()) {
                timer[2] += getTimeDifference();
                setIsDead(true);
                setHitPoints(0);
                if (timer[2] >= 0.6)
                    this.terminate();
            }
            timer[0]+=getTimeDifference();
            if (!isTerminated()) {
                if (timer[0] > TIME_SHIFT_DIRECTION) {
                    setOrientation(-getOrientation());
                    setVelocity(new double[]{getOrientation() * DEFAULT_HORIZONTAL_VELOCITY, 0});
                    timer[0] -= TIME_SHIFT_DIRECTION;
                }

                double[] newPosition = new double[]{calculateNewActualPosition()[0], getActualPosition()[1]};
                if (canHaveAsActualPosition(newPosition)) {
                    setActualPosition(newPosition);
                } else this.terminate();

                if (getWorld() != null)
                    getWorld().managePlantCollision(this, getTimeDifference());
            } else break;
            dt-=getTimeDifference();
        }
    }
}
