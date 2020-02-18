package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.facade.Facade;
import jumpingalien.util.Sprite;
import static jumpingalien.model.PositionConverter.metersToPixel;

/**
 * A class of mazubs involving an actual position, an orientation, a horizontal velocity, a vertical velocity,
 * a horizontal acceleration, a vertical acceleration, the sprites, a moving facility, a jumpingfacility and
 * a ducking facility.
 *
 * @invar The velocity of each Mazub must be a valid velocity for any Mazub.
 *        | canHaveAsVelocity(getVelocity())
 *
 * @invar The acceleration of each Mazub must be a valid acceleration for any Mazub.
 *        | canHaveAsAcceleration(getAcceleration())
 *
 * @invar The isMoving of each Mazub must be a valid isMoving for this Mazub.
 *        | canHaveAsIsMoving(isMoving())
 *
 * @invar Each Mazub can have its sprite as sprite.
 *        | isValidSprites(this.getSprites())
 *
 * @invar The isDucking of each Mazub must be a valid isDucking for this Mazub.
 *        | canHaveAsIsDucking(isDucking())
 *
 * @invar The isJumping of each Mazub must be a valid isJumping for any Mazub.
 *        | canHaveAsIsJumping(isJumping())
 *
 * @invar The Hit-Points of each Mazub must be a valid Hit-Points for any Mazub.
 *        | canHaveAsHitPoints(getHitPoints())
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public class Mazub extends GameObject {

    /**
     * Constant reflecting the lowest possible value for the horizontal velocity for this Mazub.
     */
    private final double MIN_HORIZONTAL_VELOCITY;

    /**
     * Constant reflecting the higest possible value for the horizontal velocity while movin of this Mazub.
     */
    private final double MAX_MOVING_HORIZONTAL_VELOCITY;

    /**
     * Constant reflecting the higest possible value for the horizontal velocity while ducking for this Mazub.
     */
    private final double DEFAULT_DUCKING_HORIZONTAL_VELOCITY;

    /**
     * Constant reflecting the higest possible value for the horizontal acceleration for all Mazubs.
     *
     * @return The highest possible value for the horizontal acceleration while moving for all Mazubs is 0.9.
     *         | result == 0.9
     */
    private static final double DEFAULT_HORIZONTAL_ACCELERATION = 0.9;

    /**
     * Constant reflecting the higest possible value for the vertical velocity for all Mazubs.
     *
     * @return The highest possible value for the vertical velocity while moving of all Mazubs is 8.
     *         | result == 8
     */
    private static final double MAX_VERTICAL_VELOCITY = 8;

    /**
     * Constant reflecting the higest possible value for the vertical acceleration for all Mazubs.
     *
     * @return The highest possible value for the vertical acceleration while moving for all Mazubs is -10.
     * | result == -10
     */
    private static final double DEFAULT_VERTICAL_ACCELERATION = -10;

// #####################################################################################################################

    /**
     * Initializes this new Mazub with the given pixel position and given sprites and initializes the hitPoints to 100.
     *
     * @param pixelPosition
     *        The pixelPosition for this new Mazub.
     * @param sprites
     *        The sprites for this new Mazub.
     * @post The MIN_HORIZONTAL_VELOCITY is set to 1,0.
     *       | new.MIN_HORIZONTAL_VELOCITY = 1.0
     * @post The MAX_MOVING_HORIZONTAL_VELOCITY is set to 3,0.
     *       | new.MAX_MOVING_HORIZONTAL_VELOCITY = 3.0
     * @post The DEFAULT_DUCKING_HORIZONTAL_VELOCITY is set to 1,0;
     *       | new.DEFAULT_DUCKING_HORIZONTAL_VELOCITY = 1.0
     * @effect The hitPoints of this Mazub are set to 100.
     *         | this.setHitPoints(100)
     * @post The sprites of this new Mazub are set to a copy of the given Sprites if they are valid.
     *         | new.sprites = sprites
     * @throws NullPointerException
     *         If the sprites are not valid for this Mazub.
     *         | ! isValidSprites(sprites)
     */
    @Raw
    public Mazub(int[] pixelPosition, Sprite[] sprites) throws NullPointerException {
        super(pixelPosition);
        MIN_HORIZONTAL_VELOCITY = 1.0;
        MAX_MOVING_HORIZONTAL_VELOCITY = 3.0;
        DEFAULT_DUCKING_HORIZONTAL_VELOCITY = 1.0;
        setHitPoints(100);
        if (! isValidSprites(sprites))
            throw new NullPointerException();
        this.sprites = sprites.clone();
    }

