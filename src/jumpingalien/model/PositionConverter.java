package jumpingalien.model;

/**
 * A class converting a position to meters or pixels.
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public class PositionConverter {

    /**
     * Return an integer that transforms the given meters to pixels.
     * This integer is expressed in pixels.
     *
     * @param meters
     *        The meters that need converting.
     * @return Returns an integer that gives the pixels for the given meters.
     *         | result == Math.floor(meters*100)
     */
    public static int metersToPixel(double meters) { return (int) Math.floor(meters * 100); }

    /**
     * Return an integer that transforms the given pixels to meters.
     * This integer is expressed in meters.
     *
     * @param pixel
     *        The pixel that needs converting.
     * @return Returns a double that gives meters for the given pixels.
     *         | result == pixel/100
     */
    public static double pixelToMeters(double pixel) { return pixel / 100; }
}
