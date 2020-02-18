package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;
import static jumpingalien.model.PositionConverter.metersToPixel;

/**
 * A Slime class containing a school, sprites and an id.
 *
 * @invar | isValidSchool(getSchool())
 * @invar | isValidSprites(getSprites())
 * @invar | canHaveAsVelocity(getVelocity())
 * @invar | canHaveAsAcceleration(getAcceleration())
 * @invar | canHaveAsHitPoints(getHitPoints())
 * @invar | isValidId(getId())
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public class Slime extends GameObject {

    /**
     * The default vertical acceleration for this Slime.
     */
    private double DEFAULT_HORIZONTAL_ACCELERATION;

    /**
     * The maximum horizontal velocity for this Slime.
     */
    private double MAX_HORIZONTAL_VELOCITY;

    /**
     * Initialize this new Slime with given School, sprites, hitpoints and start making the slime move.
     *
     * @param id
     *        The id for this new Slime.
     * @param pixelPosition
     *        The pixelPosition for this new Slime.
     * @param school
     *        The School for this new Slime.
     * @param sprites
     *        The sprites for this new Slime.
     * @post | DEFAULT_HORIZONTAL_ACCELERATION = 0.7
     * @post | MAX_HORIZONTAL_VELOCITY = 2.5
     * @effect | super(pixelPosition)
     * @effect | this.setId(id)
     * @effect | SlimeInfo.allIds.put(this, id)
     * @effect | SlimeInfo.addSlime(this)
     * @effect | this.setSchool(school)
     * @effect | if (getSchool() != null)
     *         |    then getSchool().addSlime(this)
     * @effect | if (getSchool() != null && getWorld() != null && !getWorld().hasAsSchool(getSchool()))
     *         |    then getWorld().addSchool(getSchool())
     * @post | new.sprites = sprites
     * @effect | this.setHitPoints(100)
     * @effect | this.startMoving()
     * @throws IllegalArgumentException
     *         | ! isValidSprites(sprites) || !isValidId(id)
     */
    public Slime(long id, int[] pixelPosition, School school, Sprite[] sprites) throws IllegalArgumentException {
        super(pixelPosition);
        DEFAULT_HORIZONTAL_ACCELERATION = 0.7;
        MAX_HORIZONTAL_VELOCITY = 2.5;
        if (!isValidSprites(sprites) || !isValidId(id))
            throw new IllegalArgumentException("Not a valid Slime");
        this.setId(id);
        SlimeInfo.allIds.put(this, id);
        SlimeInfo.addSlime(this);
        setSchool(school);
        if (getSchool()!=null) {
            getSchool().addSlime(this);
            if (getWorld() != null && !getWorld().hasAsSchool(getSchool()))
                getWorld().addSchool(getSchool());
        }
        this.sprites = sprites;
        setHitPoints(100);
        startMoving();
    }

// #####################################################################################################################

    /**
     * Return the School of this Slime.
     */
    @Basic @Raw
    public School getSchool() {
        return this.school;
    }

    /**
     * Check whether the given School is a valid School for any Slime.
     *
     * @param school
     *        The School to check.
     * @return | result == (school == null) || (!school.isTerminated())
     */
    public static boolean isValidSchool(School school) {
        return (school == null) || (!school.isTerminated());
    }

    /**
     * Set the School of this Slime to the given School.
     *
     * @param  school
     *         The new School for this Slime.
     * @post | new.getSchool() == school
     * @throws IllegalArgumentException
     *         | ! isValidSchool(school)
     */
    @Raw
    public void setSchool(School school) throws IllegalArgumentException {
        if (! isValidSchool(school))
            throw new IllegalArgumentException();
        this.school = school;
    }

    /**
     * Switches the school
     *
     * @param newSchool
     *        The school to switch to.
     * @return | result == this.getSchool() != null && newSchool != null && !this.isTerminated() && !newSchool.isTerminated()
     */
    public boolean canSwitchSchool(School newSchool) {
        return this.getSchool() != null && newSchool != null && !this.isTerminated() && !newSchool.isTerminated();
    }

    /**
     * Switch the this school to a new school.
     *
     * @param newSchool
     *        The new school of his Slime.
     * @post | ! old.getSchool().hasAsSlime(this)
     * @post | newSchool.hasAsSlime(this)
     * @effect | setSchool(newSchool)
     * @effect | for each slime in oldSchool:
     *         |    slime.setHitPoints(slime.getHitPoints() + 1) && this.setHitPoints(getHitPoints() - 1)
     * @effect | for each slime in newSchool:
     *         |    slime.setHitPoints(slime.getHitPoints() - 1) && this.setHitPoints(getHitPoints() + 1)
     * @throws IllegalArgumentException
     *         | ! canSwitchSchool(newSchool)
     */
    public void switchSchool(School newSchool) throws IllegalArgumentException {
        if (! canSwitchSchool(newSchool))
            throw new IllegalArgumentException("Can't swith school");
        for (Slime slime : getSchool().getSlimes()) {
            slime.setHitPoints(slime.getHitPoints() + 1);
            this.setHitPoints(this.getHitPoints() - 1);
        }
        getSchool().removeSlime(this);
        setSchool(newSchool);
        getSchool().addSlime(this);
        for (Slime slime : newSchool.getSlimes()) {
            slime.setHitPoints(slime.getHitPoints() - 1);
            this.setHitPoints(this.getHitPoints() + 1);
        }
    }

    /**
     * Variable registering the School of this Slime.
     */
    private School school;