// #####################################################################################################################

    /**
     * Sets the actualPosition of this Mazub to the given actualPosition and checks if it is valid.
     *
     * @param actualPosition
     *        The new actualPosition for this Mazub.
     * @post The actualPosition of this new Mazub is equal to the given actualPosition the actualPosition is valid and when this
     *       mazub is not colliding with another mazub and is not in a tile of in an impassable terrain.
     *       | if (canHaveAsActualPosition(actualPosition) && !getWorld().containsImpassableTerrain(pixelLeftBottom, pixelRightTop))
     *       |   then new.getActualPosition() == actualPosition
     * @effect The isTerminated and the gameOver are set to true when the actualPosition is not valid.
     *         | if (!canHaveAsActualPosition(actualPosition))
     *         |    then this.terminate()
     * @throws IllegalArgumentException
     *         This mazub contains impassable terrain.
     *         | getWorld().containsImpassableTerrain(pixelLeftBottom, pixelRightTop)
     */
    @Raw @Override
    public void setActualPosition(double[] actualPosition) throws IllegalArgumentException {
        if (getWorld() != null && !isDead() && !isTerminated()) {
            if (canHaveAsActualPosition(actualPosition)) {
                int[] pixelLeftBottom = new int[]{metersToPixel(actualPosition[0])+1, metersToPixel(actualPosition[1])+1};
                int[] pixelRightTop = new int[]{metersToPixel(actualPosition[0])+getCurrentSprite().getWidth()-2, metersToPixel(actualPosition[1])+getCurrentSprite().getHeight()-2};
                if (getWorld().containsImpassableTerrain(pixelLeftBottom, pixelRightTop))
                    throw new IllegalArgumentException("Mazub contains geologicalFeature");
                super.setActualPosition(actualPosition);
            } else {
                getWorld().setGameOver(true);
                this.terminate();
            }
        } else
            super.setActualPosition(actualPosition);
    }

