package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import java.util.*;

/**
 * A class containing a hashMap of all the slimes with their id's.
 *
 * @invar | hasProperSlimes()
 */
public class SlimeInfo {

    /**
     * Check whether this SlimeInfo has the given Slime as one of its
     * Slimes.
     *
     * @param  slime
     *         The Slime to check.
     */
    @Basic @Raw
    public static boolean hasAsSlime(@Raw Slime slime) {
      return slimes.contains(slime);
    }

    /**
     * Check whether this SlimeInfo can have the given Slime
     * as one of its Slimes.
     *
     * @param  slime
     *         The Slime to check.
     * @return | result == (slime != null)
     */
    @Raw
    public boolean canHaveAsSlime(Slime slime) {
      return (slime != null);
    }

    /**
     * Check whether this SlimeInfo has proper Slimes attached to it.
     *
     * @return | for each slime in Slime:
     *         |   if (hasAsSlime(slime))
     *         |     then canHaveAsSlime(slime)
     */
    public boolean hasProperSlimes() {
        for (Slime slime : slimes) {
            if (!canHaveAsSlime(slime))
                return false;
        }
        return true;
    }

    /**
     * Return the number of Slimes associated with this SlimeInfo.
     *
     * @return | result == card({slime:Slime | hasAsSlime({slime)})
     */
    public int getNbSlimes() {
      return slimes.size();
    }

    /**
     * Add the given Slime to the set of Slimes of this SlimeInfo.
     *
     * @param  slime
     *         The Slime to be added.
     * @pre  | (slime != null)
     * @post | new.hasAsSlime(slime)
     */
    public static void addSlime(@Raw Slime slime) {
      assert (slime != null);
      slimes.add(slime);
    }

    /**
     * Remove the given Slime from the set of Slimes of this SlimeInfo.
     *
     * @param  slime
     *         The Slime to be removed.
     * @pre | this.hasAsSlime(slime)
     * @post | ! new.hasAsSlime(slime)
     */
    @Raw
    public static void removeSlime(Slime slime) {
      assert hasAsSlime(slime);
      slimes.remove(slime);
    }

    public static Set<Slime> getSlimes() {
        return slimes;
    }

    /**
     * Variable referencing a set collecting all the Slimes
     * of this SlimeInfo.
     *
     * @invar | slimes != null
     * @invar | for each slime in slimes:
     *        |   ( (slime != null) && (! slime.isTerminated()) )
     */
    private static Set<Slime> slimes = new HashSet<>();

// #####################################################################################################################

    public static HashMap<Slime, Long> allIds = new HashMap<>();
}
