package jumpingalien.model;

import jumpingalien.util.Sprite;

/**
 *
 * @invar | canHaveAsVelocity(getVelocity())
 * @invar | canHaveAsHitPoints(getHitPoints())
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public class Skullcab extends Plant {

    /**
     * The the amount of time that has to elapse before this Skullcab can lose hitpoints again.
     *
     * @return | result == 0.6
     */
    public double DELAY_LOSING_HITPOINTS = 0.6;
    /**
     * The default vertical velocity for this Sneezewort.
     *
     * @return | result == 0.5
     */
    private double DEFAULT_VERTICAL_VELOCITY = 0.5;
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
     * Initializes this new Skullcab with the given pixelPosition, sprites, LIFE_TIME
     * and hitPoints and it also makes the plant start hover.
     *
     * @param pixelPosition
     *        The pixelPosition for this new Skullcab.
     * @param sprites
     *        The sprites for this Skullcab.
     * @post | new.LIFE_TIME = 12
     * @post | new.getSprites = sprites
     * @effect | this.setHitPoints(3)
     * @effect | this.startHovering()²
     */
    public Skullcab(int[] pixelPosition, Sprite[] sprites) {
        super(pixelPosition, sprites);
        LIFE_TIME = 12;
        setHitPoints(3);
        startHovering();
    }

// #####################################################################################################################

    /**
     * Checks wether the given velocity is a valid velocity for this Sneezewort.
     *
     * @param velocity
     *        The given velocity to check.
     * @return | result == (Math.abs(velocity[1]) == DEFAULT_VERTICAL_VELOCITY) && (velocity[0] == 0)
     */
    @Override
    public boolean canHaveAsVelocity(double[] velocity) {
        return (Math.abs(velocity[1]) == DEFAULT_VERTICAL_VELOCITY) && (velocity[0] == 0);
    }

    /**
     * Check whether the given Hit-Points are valid Hit-Points for this skullcab.
     *
     * @param hitPoints
     *        The Hit-Points to check.
     * @return | result == (hitPoints >= 0) && (hitpoints <= 3)
     */
    @Override
    public boolean canHaveAsHitPoints(int hitPoints) {
        return hitPoints >= 0 && hitPoints <= 3;
    }

    /**
     * Make the Skullcab hover.
     *
     * @effect | super.setOrientation(1)
     * @effect | super.setVelocity(new double[]{DEFAULT_VERTICAL_VELOCITY*getOrientation(),0})
     * @effect | super.setAcceleration(new double[]{0,0})
     */
    public void startHovering() {
        super.setOrientation(1);
        super.setVelocity(new double[]{0, DEFAULT_VERTICAL_VELOCITY*getOrientation()});
        super.setAcceleration(new double[]{0,0});
    }

    /**
     * An array timer who counts the time passed this is used for the sprites. 3de voor uninterupted time
     */
    public double[] timer = new double[]{0, 0, 0, 0};

// #####################################################################################################################

    /**
     * Advances the state of the Sneezewort by the given time period and updates the timer, the actualPositon, the velocity,
     * the isDead & isTerminated state, the orientztion, the timeDifference and the hitPoints.
     * @param time
     *        Gives the time that had passed since the last advanceTime was called.
     * @post | new.timer[1] == old.timer[1] + dt
     * @post | if (getTimeDifference() > dt)
     *       |    then setTimeDifference(dt)
     *       |    else then new.getTimeDifference() == (0.01)/DEFAULT_VERTICAL_VELOCITY)
     * @post | if (timer[1] >= LIFE_TIME || isDead())
     *       |    then new.timer[2] == old.timer[2] + getTimeDifference()
     * @effect | if (timer[1] >= LIFE_TIME || isDead())
     *         |    then setIsDead(true)
     * @effect | if (timer[1] >= LIFE_TIME || isDead())
     *         |    then setHitPoints(0)
     * @effect | if ((timer[1] >= LIFE_TIME || isDead()) && timer[2] >= 0.6)
     *         |    then this.terminate()
     * @post | new.timer[0] == old.timer[0] + dt
     * @effect | if (!isTerminated() && timer[0] > TIME_SHIFT_DIRECTION)
     *         |    then setOrientation(-getOrientation())
     * @effect | if (!isTerminated() && timer[0] > TIME_SHIFT_DIRECTION)
     *         |    then setVelocity(new double[]{0, getOrientation() * DEFAULT_VERTICAL_VELOCITY})
     * @post  | if (!isTerminated() && timer[0] > TIME_SHIFT_DIRECTION)
     *        |    then new.timer[0] == old.timer[0] - TIME_SHIFT_DIRECTION
     * @effect | if (!isTerminated && canHaveAsActualPosition(calculateNewActualPosition()))
     *         |    then setActualPosition(newPosition)
     * @effect | if (!isTerminated && !canHaveAsActualPosition(calculateNewActualPosition()))
     *         |    then this.terminate()
     * @effect | if (!isTerminated && getWorld() != null)
     *         |    then getWorld().managePlantCollision(this, getTimeDifference())
     */
    @Override
    public void advanceTime(double time) {
        float dt = (float) time;
        timer[1] += dt;
        while (dt > 0) {
            setTimeDifference((0.01)/DEFAULT_VERTICAL_VELOCITY);
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
                    setVelocity(new double[]{0, getOrientation() * DEFAULT_VERTICAL_VELOCITY});
                    timer[0] -= TIME_SHIFT_DIRECTION;
                }

                double[] newPosition = calculateNewActualPosition();
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