// #####################################################################################################################

    /**
     * Gives the maximum velocity when the given newVelocity is higher than the maximum velocity and
     * returns the minimum velocity when the given newVelocity is lower than the minimum velocity.
     *
     * @param newVelocity
     *        An invalid velocity that needs correcting.
     * @return Returns a correct Velocity when the calculated velocity is not between the maximum and minimum velocity.
     *         | if (Math.abs(newVelocity) > maxVelocity)
     *         |    then result == getOrientation() * maxvelocity
     *         |    else then result == getOrientation() * minVelocity
     */
    @Override
    public double[] correctVelocity(double[] newVelocity) {
        double newVelocityX = newVelocity[0];
        double newVelocityY = newVelocity[1];
        if (Math.abs(newVelocity[0]) < MIN_HORIZONTAL_VELOCITY)
            newVelocityX = getOrientation()*MIN_HORIZONTAL_VELOCITY;
        if (isDucking() && (Math.abs(newVelocity[0]) > DEFAULT_DUCKING_HORIZONTAL_VELOCITY))
            newVelocityX = getOrientation()*DEFAULT_DUCKING_HORIZONTAL_VELOCITY;
        if (!isDucking() && Math.abs(newVelocity[0]) > MAX_MOVING_HORIZONTAL_VELOCITY)
            newVelocityX = getOrientation()*MAX_MOVING_HORIZONTAL_VELOCITY;
        if (Math.abs(newVelocity[1]) > MAX_VERTICAL_VELOCITY)
            newVelocityY = getOrientation()* MAX_VERTICAL_VELOCITY;
        return new double[]{newVelocityX, newVelocityY};
    }

    /**
     * Check whether the given velocity is a valid velocity for this Mazub.
     *
     * @param  velocity
     *         The velocity to check.
     * @return Returns true if the velocity is between the maximum and minimum velocity.
     *         | if (isDucking() && isMoving())
     *         |    then result ==  (Math.abs(velocity[0]) <= DEFAULT_DUCKING_HORIZONTAL_VELOCITY) && (Math.abs(velocity[0]) >= MIN_HORIZONTAL_VELOCITY) && (velocity[1] <= MAX_VERTICAL_VELOCITY);
     *         | if (isMoving())
     *         |    then result ==  (Math.abs(velocity[0]) <= MAX_MOVING_HORIZONTAL_VELOCITY) && (Math.abs(velocity[0]) >= MIN_HORIZONTAL_VELOCITY) && (velocity[1] <= MAX_VERTICAL_VELOCITY);
     *         | else then result == (velocity[0] == 0) && (velocity[1] <= MAX_VERTICAL_VELOCITY);
     */
    @Override
    public boolean canHaveAsVelocity(double[] velocity) {
        if (isDucking() && isMoving())
            return (Math.abs(velocity[0]) <= DEFAULT_DUCKING_HORIZONTAL_VELOCITY) && (Math.abs(velocity[0]) >= MIN_HORIZONTAL_VELOCITY) && (velocity[1] <= MAX_VERTICAL_VELOCITY);
        if (isMoving())
            return (Math.abs(velocity[0]) <= MAX_MOVING_HORIZONTAL_VELOCITY) && (Math.abs(velocity[0]) >= MIN_HORIZONTAL_VELOCITY) && (velocity[1] <= MAX_VERTICAL_VELOCITY);
        else
            return (velocity[0] == 0) && (velocity[1] <= MAX_VERTICAL_VELOCITY);
    }

// #####################################################################################################################

    /**
     * Checks whether the given acceleration is a valid acceleration for any Mazub.
     *
     * @param acceleration
     *        The acceleration to check.
     * @return Returns true when the vertical- or horizonyalAcceleration is equal to 0 or to the DEFAULT_VERTICAL_ACCELERATION or to DEFAULT_HORIZONTAL_ACCELERATION.
     *         | result == (acceleration[0] == 0) || (acceleration[0] == DEFAULT_HORIZONTAL_ACCELERATION) || (acceleration[1] == 0) || (acceleration[1] == DEFAULT_VERTICAL_ACCELERATION)
     */
    @Override
    public boolean canHaveAsAcceleration(double[] acceleration) {
        return (acceleration[0] == 0) || (acceleration[0] == DEFAULT_HORIZONTAL_ACCELERATION)
                || (acceleration[1] == 0) || (acceleration[1] == DEFAULT_VERTICAL_ACCELERATION);
    }

