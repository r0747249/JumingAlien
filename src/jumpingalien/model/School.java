package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A school containing slimes, a world.
 *
 * @invar | hasProperSlimes()
 * @invar | isValidWorld(getWorld())
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public class School {

    /**
     * Initialize this new SlimeCollection as a non-terminated SlimeCollection with
     * no Slimes yet and the given world.
     * @param world
     *        The world for this new school.
     * @post | new.getNbSlimes() == 0
     * @effect | this.setWorld(world)
     * @post | if (world != null)
     *       |    then getWorld().getSchools().hasAsSchool(this)
     */
    @Raw
    public School(World world) throws IllegalArgumentException {
        if (! isValidWorld(world))
            throw new IllegalArgumentException("Not a valid World");
        setWorld(world);
        if (world != null)
            getWorld().addSchool(this);
    }

// #####################################################################################################################

    /**
     * Check wether this school is terminated.
     */
    @Basic @Raw
    public boolean isTerminated() {
        return this.isTerminated;
    }

    /**
     * Terminate this school.
     *
     * @post | new.isTerminated()
     */
    public void terminate() {
        this.isTerminated = true;
    }

    /**
     * Variable registering wether or not this school is terminated.
     */
    private boolean isTerminated;

// #####################################################################################################################

    /**
     * Return the Slimes of this School.
     */
    @Basic @Raw
    public Set<Slime> getSlimes() {
        return new HashSet<>(slimes);
    }

    /**
     * Check whether this SlimeCollection has the given Slime as one of its Slimes.
     *
     * @param  slime
     *         The Slime to check.
     * @return | slimes.contains(slime)
     */
    @Basic @Raw
    public boolean hasAsSlime(@Raw Slime slime) {
        return slimes.contains(slime);
    }

    /**
     * Check whether this SlimeCollection can have the given Slime as one of its Slimes.
     *
     * @param  slime
     *         The Slime to check.
     * @return | result == (slime != null) && Slime.isValidSchool(this)
     */
    @Raw
    public boolean canHaveAsSlime(Slime slime) {
      return (slime != null) && (Slime.isValidSchool(this));
    }

    /**
     * Check whether this SlimeCollection has proper Slimes attached to it.
     *
     * @return | for each slime in Slime:
     *         |   if (hasAsSlime(slime))
     *         |     then canHaveAsSlime(slime) && (slime.getSchool() == this)
     */
    public boolean hasProperSlimes() {
        for (Slime slime : slimes) {
            if (!canHaveAsSlime(slime) || slime.getSchool() != this)
                return false;
        }
        return true;
    }

    /**
     * Return the number of Slimes associated with this SlimeCollection.
     *
     * @return | result == card({slime:Slime | hasAsSlime({slime)})
     */
    public int getNbSlimes() {
      return slimes.size();
    }

    /**
     * Add the given Slime to the set of Slimes of this SlimeCollection.
     *
     * @param  slime
     *         The Slime to be added.
     * @pre | (slime != null) && (slime.getSchool() == this)
     * @post | hasAsSlime(slime)
     * @post | SlimeInfo.hasAsSlime(slime)
     * @throws IllegalArgumentException
     *         | hasAsSlime(slime) || slime.isTerminated() || slime.getSchool() == null || slime.getSchool().isTerminated()
     */
    public void addSlime(@Raw Slime slime) throws IllegalArgumentException{
        assert (slime != null) && (slime.getSchool() == this);
        if (hasAsSlime(slime) || slime.isTerminated() || slime.getSchool() == null || slime.getSchool().isTerminated())
            throw new IllegalArgumentException("can't add slime");
        slimes.add(slime);
        SlimeInfo.addSlime(slime);
    }

    /**
     * Remove the given Slime from the set of Slimes of this SlimeCollection.
     *
     * @param  slime
     *         The Slime to be removed.
     * @post | ! new.hasAsSlime(slime)
     * @post | slime.getSchool() == null
     * @post | ! hasAsSlime(slime)
     * @throws IllegalArgumentException
     *         | ! hasAsSlime(slime)
     */
    @Raw
    public void removeSlime(Slime slime) throws IllegalArgumentException {
        if (!hasAsSlime(slime))
            throw new IllegalArgumentException();
        slime.setSchool(null);
        slimes.remove(slime);
    }

    /**
     * Variable referencing a set collecting all the Slimes of this SlimeCollection.
     *
     * @invar | slimes != null
     * @invar | for each slime in slimes:
     *        |   ( (slime != null) &&
     *        |     (! slime.isTerminated()) )
     */
    private final Set<Slime> slimes = new HashSet<>();

// #####################################################################################################################
    
    /**
     * Return the world of this school.
     */
    @Basic @Raw
    public World getWorld() {
        return this.world;
    }
    
    /**
     * Check whether the given world is a valid world for
     * any school.
     *
     * @param  world
     *         The world to check.
     * @return | result == world == null || !world.isTerminated()
     */
    public static boolean isValidWorld(World world) {
        return world == null || !world.isTerminated();
    }
    
    /**
     * Set the world of this school to the given world.
     *
     * @param  world
     *         The new world for this school.
     * @post | new.getWorld() == world
     * @throws IllegalArgumentException
     *         | ! isValidWorld(getWorld())
     */
    @Raw
    public void setWorld(World world) throws IllegalArgumentException {
        if (! isValidWorld(world))
            throw new IllegalArgumentException();
        this.world = world;
    }
    
    /**
     * Variable registering the world of this school.
     */
    private World world;

// #####################################################################################################################

    /**
     * Add the given amount of hit points to each living slime in this school.µ
     *
     * @param slime The slime that collided.
     * @param hitPoints The amount of hitpoints that need to be added to each slime.
     * @effect | for each blob in getSlimes():
     *         |    if (!blob.equals(slime) && !blob.isTerminated())
     *         |        then blob.setHitPoints(blob.getHitPoints() + hitPoints)
     */
    public void addHitpointsToSchool(Slime slime, int hitPoints) {
        for (Slime blob : getSlimes()) {
            if (!blob.equals(slime) && !blob.isTerminated())
                blob.setHitPoints(blob.getHitPoints() + hitPoints);
        }
    }
}