// #####################################################################################################################

    /**
     * Terminate this slime.
     *
     * @effect | super.terminate()
     * @effect | if (getSchool() != null)
     *         |    then getSchool().removeSlime(this)
     * @effect | if (SlimeInfo.hasAsSlime(this))
     *         |    then SlimeInfo.removeSlime(this)
     */
    @Override
    public void terminate() {
        super.terminate();
        if (getSchool() != null)
            getSchool().removeSlime(this);
        if (SlimeInfo.hasAsSlime(this))
            SlimeInfo.removeSlime(this);
    }

// #####################################################################################################################

    /**
     * Return the sprite to be displayed.
     *
     * @return | result == (getOrientation() == 1) ? getSprites()[0] : getSprites()[1]
     */
    @Override
    public Sprite getCurrentSprite() {
        return (getOrientation() == 1) ? getSprites()[0] : getSprites()[1];
    }

    /**
     * Returns the sprites of this Slime.
     */
    @Override @Immutable @Raw
    public Sprite[] getSprites() {
        return this.sprites;
    }

    /**
     * Check whether this sprite is a valid sprite for any Slime.
     *
     * @param sprites
     *        The given sprites to check
     * @return | result == (sprites != null) && (sprites.length == 2) && (sprites[0] != null) && (sprites[1] != null)
     */
    public static boolean isValidSprites(Sprite[] sprites) {
        return (sprites != null) && (sprites.length == 2) && (sprites[0] != null) && (sprites[1] != null);
    }

    /**
     * Variable registering the sprite of this Mazub.
     */
    private final Sprite[] sprites;

// #####################################################################################################################

    /**
     * Check wether this velocity is a valid velocity for any slime.
     *
     * @param velocity
     *        The velocity to check.
     * @return | Math.abs(velocity[0]) <= MAX_HORIZONTAL_VELOCITY && Math.abs(velocity[0]) >= 0 && velocity[1] == 0
     */
    @Override
    public boolean canHaveAsVelocity(double[] velocity) {
        return Math.abs(velocity[0]) <= MAX_HORIZONTAL_VELOCITY && Math.abs(velocity[0]) >= 0 && velocity[1] == 0;
    }

    /**
     * Check wether this Acceleration is a valid acceleration for any slime.
     *
     * @param acceleration
     *        The acceleration to check.
     * @return | (acceleration[0] == 0 || Math.abs(acceleration[0]) == DEFAULT_HORIZONTAL_ACCELERATION)
     */
    @Override
    public boolean canHaveAsAcceleration(double[] acceleration) {
        return (acceleration[0] == 0 || Math.abs(acceleration[0]) == DEFAULT_HORIZONTAL_ACCELERATION);
    }

    /**
     * Check whether the given Hit-Points are a valid amount of Hit-Points for any Mazub.
     *
     * @param hitPoints
     *        The Hit-Points to check.
     * @return | result == (hitPoints >= 0)
     */
    @Override
    public boolean canHaveAsHitPoints(int hitPoints) {
        return (hitPoints >= 0);
    }

// #####################################################################################################################

   /**
    * Return the id of this Slime.
    */
   @Basic @Raw
   public Long getId() {
       return this.id;
   }

   /**
    * Check whether the given id is a valid id for any Slime.
    *
    * @param  id
    *         The id to check.
    * @return | result == (id >= 0) && !SlimeInfo.allIds.containsValue(id)
    */
   public static boolean isValidId(Long id) {
       return (id >= 0) && !SlimeInfo.allIds.containsValue(id);
   }

   /**
    * Set the id of this Slime to the given id.
    *
    * @param  id
    *         The new id for this Slime.
    * @post | new.getId() == id
    * @throws IllegalArgumentException
    *         | ! isValidId(id)
    */
   @Raw
   public void setId(Long id) throws IllegalArgumentException {
       if (! isValidId(id))
           throw new IllegalArgumentException();
       this.id = id;
   }

   /**
    * Variable registering the id of this Slime.
    */
   private Long id;