// #####################################################################################################################

    /**
     * Make the given alien move to the left or the right given the alien's orientation.
     *
     * @param orientation
     *        The new orientation for this Mazub this will make the alien move to the right or the left.
     * @pre This Mazub must not be already moving.
     *      | canHaveAsIsMoving(true)
     * @post The running sprite counter is set to 0.
     *       | new.counter == 0
     * @effect The orientation is set to the given orientation.
     *         | setOrientation(orientation)
     * @effect The moving state is set to true.
     *         | setIsMoving(true)
     * @effect The horizontal velocity is set to the minimum horizontal velocity.
     *         | setVelocity(new double[]{getOrientation() * MIN_HORIZONTAL_VELOCITY, getVelocity()[1]})
     * @effect The horizontal acceleration is set to the default horizontal acceleration.
     *         | setAcceleration(new double[]{getOrientation() * DEFAULT_HORIZONTAL_ACCELERATION, getAcceleration()[1]})
     */
    public void startMove(int orientation) {
        assert (canHaveAsIsMoving(true));
        counter = 0;
        setOrientation(orientation);
        setIsMoving(true);
        setVelocity(new double[]{getOrientation() * MIN_HORIZONTAL_VELOCITY, getVelocity()[1]});
        setAcceleration(new double[]{getOrientation() * DEFAULT_HORIZONTAL_ACCELERATION, getAcceleration()[1]});
        if (getWorld() != null)
            getWorld().manageVisbleWindowPosition();
    }

    /**
     * Ends the given alien's movement.
     *
     * @pre This Mazub must be moving.
     *      | canHaveAsIsMoving(false)
     * @post Sets the running sprites timer to 0.
     *       | new.timer[1] == 0
     * @effect The moving state is set to false.
     *         | setIsMoving(false)
     * @effect The horizontal velocity is set to 0.
     *         | setVelocity(new double[]{0, getVelocity()[1]})
     * @effect The horizontal acceleration is set to 0.
     *         | setAcceleration(new double[]{0, getAcceleration()[1]})
     */
    public void endMove() {
        assert (canHaveAsIsMoving(false));
        timer[1] = 0;
        setIsMoving(false);
        setVelocity(new double[]{0, getVelocity()[1]});
        setAcceleration(new double[]{0, getAcceleration()[1]});
    }

    /**
     * Returns the isMoving of this Mazub.
     */
    @Basic @Raw
    public boolean isMoving() {
        return this.isMoving;
    }

    /**
     * Checks if this mazub can have the isMoving as isMoving.
     *
     * @param isMoving
     *        The isMoving to check.
     * @return Returns true if he wasn't already moving and if he is not dead.
     *         | result == (isMoving() != isMoving) && !isDead()
     */
    public boolean canHaveAsIsMoving(boolean isMoving) {
        return (isMoving() != isMoving) && !isDead();
    }

    /**
     * Set the isMoving of this Mazub to the given isMoving.
     *
     * @param isMoving
     *        The new isMoving for this Mazub.
     * @pre The given isMoving must be a valid isMoving for this Mazub.
     *      | canHaveAsIsMoving(isMoving)
     * @post The isMoving of this Mazub is equal to the given isMoving.
     *       | new.isMoving() == isMoving
     */
    @Raw @Model
    private void setIsMoving(boolean isMoving) {
        assert canHaveAsIsMoving(isMoving);
        this.isMoving = isMoving;
    }

    /**
     * Variable registering the isMoving of this Mazub.
     */
    private boolean isMoving;

// #####################################################################################################################

    /**
     * Make the given alien jump.
     *
     * @effect The jumping state is set to true.
     *         | setIsJumping(true)
     * @effect The vertical velocity is set to MAX_VERTICAL_VELOCITY
     *         | setVelocity(new double[]{getVelocity()[0], MAX_VERTICAL_VELOCITY})
     * @effect The vertical acceleration is set to -10.
     *         | setAcceleration(new double[]{getAcceleration()[0], -10})
     */
    public void startJump() {
        setIsJumping(true);
        setVelocity(new double[]{getVelocity()[0], MAX_VERTICAL_VELOCITY});
        setAcceleration(new double[]{getAcceleration()[0], -10});
        if (getWorld() != null)
            getWorld().manageVisbleWindowPosition();
    }

    /**
     * End the given alien's jump.
     *
     * @post The timer that measures the delay between moving sprites is set to 0.
     *       | new.timer[1] == 0
     * @effect The jumping state is set to false.
     *         | setIsJumping(false)
     * @effect The vertical velocity is set to 0 if the velocity is still positive.
     *         | if (getVelocity()[1] > 0)
     *         |    setVelocity(new double[]{getVelocity()[0], 0})
     */
    public void endJump() {
        setIsJumping(false);
        timer[1] = 0;
        if (getVelocity()[1] > 0)
            setVelocity(new double[]{getVelocity()[0], 0});
    }

    /**
     * Return the isJumping of this Mazub.
     */
    @Basic @Raw
    public boolean isJumping() {
        return this.isJumping;
    }

    /**
     * Checks if this Mazub can have the given isJumping as isJumping.
     *
     * @param isJumping
     *        The isJumping to check.
     * @return Returns true if this Mazub isn't already jumping and is not dead nor is terminated.
     *         | result == (isJumping() != isJumping) && !isDead() && !isTerminated()
     */
    private boolean canHaveAsIsJumping(boolean isJumping) {
        return (isJumping() != isJumping) && !isDead() && !isTerminated();
    }

    /**
     * Sets the isJumping of this Mazub to the given isJumping.
     *
     * @param isJumping
     *        The new isJumping for this Mazub.
     * @post The isJumping of this new Mazub is equal the the given isJumping.
     *       | new.isJumping() == isJumping
     * @throws RuntimeException
     *         This Mazub can not have the given isJumping as isJumping.
     *         | ! canHaveAsIsJumping(isJumping)
     */
    @Raw @Model
    private void setIsJumping(boolean isJumping) throws RuntimeException {
        if (!canHaveAsIsJumping(isJumping))
            throw new RuntimeException("Is already in the given Jumping state");
        this.isJumping = isJumping;
    }

    /**
     * Variable registering the isJumping of this Mazub.
     */
    private boolean isJumping;

