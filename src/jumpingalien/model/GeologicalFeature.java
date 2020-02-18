package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An enumeration introducing different geologicalFeatures. The geologicalFeatures
 * are used to check if they are passable for example.
 *   In its current form, the class supports the geologicalFeautures AIR,
 *   SOLID_GROUND, WATER, MAGMA, ICE and GAS.
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
@Value
public enum GeologicalFeature {
    AIR(0),
    SOLID_GROUND(1),
    WATER(2),
    MAGMA(3),
    ICE(4),
    GAS(5);

    /**
     * Initializes this geologicalFeature with the given value.
     * @param geoFeature The value for this new geological feature.
     * @post | new.value == geoFeature
     */
    GeologicalFeature(int geoFeature) {
        this.value = geoFeature;
    }

    /**
     * Return a set of GeologicalFeatures that are impassable.
     *
     * @post | impassable.contains(ICE) && impassable.contains(SOLID_GROUND)
     * @return | result == impassable
     */
    @Basic
    public static Set<GeologicalFeature> impassableFeat(){
        Set<GeologicalFeature> impassable = new HashSet<>();
        impassable.add(SOLID_GROUND);
        impassable.add(ICE);
        return impassable;
    }

    /**
     * Return the value of this geological feauture.
     */
    @Basic @Raw @Immutable
    public int getValue() {
        return value;
    }

    /**
     * Variable storing the value of this geological feature.
     */
    private final int value;

    /**
     * Return the value as a string.
     * @return | result == "[Feature " + getValue() + "]"
     */
    @Override
    public String toString() {
        return "[Feature " + getValue() + "]";
    }
}