// #####################################################################################################################

    /**
     * Manages the movement of this slime.
     *
     * @effect | if (!isTerminated() && canHaveAsActualPosition(manageNewPosition()))
     *         |    then setActualPosition(manageNewPosition())
     *         |    else then terminate()
     * @effect | if (!isTerminated())
     *         |    then setVelocity(manageNewVelocity())
     * @effect | if (!isTerminated() && canHaveAsAcceleration(manageNewAcceleration()))
     *         |    then setAcceleration(manageNewAcceleration())
     */
    private void manageMovement() {
        if (!isTerminated()) {
            if (canHaveAsActualPosition(manageNewPosition()))
                setActualPosition(manageNewPosition());
            else terminate();
            if (!isTerminated()) {
                setVelocity(manageNewVelocity());
                if (canHaveAsAcceleration(manageNewAcceleration())) setAcceleration(manageNewAcceleration());
            }
        }
    }

    /**
     * Manages the new position of this Slime.
     *
     * @effect | if (getWorld() != null && getWorld().horizontalSlimeCollision(this))
     *         |   then endMoving()
     * @effect | if (getWorld() != null && getWorld().isOverlappingOtherSlime(this, new int[]{metersToPixel(newPos[0]), getPixelPosition()[1]}))
     *         |   then switchDirection()
     * @return | result == calculateNewActualPosition()
     */
    private double[] manageNewPosition() {
        if (getWorld() != null && getWorld().horizontalSlimeCollision(this))
            endMoving();
        if (getWorld() != null && getWorld().isOverlappingOtherSlime(this, new int[]{metersToPixel(calculateNewActualPosition()[0]), getPixelPosition()[1]}))
            switchDirection();
        return calculateNewActualPosition();
    }

    /**
     * Manages the new velocity for this Mazub.
     *
     * @return | result == new double[]{getOrientation()*Math.abs(getVelocity()[0] + getAcceleration()[0] * getTimeDifference()), 0}
     */
    private double[] manageNewVelocity() {
        return new double[]{getOrientation()*Math.abs(getVelocity()[0] + getAcceleration()[0] * getTimeDifference()), 0};
    }

    /**
     * Manages the new acceleration for this Mazub.
     *
     * @return | result == new double[]{getOrientation()*DEFAULT_HORIZONTAL_ACCELERATION, 0}
     */
    private double[] manageNewAcceleration() {
        return new double[]{getOrientation()*DEFAULT_HORIZONTAL_ACCELERATION, 0};
    }

// #####################################################################################################################

    /**
     * Makes the given slime move.
     *
     * @effect | setOrientation(1)
     * @effect | setVelocity(new double[]{0, 0})
     * @effect | setAcceleration(new double[]{DEFAULT_HORIZONTAL_ACCELERATION, DEFAULT_VERTICAL_ACCELERATION})
     */
    public void startMoving() {
        setOrientation(1);
        setVelocity(new double[]{0, 0});
        setAcceleration(new double[]{DEFAULT_HORIZONTAL_ACCELERATION, 0});
    }

    /**
     * Ends the movement of this Slime.
     *
     * @post | new.getVelocity() == new double[]{0, 0}
     * @post | new.getAcceleration() == new double[]{0, 0}
     */
    public void endMoving() {
        setVelocity(new double[]{0, 0});
        setAcceleration(new double[]{0, 0});
    }

    /**
     * Switches the direction of the slime and sets its velocity and acceleration back to 0.
     *
     * @effect | this.setOrientation(-this.getOrientation())
     * @effect | this.setVelocity(new double[]{0, 0})
     * @effect | this.setAcceleration(new double[]{0, 0})
     */
    public void switchDirection() {
        this.setOrientation(-this.getOrientation());
        this.setVelocity(new double[]{0, 0});
        this.setAcceleration(new double[]{0, 0});
    }

// #####################################################################################################################

    /**
     * Advance the time of this Slime.
     *
     * @param dt The given time difference.
     * @post | if (getHitPoints() == 0)
     *       |    then this.terminate()
     * @effect | setTimeDifference((0.01)/(Math.sqrt(Math.pow(getVelocity()[0], 2) + Math.pow(getVelocity()[1], 2)) + Math.sqrt(Math.pow(getAcceleration()[0], 2) + Math.pow(getAcceleration()[1], 2))*dt))
     * @effect | if (Double.isInfinite(getTimeDifference()) || getTimeDifference() >= dt)
     *         |    then setTimeDifference(dt)
     * @effect | if (! isTerminated())
     *         |    then manageMovement()
     * @effect | if (! isTerminated() && getWorld() != null)
     *         |    then getWorld().manageSlimeGeoFeaturesCollision(getTimeDifference(), this)
     * @effect | if (! isTerminated() && getWorld() != null)
     *         |    then getWorld().manageSlimeCollision(this)
     * @post | new.dt = old.dt - getTimeDifference()
     */
    @Override
    public void advanceTime(double dt) {
        while (dt > 0) {
            if (getHitPoints() == 0)
                this.terminate();
            setTimeDifference((0.01)/(Math.sqrt(Math.pow(getVelocity()[0], 2) + Math.pow(getVelocity()[1], 2)) + Math.sqrt(Math.pow(getAcceleration()[0], 2) + Math.pow(getAcceleration()[1], 2))*dt));
            if (Double.isInfinite(getTimeDifference()) || getTimeDifference() >= dt)
                setTimeDifference(dt);
            if (! isTerminated()) {
                manageMovement();
                if (getWorld() != null) {
                    getWorld().manageSlimeGeoFeaturesCollision(getTimeDifference(), this);
                    getWorld().manageSlimeCollision(this);
                }
            } else break;
            dt -= getTimeDifference();
        }
    }

// #####################################################################################################################
}