// #####################################################################################################################

    /**
     * Make the given alien duck.
     *
     * @effect The isDucking is set to true.
     *         | setIsDucking(true)
     * @effect The horizontal velocity is set to the (+/-)DEFAULT_DUCKING_HORIZONTAL_VELOCITY depending on the orientation.
     *         | setVelocity(new double[]{getOrientation()*DEFAULT_DUCKING_HORIZONTAL_VELOCITY, getVelocity()[1]})
     * @effect The horizontal acceleration is set to 0.
     *         | setAcceleration(new double[]{0, getAcceleration()[1]})
     */
    public void startDuck() {
        setIsDucking(true);
        setVelocity(new double[]{getOrientation()*DEFAULT_DUCKING_HORIZONTAL_VELOCITY, getVelocity()[1]});
        setAcceleration(new double[]{0, getAcceleration()[1]});
    }

    /**
     * End the given alien's ducking.
     *
     * @post The timer that measures the delay between moving sprites is set to 0.
     *       | new.timer[1] == 0
     * @post The running sprites counter is set to 0.
     *       | new.counter == 0
     * @effect The ducking state is set to false.
     *         | setIsDucking(false)
     * @effect The horizontal acceleration is set to the DEFAULT_HORIZONTAL_ACCELERATION in the right direction if Mazub is moving.
     *         | setAcceleration(new double[]{DEFAULT_HORIZONTAL_ACCELERATION*getOrientation(), getAcceleration()[1]})
     */
    public void endDuck() {
        setIsDucking(false);
        if (!isImpossibleEndDuck()) {
            timer[1] = 0;
            counter = 0;
        }
        if (isMoving())
            setAcceleration(new double[]{DEFAULT_HORIZONTAL_ACCELERATION*getOrientation(), getAcceleration()[1]});
    }

    /**
     * Return the isDucking of this Mazub.
     */
    @Basic @Raw
    public boolean isDucking() {
        return this.isDucking;
    }

    /**
     * Checks if the given Mazub can have the given isDucking as isDucking.
     *
     * @param isDucking
     *        The isDucking to check.
     * @return returns true if he wasn't already in the given ducking state
     *         | result == (isDucking() != isDucking)
     */
    public boolean canHaveAsIsDucking(boolean isDucking) {
        return isDucking() != isDucking;
    }

    /**
     * Sets the isDucking of this Mazub to the given isDucking when it does not overlap with an impassable terrain.
     *
     * @param isDucking
     *        The new isDucking for this Mazub.
     * @post If the given isDucking is a valid isDucking for this Mazub,
     *       the isDucking of this new Mazub is equal to the given isDucking.
     *       | if (canHaveAsIsDucking(isDucking) && !(getWorld() == null || getWorld().containsImpassableTerrain(this, 1)))
     *       |   then new.isDucking() == isDucking && !new.isImpossibleEndDuck()
     *       |   else then new.getIsducking() == true && new.isImpossibleEndDuck()
     */
    @Raw @Model
    private void setIsDucking(boolean isDucking) {
        if (canHaveAsIsDucking(isDucking)) {
            this.isDucking = isDucking;
            if (!isDucking && (getWorld() == null || getWorld().containsImpassableTerrain(this, 1))) {
                this.isDucking = true;
                setImpossibleEndDuck(true);
            } else
                setImpossibleEndDuck(false);
        }
    }

    /**
     * Variable registering the isDucking of this Mazub.
     */
    private boolean isDucking;

    /**
     * Return the impossibleEndDuck of this mazub.
     */
    @Basic @Raw
    public boolean isImpossibleEndDuck() {
        return this.impossibleEndDuck;
    }

    /**
     * Set the impossibleEndDuck of this mazub to the given impossibleEndDuck.
     *
     * @param impossibleEndDuck
     *        The new impossibleEndDuck for this mazub.
     * @post If the given impossibleEndDuck is a valid impossibleEndDuck for any mazub,
     *       the impossibleEndDuck of this new mazub is equal to the given impossibleEndDuck.
     *       | if (canHaveAsImpossibleEndDuck(impossibleEndDuck))
     *       |   then new.isImpossibleEndDuck() == impossibleEndDuck
     */
    @Raw
    private void setImpossibleEndDuck(boolean impossibleEndDuck) {
        this.impossibleEndDuck = impossibleEndDuck;
    }

    /**
     * Variable registering the impossibleEndDuck of this mazub.
     */
    private boolean impossibleEndDuck;

// #####################################################################################################################

    /**
     * Index for the running sprites.
     */
    private int counter = 0;

    @Override
    public Sprite getCurrentSprite() {
        if (Facade.isExecutingTest())
           return (isDucking()) ? getSprites()[1] : getSprites()[0];
        if (!isMoving()) {
            timer[1] = 0;
            if (isDucking()) {
                if ((timer[0] >= 1) || (getOrientation() == 0)) {
                    setOrientation(0);
                    return getSprites()[1];
                } return (getOrientation() > 0) ? getSprites()[6] : getSprites()[7];
            } else {
                if ((timer[0] >= 1) || (getOrientation() == 0)) {
                    setOrientation(0);
                    return getSprites()[0];
                } return (getOrientation() > 0) ? getSprites()[2] : getSprites()[3];}
        } else {
            timer[0] = 0;
            if (isDucking()) {
                timer[1] = 0;
                return (getOrientation() > 0) ? getSprites()[6] : getSprites()[7]; }
            if (isJumping()) {
                timer[1] = 0;
                return (getOrientation() > 0) ? getSprites()[4] : getSprites()[5]; }
            else return runningAnnimation(); }
    }

    private Sprite runningAnnimation() {
        if ((getOrientation() > 0) && !isDucking()) {
            while (timer[1] >= 0.075) {
                timer[1] = 0;
                counter += 1;
                if (counter > (getSprites().length - 8) / 2 - 1)
                    counter = 0;
            }
            return getSprites()[8 + counter];
        } else {
            while (timer[1] >= 0.075) {
                timer[1] = 0;
                counter += 1;
                if (counter > (getSprites().length - 8) / 2 - 1)
                    counter = 0;
            }
            return getSprites()[8 + (getSprites().length - 8) / 2 + counter];
        }
    }

    /**
     * Returns the sprites of this Mazub.
     */
    @Basic @Raw @Immutable
    public Sprite[] getSprites() { return this.sprites; }

    /**
     * Check whether this Mazub can have the given sprite as its sprite.
     *
     * @param sprites
     *        The sprites to check.
     * @return Returns true when the length of the array sprites is an even number and contains more than 10
     *         elements. The array may also not be or contain null.
     *         | if (sprites != null)
     *         |    for each sprite in sprites:
     *         |       if (sprite == null)
     *         |           then result == false
     *         |    then result == (sprites.length >= 10) && (sprites.length % 2 == 0)
     *         | else then result == false
     */
    @Raw
    private static boolean isValidSprites(Sprite[] sprites) {
        if (sprites != null) {
            for (Sprite sprite : sprites)
                if (sprite == null)
                    return false;
            return (sprites.length >= 10) && (sprites.length % 2 == 0);
        } else
            return false;
    }

    /**
     * Variable registering the sprite of this Mazub.
     */
    private final Sprite[] sprites;

// #####################################################################################################################

    /**
     * Check whether the given Hit-Points are a valid amount of Hit-Points for any Mazub.
     *
     * @param hitPoints
     *        The Hit-Points to check.
     * @return Returns true if the hitPoints are between 0 and 500.
     *         | result == hitPoints > 0 && hitPoints < 500
     */
    @Override
    public boolean canHaveAsHitPoints(int hitPoints) {
        return hitPoints >= 0 && hitPoints <= 500;
    }

// #####################################################################################################################

    /**
     * An array timer who counts the time passed this is used for the sprites. The third index is
     * used to loose hitPoints, the fourth is used to remove hitPoints
     */
    private double[] timer = {0, 0, 0, 0};

    public double freeze;

    @Override
    public void advanceTime(double dt) {
        if (dt < 0)
            dt = 0;
        if (dt > 0.2)
            dt = 0.2;
        if (getWorld() != null) {
        	if (!isTerminated())
        	    setAcceleration(manageNewAcceleration());
        	getWorld().manageCollisionTargetTile(this);
            while (dt > 0) {
            	setTimeDifference( (0.01)/(Math.sqrt(Math.pow(getVelocity()[0], 2) + Math.pow(getVelocity()[1], 2)) + Math.sqrt(Math.pow(getAcceleration()[0], 2) + Math.pow(getAcceleration()[1], 2))*dt));
                if (Double.isInfinite(getTimeDifference()) || getTimeDifference() > dt)
                    setTimeDifference(dt);
                manageMovement();
                if (!isTerminated() && getWorld() != null) {
                    for (GameObject gameObject : getWorld().getGameObjectSet()) {
                        if (gameObject instanceof Plant)
                            getWorld().managePlantCollision((Plant) gameObject, getTimeDifference());
                        if (gameObject instanceof Slime && freeze <= 0)
                            getWorld().manageSlimeCollision((Slime) gameObject);
                    }
                    getWorld().manageGeoFeaturesCollision(getTimeDifference());
                }
                manageDeath(getTimeDifference());
                timer = new double[]{timer[0] + getTimeDifference(), timer[1] + getTimeDifference(), timer[2], timer[3]};
                freeze -= getTimeDifference();
                dt -= getTimeDifference();
            }
        } else {
            setTimeDifference(dt);
            setActualPosition(calculateNewActualPosition());
            setVelocity(calculateNewVelocity());
            freeze -= getTimeDifference();
            timer = new double[]{timer[0]+=dt, timer[1]+=dt, timer[2], timer[3]};
        }
    }
// #####################################################################################################################

    /**
     * Manages the death of this Mazub.
     *
     * @param dt
     *        The time that has passed between the last method call and the time now.
     * @effect This mazub is dead.
     *         | this.setIsDead(true)
     * @post If this mazub is dead this mazub will stop moving.
     *       | new.isMoving() == true
     * @post If this mazub is dead this mazub will stop jumping.
     *       | new.isJumping() == true
     * @post The timer[0] is incremented with the given dt.
     *       | new.timer[0] == this.timer[0]+dt
     * @post If mazub is in a world and is dead for more than 0.6 seconds the gameOver is set to true.
     *       | if (getWorld() != null && timer[3] >= 0.6 && isDead())
     *       |    then getWorld().setGameOver(true)
     * @post If mazub is dead for more than 0.6 seconds this mazub is terminated.
     *       | if (timer[3] >= 0.6 && isDead())
     *       |    then this.terminate()
     */
    private void manageDeath(double dt) {
        if (getHitPoints() == 0 || isDead()) {
            timer[3]+=dt;
            setIsDead(true);
            if (canHaveAsIsMoving(false))
                endMove();
            if (canHaveAsIsJumping(false))
                endJump();
            if (timer[3] >= 0.6){
                if (getWorld() != null)
                    getWorld().setGameOver(true);
                this.terminate();
            }
        } else timer[3] = 0;
    }

// #####################################################################################################################

    /**
     * Manages the movement of this mazub.
     *
     * @effect Sets the new actualPosition when mazub is not terminated.
     *         | if (! isTerminated())
     *         |    then setActualPosition(manageNewPosition())
     * @effect Sets the new velocity when mazub is not terminated.
     *         | if (! isTerminated())
     *         |    then setVelocity(manageNewVelocity())
     * @effect Sets the new acceleration when mazub is not terminated.
     *         | if (! isTerminated())
     *         |    then setAcceleration(manageNewAcceleration())
     */
    private void manageMovement() {
        if (!isTerminated()) {
            setActualPosition(manageNewPosition());
            if (!isTerminated()) {
                setVelocity(manageNewVelocity());
                setAcceleration(manageNewAcceleration());
            }
        }
    }

    /**
     * Manages the new position of this Mazub.
     *
     * @effect If this mazub collides at the left or right side with an impassable terrain or another mazub
     *         and if he can stop moving, then this mazub will stop moving.
     *         | if (getWorld().checkHorizontalCollision(this, newPos) && canHaveAsIsMoving(false))
     *         |    then endMove()
     * @effect If this mazub collides at he top or bottom with an impassable terrain or another mazub
     *         and if he can stop jumping, then this mazub will stop jumping.
     *         | if (getWorld().checkVerticalCollision(this, newPos) && canHaveAsIsJumping(false))
     *         |    then endJump()
     * @return Return the new position for this mazub
     *         | result == new double[]{(getWorld().checkHorizontalCollision(this, new double[] {calculateNewActualPosition()[0], calculateNewActualPosition()[1]}) ? getActualPosition()[0] : calculateNewActualPosition()[0],
     *         |            (getWorld().checkVerticalCollision(this, new double[] {calculateNewActualPosition()[0], calculateNewActualPosition()[1]})) ? getActualPosition()[1] : calculateNewActualPosition()[0]}
     */
    private double[] manageNewPosition() {
        double[] newPos = calculateNewActualPosition();
        if (getWorld().checkHorizontalCollision(this, newPos)) {
            newPos[0] = getActualPosition()[0];
            if (canHaveAsIsMoving(false))
                endMove();
        }
        if (getWorld().checkVerticalCollision(this, newPos)) {
            newPos[1] = getActualPosition()[1];
            if (canHaveAsIsJumping(false))
                endJump();
        }
        return newPos;
    }

    /**
     * Manages the new velocity for this Mazub.
     *
     * @return Return the newly calculated velocity for this mazub if he doesn't collides with
     *         an impassable terrain or another mazub else the velocity will be set to 0.
     *         | result == new double[]{(getWorld().checkHorizontalCollision(this, getActualPosition())) ? 0 : calculateNewVelocity()[0],
     *         |            (getWorld().checkVerticalCollision(this, getActualPosition())) ? 0 : calculateNewVelocity()[1]}
     */
    private double[] manageNewVelocity() {
        return new double[]{(getWorld().checkHorizontalCollision(this, getActualPosition())) ? 0 : calculateNewVelocity()[0],
                (getWorld().checkVerticalCollision(this, getActualPosition())) ? 0 : calculateNewVelocity()[1]};
    }

    /**
     * Manages the new acceleration for this Mazub.
     *
     * @return Return the new acceleration fot thi mazub.
     *         | result ==  new double[]{(getWorld().checkHorizontalCollision(this, getActualPosition())) ? 0 : getAcceleration()[0],
     *         |             (getWorld().checkVerticalCollision(this, getActualPosition())) ? 0 : DEFAULT_VERTICAL_ACCELERATION}
     */
    private double[] manageNewAcceleration() {
        return new double[]{(getWorld().checkHorizontalCollision(this, getActualPosition())) ? 0 : getAcceleration()[0],
                (getWorld().checkVerticalCollision(this, getActualPosition())) ? 0 : DEFAULT_VERTICAL_ACCELERATION};
    }
}