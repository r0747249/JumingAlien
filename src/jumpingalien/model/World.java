package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class world containing GameObjects, visiblewindow and dimension, geologicalFeatures and schools.
 *
 * @invar | canHaveAsVisibleWindowDimension(this.getVisibleWindowDimension())
 * @invar | canHaveAsVisbleWindowPosition(getVisbleWindowPosition())
 * @invar | canHaveAsTileSize(this.getTileSize())
 * @invar | isValidNbTiles(this.getNbTiles())
 * @invar | isValidGeologicalFeature()
 * @invar | isValidGeologicalFeatures(this.getGeologicalFeatures())
 * @invar | hasProperGameObjects()
 * @invar | isValidTargetTileCoordinate(getTargetTileCoordinate())
 * @invar | hasProperSchools()
 *
 * @version 3.0
 * @author Alexandre Vryghem (Bachelor Informatic first year)
 * https://github.com/KUL-ogp/ogp1819-project-vryghem-ziman
 */
public class World {

    /**
     * The maximum amount of GameObjects.
     */
    private final int MAX_AMOUNT_GAMEOBJECTS;

    /**
     * Return the width of this world.
     *
     * @return | result == this.width
     */
    @Basic @Raw @Immutable
    public int getWidth() {
        return this.width;
    }

    /**
     * Variable registering the width of this world.
     */
    private final int width;

    /**
     * Return the height of this world.
     *
     * @return | result = this.height
     */
    @Basic @Raw @Immutable
    public int getHeight() {
        return this.height;
    }

    /**
     * Variable registering the height of this world.
     */
    private final int height;

    /**
     * The first index is used to remove hitPoints when touching a plant. the thir for scullcab
     */
    public double[] timer = {0, 0, 0};

// #####################################################################################################################

    /**
     * Initialize this new world with given tileSize, nbTiles, targetTileCoordinate, visibleWindowDimension,
     * visibleWindowDimension and the geologicalFeatures.
     *
     * @param tileSize
     *        The tileSize for this new world.
     * @param nbTiles
     *        The nbTiles for this new world.
     * @param targetTileCoordinate
     *        The targetTileCoordinate for this new world.
     * @param visibleWindowDimension
     *        The visibleWindowDimension for this new world.
     * @param geologicalFeatures
     *        The geologicalFeatures for this new world.
     * @post | new.MAX_AMOUNT_GAMEOBJECTS == 100
     * @post | if (! canHaveAsTileSize(tileSize))
     *       |    then new.getTileSize == -tileSize
     *       |    else then new.getTileSize() == tileSize
     * @post | if (! isValidNbTiles(nbTiles))
     *       |    then if (nbTiles[0] < 0)
     *       |        then nbTiles = new int[]{-nbTiles[0], nbTiles[1]}
     *       |    then if (nbTiles[1] < 0)
     *       |        then nbTiles = new int[]{nbTiles[0], -nbTiles[1]}
     *       | new.nbTiles = nbTiles
     * @post | new.getWidth == getNbTiles()[0]*getTileSize()
     * @post | new.getHeight == getNbTiles()[1]*getTileSize()
     * @effect | this.setTargetTileCoordinate(targetTileCoordinate)
     * @post | new.visibleWindowDimension == visibleWindowDimension
     * @effect | if (isValidGeologicalFeatures(geologicalFeatures))
     *         |     then int[] geo = new int[getWidth()*getHeight()]
     *         |     for (int i = 0 ; i < geologicalFeatures.length; i++)
     *         |         if (isValidGeologicalFeature(geologicalFeatures[i]))
     *         |            then geo[i] == geologicalFeatures[i]
     *         |     then this.setGeologicalFeatures(geo)
     *         | else then this.setGeologicalFeatures(new int[getWidth()*getHeight()])
     * @throws NullPointerException
     *         | ! isValidTargetTileCoordinate(targetTileCoordinate)
     * @throws IllegalArgumentException
     *         | ! canHaveAsVisibleWindowDimension(this.getvisibleWindowDimension()) || ! isValidGeologicalFeatures(geologicalFeatures)
     */
    @Raw
    public World(int tileSize, int[] nbTiles, int[] targetTileCoordinate, int[] visibleWindowDimension, int[] geologicalFeatures) throws NullPointerException, IllegalArgumentException {
        MAX_AMOUNT_GAMEOBJECTS = 100;

        if (! canHaveAsTileSize(tileSize))
            this.tileSize = -tileSize;
        else this.tileSize = tileSize;

        if (! isValidNbTiles(nbTiles)) {
            if (nbTiles[0] < 0) nbTiles = new int[]{-nbTiles[0], nbTiles[1]};
            if (nbTiles[1] < 0) nbTiles = new int[]{nbTiles[0], -nbTiles[1]};
        }
        this.nbTiles = nbTiles;

        width = getNbTiles()[0]*getTileSize();
        height = getNbTiles()[1]*getTileSize();

        if (! isValidTargetTileCoordinate(targetTileCoordinate))
            throw new NullPointerException();
        this.setTargetTileCoordinate(targetTileCoordinate);

        if (! canHaveAsVisibleWindowDimension(visibleWindowDimension))
            throw new IllegalArgumentException();
        this.visibleWindowDimension = visibleWindowDimension;

        if (!isValidGeologicalFeatures(geologicalFeatures)) {
            throw new IllegalArgumentException("Not a valid geologicalFeature int[]");
        } else {
            int[] geo = new int[getWidth() * getHeight()];
            for (int i = 0; i < geologicalFeatures.length; i++) {
                if (isValidGeologicalFeature(geologicalFeatures[i]))
                    geo[i] = geologicalFeatures[i];
            }
            this.setGeologicalFeatures(geo);
        }
    }

// #####################################################################################################################

    /**
     * Return the Mazub that is moved by the user.
     *
     * @return | if (mazub != null && !mazub.isTerminated())
     *         |    then return this.mazub
     *         |    else then return null
     */
    @Basic
    public Mazub getMazub() {
        if (mazub != null && !mazub.isTerminated())
            return this.mazub;
        return null;
    }

    /**
     * Sets the mazub of this world to the given mazub.
     *
     * @param mazub
     *        The new mazub for this world.
     */
    @Raw
    public void setMazub(Mazub mazub) {
        this.mazub = mazub;
    }

    /**
     * Variable registering the mazub of this world.
     */
    private Mazub mazub;

// #####################################################################################################################

    /**
     * Return the visibleWindowDimension of this world.
     */
    @Basic @Raw @Immutable
    public int[] getVisibleWindowDimension() {
        return this.visibleWindowDimension;
    }

    /**
     * Check whether this world can have the given visibleWindowDimension as its visibleWindowDimension.
     *
     * @param  visibleWindowDimension
     *         The visibleWindowDimension to check.
     * @return | result == visibleWindowDimension[0] <= getWidth() && getVisibleWindowDimension()[1] <= getHeight() && visibleWindowDimension[0] >= 0 && getVisibleWindowDimension()[1] >= 0
     */
    @Raw
    public boolean canHaveAsVisibleWindowDimension(int[] visibleWindowDimension) {
        return visibleWindowDimension[0] <= getWidth() && visibleWindowDimension[1] <= getHeight() && visibleWindowDimension[0] >= 0 && visibleWindowDimension[1] >= 0;
    }

    /**
     * Variable registering the visibleWindowDimension of this world.
     */
    private final int[] visibleWindowDimension;

// #####################################################################################################################

    /**
     * Manages the new visibleWindowPosition of this world.
     *
     * @post | if (getMazub() == null)
     *       |   then new.getVisbleWindowPosition() == new int[]{0, 0}
     * @post | if (getVisbleWindowPosition()[0] + getVisibleWindowDimension()[0] - getMazub().getPixelPosition()[0] <= 200)
     *       |   then new.getVisbleWindowPosition()[0] = (getMazub().getPixelPosition()[0] + 200 <= getWidth()) ? getMazub().getPixelPosition()[0] + (200 - getVisibleWindowDimension()[0]) : getWidth() - getVisibleWindowDimension()[0]
     *       | else if (getMazub().getPixelPosition()[0] <= getVisbleWindowPosition()[0] + 200)
     *       |   then new.getVisbleWindowPosition()[0] = (getMazub().getPixelPosition()[0] > 200) ? getMazub().getPixelPosition()[0] - 200 : 0
     * @post | if (getVisbleWindowPosition()[1] + getVisibleWindowDimension()[1] - getMazub().getPixelPosition()[1] <= 200)
     *       |   then new.getVisbleWindowPosition()[1] = (200 + getMazub().getPixelPosition()[1] <= getHeight()) ? getMazub().getPixelPosition()[1] + (200 - getVisibleWindowDimension()[1]) : getHeight() - getVisibleWindowDimension()[1]
     *       | else if (getMazub().getPixelPosition()[1] <= getVisbleWindowPosition()[1] + 200)
     *       |   then new.getVisbleWindowPosition()[1] = (getMazub().getPixelPosition()[1] > 200) ? getMazub().getPixelPosition()[1] - 200 : 0
     */
    public void manageVisbleWindowPosition() {
        if (getMazub() == null)
            setVisbleWindowPosition(new int[]{0,0});
        else if (getVisibleWindowDimension()[0] > 400 + getMazub().getCurrentSprite().getWidth() || (getVisibleWindowDimension()[1] < 400+getMazub().getCurrentSprite().getHeight())) {
            int[] newWindowPosition = getVisbleWindowPosition();
            //right side & left side
            if (getVisbleWindowPosition()[0] + getVisibleWindowDimension()[0] - getMazub().getPixelPosition()[0] <= 200) {
                newWindowPosition[0] = (getMazub().getPixelPosition()[0] + 200 <= getWidth()) ? getMazub().getPixelPosition()[0] + (200 - getVisibleWindowDimension()[0]) : getWidth() - getVisibleWindowDimension()[0];
            } else if (getMazub().getPixelPosition()[0] <= getVisbleWindowPosition()[0] + 200) {
                newWindowPosition[0] = (getMazub().getPixelPosition()[0] > 200) ? getMazub().getPixelPosition()[0] - 200 : 0;
            }
            //top & bottom
            if (getVisbleWindowPosition()[1] + getVisibleWindowDimension()[1] - getMazub().getPixelPosition()[1] <= 200) {
                newWindowPosition[1] = (200 + getMazub().getPixelPosition()[1] <= getHeight()) ? getMazub().getPixelPosition()[1] + (200 - getVisibleWindowDimension()[1]) : getHeight() - getVisibleWindowDimension()[1];
            } else if (getMazub().getPixelPosition()[1] <= getVisbleWindowPosition()[1] + 200) {
                newWindowPosition[1] = (getMazub().getPixelPosition()[1] > 200) ? getMazub().getPixelPosition()[1] - 200 : 0;
            }
            setVisbleWindowPosition(newWindowPosition);
        }
    }

    /**
     * Returns the visbleWindowPosition of this World.
     */
    @Basic @Raw
    public int[] getVisbleWindowPosition() {
        return this.visbleWindowPosition;
    }

    /**
     * Check whether the given visbleWindowPosition is a valid visbleWindowPosition for this world.
     *
     * @param  visbleWindowPosition
     *         The visbleWindowPosition to check.
     * @return | result == (visbleWindowPosition[0] < getWidth()) && (visbleWindowPosition[1] < getHeight()) && (visbleWindowPosition[0]>=0)&& (visbleWindowPosition[1]>=0)
     */
    public boolean canHaveAsVisbleWindowPosition(int[] visbleWindowPosition) {
        return (visbleWindowPosition[0] < getWidth()) && (visbleWindowPosition[1] < getHeight()) && (visbleWindowPosition[0]>=0)&& (visbleWindowPosition[1]>=0);
    }

    /**
     * Set the visbleWindowPosition of this world to the given visbleWindowPosition.
     *
     * @param  visbleWindowPosition
     *         The new visbleWindowPosition for this world.
     * @post | if (canHaveAsVisbleWindowPosition(visbleWindowPosition))
     *       |    then new.getVisbleWindowPosition() == visbleWindowPosition
     * @throws IllegalArgumentException
     *         | ! canHaveAsVisbleWindowPosition(getVisbleWindowPosition())
     */
    @Raw
    public void setVisbleWindowPosition(int[] visbleWindowPosition) throws IllegalArgumentException {
        if (canHaveAsVisbleWindowPosition(visbleWindowPosition))
            this.visbleWindowPosition = visbleWindowPosition;
        else {
            throw new IllegalArgumentException("{"+visbleWindowPosition[0]+";"+visbleWindowPosition[1]+"}"+" is not a valid window position");
        }
    }

    /**
     * Variable registering the visbleWindowPosition of this world.
     */
    private int[] visbleWindowPosition = new int[]{0,0};

// #####################################################################################################################

    /**
     * Return the tileSize of this world.
     */
    @Basic @Raw @Immutable
    public int getTileSize() {
        return this.tileSize;
    }

    /**
     * Check whether this world can have the given tileSize as its tileSize.
     *
     * @param  tileSize
     *         The tileSize to check.
     * @return | result == tileSize > 0 && getWidth()%tileSize == 0 && getHeight()%tileSize == 0
     */
    @Raw
    public boolean canHaveAsTileSize(int tileSize) {
        return tileSize > 0 && getWidth()%tileSize == 0 && getHeight()%tileSize == 0;
    }

    /**
     * Variable registering the tileSize of this world.
     */
    private final int tileSize;

// #####################################################################################################################

    /**
     * Return the nbTiles of this world.
     */
    @Basic @Raw @Immutable
    public int[] getNbTiles() {
        return this.nbTiles;
    }

    /**
     * Check whether the given nbTiles are valid for this world.
     *
     * @param  nbTiles
     *         The nbTiles to check.
     * @return | result == nbTiles[0] > 0 && nbTiles[1] > 0
     */
    @Raw
    public static boolean isValidNbTiles(int[] nbTiles) {
        return nbTiles[0] > 0 && nbTiles[1] > 0;
    }

    /**
     * Variable registering the nbTiles of this world.
     */
    private final int[] nbTiles;

// #####################################################################################################################

    /**
     * Returns the index of the given pixelPosition in the array geologicalFeatures.
     *
     * @param pixelX
     *        The X-pixel needed to calculate the index.
     * @param pixelY
     *        The Y-pixel needed to calculate the index.
     * @return | result == Math.floor(pixelX/getTileSize()) + (Math.floor(pixelY/getTileSize()))*getNbTiles()[0]
     */
    public int calculateTile(int pixelX, int pixelY) {
        return (int) (Math.floor(pixelX/getTileSize()) + (Math.floor(pixelY/getTileSize()))*getNbTiles()[0]);
    }

    /**
     * Converts tileCoordinate to a pixelPosition.
     *
     * @param tileCoordinate
     *        The given tileCoordinate to convert.
     * @return | result == new int[]{getTileSize()*tileCoordinate[0] , getTileSize()*tileCoordinate[1]}
     */
    public int[] tileToPixel(int[] tileCoordinate){
        return new int[]{getTileSize()*tileCoordinate[0] , getTileSize()*tileCoordinate[1]};
    }

// #####################################################################################################################

    /**
     * Return the geologicalFeature of the given tile index.
     * @param tileIndex
     *        The given tileIndex.
     * @return | result == getGeologicalFeatures()[tileIndex]
     */
    @Basic @Raw
    public int getGeologicalFeature(int tileIndex) {
        return getGeologicalFeatures()[tileIndex];
    }

    /**
     * Check wether the geologicalFeature is a valid geologicalFeature for any world.
     *
     * @param geologicalFeature
     *        The given geologicalFeature to check.
     * @return | result == geologicalFeature >= 0 && geologicalFeature <= 3
     */
    private static boolean isValidGeologicalFeature(int geologicalFeature) {
        return geologicalFeature >= 0 && geologicalFeature <= 5;
    }

    /**
     * Set the tile with the given tileIndex to the given geologicalFeature.
     *
     * @param geologicalFeature
     *        The new geologicalFeatures for this world.
     * @param tileIndex
     *        The tileIndex for where the new geologicalFeature has to be set.
     *
     */
    @Raw
    public void setGeologicalFeature(int geologicalFeature, int tileIndex) {
        if (isValidGeologicalFeature(geologicalFeature))
            this.getGeologicalFeatures()[tileIndex] = geologicalFeature;
        else
            this.getGeologicalFeatures()[tileIndex] = 0;
    }

    /**
     * Return the geologicalFeatures of this world.
     */
    @Basic @Raw
    public int[] getGeologicalFeatures() {
        return this.geologicalFeatures;
    }

    /**
     * Check whether the given geologicalFeatures is a valid geologicalFeatures for any world.
     *
     * @param  geologicalFeatures
     *         The geologicalFeatures to check.
     * @return | result == geologicalFeatures != null
     */
    private static boolean isValidGeologicalFeatures(int[] geologicalFeatures) {
        return geologicalFeatures != null;
    }

    /**
     * Set the geologicalFeatures of this world to the given geologicalFeatures.
     *
     * @param  geologicalFeatures
     *         The new geologicalFeatures for this world.
     * @post | if (isValidGeologicalFeatures(geologicalFeatures))
     *       |    then new.getGeologicalFeatures() == geologicalFeatures
     */
    @Raw
    public void setGeologicalFeatures(int[] geologicalFeatures) {
        if (isValidGeologicalFeatures(geologicalFeatures))
            this.geologicalFeatures = geologicalFeatures;
    }

    /**
     * Variable registering the geologicalFeatures of this world.
     */
    private int[] geologicalFeatures;

// #####################################################################################################################

    /**
     * Returns a copy of the GameObjectSet of all not terminated gameObjects of this world.
     *
     * @return | result == gameObjects.stream().filter(gameObject -> !gameObject.isTerminated()).collect(Collectors.toSet())
     */
    public Set<GameObject> getGameObjectSet() {
        return gameObjects.stream().filter(gameObject -> !gameObject.isTerminated()).collect(Collectors.toSet());
    }

    /**
     * Check whether this world has the given GameObject as one of its GameObjects.
     *
     * @param  gameObject
     *         The GameObject to check.
     */
    @Raw
    public boolean hasAsGameObject(@Raw GameObject gameObject) {
        return gameObjects.contains(gameObject);
    }

    /**
     * Check whether this world can have the given GameObject
     * as one of its GameObjects.
     *
     * @param  gameObject
     *         The GameObject to check.
     * @return | result == (getNbGameObjects() + i < MAX_AMOUNT_GAMEOBJECTS + 1 || gameObject instanceof Mazub) && !isActiveGame() && (gameObject != null) && (GameObject.isValidWorld(this)) && (gameObject.getWorld() == this || gameObject.getWorld() == null)
     *         |        && gameObject.getPixelPosition()[0] >= 0 && gameObject.getPixelPosition()[1] >= 0 && gameObject.getPixelPosition()[0] < getWidth() && gameObject.getPixelPosition()[1] < getHeight()
     *         |        && !detectsObjectInObject(gameObject) && !gameObject.isTerminated() && (!(gameObject instanceof Mazub) || getMazub() == null) && (gameObject instanceof Plant || !containsImpassableTerrain(gameObject, 1))
     */
    @Raw
    public boolean canHaveAsGameObject(GameObject gameObject) {
        int i = 1;
        for (GameObject object : getGameObjectSet())
            if (object instanceof Mazub) i = 0;
        return (getNbGameObjects() + i < MAX_AMOUNT_GAMEOBJECTS + 1 || gameObject instanceof Mazub) && !isActiveGame() && (gameObject != null) && (GameObject.isValidWorld(this)) && (gameObject.getWorld() == this || gameObject.getWorld() == null)
                && gameObject.getPixelPosition()[0] >= 0 && gameObject.getPixelPosition()[1] >= 0 && gameObject.getPixelPosition()[0] < getWidth() && gameObject.getPixelPosition()[1] < getHeight()
                && !detectsObjectInObject(gameObject) && !gameObject.isTerminated() && (!(gameObject instanceof Mazub) || getMazub() == null) && (gameObject instanceof Plant || !containsImpassableTerrain(gameObject, 1));
    }

    /**
     * Check whether this World has proper GameObjects attached to it.
     *
     * @return | for each school in School:
     *         |   if (hasAsSchool(school))
     *         |      then canHaveAsSchool(school) && (school.getWorld() == this)
     */
    public boolean hasProperGameObjects() {
        for (GameObject gameObject : gameObjects) {
            if (!canHaveAsGameObject(gameObject)) return false;
            if (gameObject.getWorld() != this) return false;
        }
        return true;
    }

    /**
     * Return the number of GameObjects associated with this world.
     *
     * @return | result == gameObjects.size()
     */
    public int getNbGameObjects() {
        return gameObjects.size();
    }

    /**
     * Add the given GameObject to the set of GameObjects of this world.
     *
     * @param  gameObject
     *         The GameObject to be added.
     * @post | new.hasAsGameObject(gameObject)
     * @throws IllegalArgumentException
     *         | ! canHaveAsGameObject(gameObject)
     */
    public void addGameObject(@Raw GameObject gameObject) throws IllegalArgumentException {
        if (!canHaveAsGameObject(gameObject))
            throw new IllegalArgumentException();
        gameObjects.add(gameObject);
        gameObject.setWorld(this);
        if (gameObject instanceof Mazub && getMazub() == null)
            setMazub((Mazub) gameObject);
        else if (gameObject instanceof Slime) {
            if (((Slime) gameObject).getSchool() != null && !((Slime) gameObject).getSchool().hasAsSlime((Slime) gameObject))
                ((Slime) gameObject).getSchool().addSlime((Slime) gameObject);
            if (!SlimeInfo.hasAsSlime((Slime) gameObject))
                SlimeInfo.addSlime((Slime) gameObject);
        }
    }

    /**
     * Remove the given GameObject from the set of GameObjects of this world.
     *
     * @param  gameObject
     *         The GameObject to be removed.
     * @effect | gameObject.terminate()
     * @post | if (gameObject == getMazub())
     *       |    then new.isGameOver
     * @post | ! new.hasAsGameObject(gameObject)
     * @throws IllegalArgumentException
     *         | ! hasAsGameObject(gameObject)
     */
    @Raw
    public void removeGameObject(GameObject gameObject) throws IllegalArgumentException {
        if (! hasAsGameObject(gameObject))
            throw new IllegalArgumentException();
        gameObject.terminate();
        if (gameObject == getMazub())
            setGameOver(true);
        gameObjects.remove(gameObject);
    }

    /**
     * Variable referencing a set collecting all the GameObjects of this world.
     *
     * @invar  The referenced set is effective.
     *       | gameObjects != null
     * @invar | for each gameObject in gameObjects:
     *        |   ( (gameObject != null) && (! gameObject.isTerminated()) )
     */
    private final HashSet<GameObject> gameObjects = new HashSet<>();
    
// #####################################################################################################################

    /**
     * Return the targetTileCoordinate of this world.
     */
    @Basic @Raw
    public int[] getTargetTileCoordinate() {
        return this.targetTileCoordinate;
    }

    /**
     * Check whether the given targetTileCoordinate is a valid targetTileCoordinate for any world.
     *
     * @param  targetTileCoordinate
     *         The targetTileCoordinate to check.
     * @return | result == targetTileCoordinate != null
     */
    public static boolean isValidTargetTileCoordinate(int[] targetTileCoordinate) {
        return targetTileCoordinate != null && targetTileCoordinate.length == 2;
    }

    /**
     * Set the targetTileCoordinate of this world to the given targetTileCoordinate.
     *
     * @param  targetTileCoordinate
     *         The new targetTileCoordinate for this world.
     * @pre | isValidTargetTileCoordinate(targetTileCoordinate)
     * @post | new.getTargetTileCoordinate() == targetTileCoordinate
     */
    @Raw
    public void setTargetTileCoordinate(int[] targetTileCoordinate) {
        assert isValidTargetTileCoordinate(targetTileCoordinate);
        this.targetTileCoordinate = targetTileCoordinate.clone();
    }

    /**
     * Variable registering the pixelPosition of the targetTile.
     */
    private int[] targetTileCoordinate;

// #####################################################################################################################

    /**
     * Return the gameOver state of this world.
     */
    @Basic @Raw
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Sets the gameOver state of this world to the given gameOver.
     *
     * @param gameOver
     *        The new gameOver state of this world.
     * @post | new.isGameOver() == gameOver
     */
    @Raw
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Variable registering the gameOver state of this world.
     */
    private boolean gameOver;

// #####################################################################################################################

    /**
     * Manages the collision with The targetTile.
     *
     * @param gameObject
     *        The gameobject toc heck collision with.
     * @post | if (!gameObject.isTerminated() && getMazub().getPixelPosition()[0] < (tileToPixel(getTargetTileCoordinate())[0] + getTileSize() - 1) && getMazub().getPixelPosition()[0] > (tileToPixel(getTargetTileCoordinate())[0] - getMazub().getCurrentSprite().getWidth()) - 1 &&
     *                 getMazub().getPixelPosition()[1] < (tileToPixel(getTargetTileCoordinate())[1] + getTileSize() - 1) && getMazub().getPixelPosition()[1] > (tileToPixel(getTargetTileCoordinate())[1] - getMazub().getCurrentSprite().getHeight() - 1))
     *       |    then new.isGameOver()
     * @post | if (!gameObject.isTerminated() && getMazub().getPixelPosition()[0] < (tileToPixel(getTargetTileCoordinate())[0] + getTileSize() - 1) && getMazub().getPixelPosition()[0] > (tileToPixel(getTargetTileCoordinate())[0] - getMazub().getCurrentSprite().getWidth()) - 1 &&
     *                 getMazub().getPixelPosition()[1] < (tileToPixel(getTargetTileCoordinate())[1] + getTileSize() - 1) && getMazub().getPixelPosition()[1] > (tileToPixel(getTargetTileCoordinate())[1] - getMazub().getCurrentSprite().getHeight() - 1))
     *       |    then new.hasReachedTargetTile()
     */
    public void manageCollisionTargetTile(GameObject gameObject) {
        if (!gameObject.isTerminated() && getMazub().getPixelPosition()[0] < (tileToPixel(getTargetTileCoordinate())[0] + getTileSize() - 1) && getMazub().getPixelPosition()[0] > (tileToPixel(getTargetTileCoordinate())[0] - getMazub().getCurrentSprite().getWidth()) - 1 &&
                getMazub().getPixelPosition()[1] < (tileToPixel(getTargetTileCoordinate())[1] + getTileSize() - 1) && getMazub().getPixelPosition()[1] > (tileToPixel(getTargetTileCoordinate())[1] - getMazub().getCurrentSprite().getHeight() - 1)) {
            this.setGameOver(true);
            this.setReachedTargetTile(true);
        }
    }

    /**
     * Return the reachedTargetTile state of this world.
     */
    @Basic @Raw
    public boolean hasReachedTargetTile() {
        return this.reachedTargetTile;
    }

    /**
     * Sets the reachedTargetTile state of this world to the given gameOver.
     *
     * @param reachedTargetTile
     *        The new reachedTargetTile state of this world.
     * @post | new.hasReachedTargetTile() == reachedTargetTile
     */
    @Raw
    public void setReachedTargetTile(boolean reachedTargetTile) {
        this.reachedTargetTile = reachedTargetTile;
    }

    /**
     * Variable registering the reachedTargetTile state of this world.
     */
    private boolean reachedTargetTile;

// #####################################################################################################################

    /**
     * Return the activeGame state of this world.
     */
    @Basic @Raw
    public boolean isActiveGame() {
        return this.activeGame;
    }

    /**
     * Sets the activeGame state of this world to the given gameOver.
     *
     * @param  activeGame
     *         The new activeGame for this world.
     * @post | new.isActiveGame() == activeGame
     * @throws IllegalArgumentException
     *         | getMazub() == null
     */
    @Raw
    public void setActiveGame(boolean activeGame) throws IllegalArgumentException {
        if (getMazub() == null)
            throw new IllegalArgumentException();
        this.activeGame = activeGame;
    }

    /**
     * Variable registering the activeGame state of this world.
     */
    private boolean activeGame;

// #####################################################################################################################

    /**
     * Check wether this world is terminated.
     */
    @Basic @Raw
    public boolean isTerminated() {
        return this.isTerminated;
    }

    /**
     * Terminate this world and all objects it contains.
     *
     * @post | for each gameObject in getGameObjectSet():
     *       |    gameObject.terminate()
     * @post | new.isTerminated()
     */
    public void terminate() {
        for (GameObject gameObject: getGameObjectSet())
            gameObject.terminate();
        this.isTerminated = true;
    }

    /**
     * Variable registering wether or not this world is terminated.
     */
    private boolean isTerminated;

// #####################################################################################################################

    /**
     * Variable registering wether or not its the first dt time that mazub collides with WATER.
     */
    private boolean first = true;

    /**
     * Manages the hitpoints when the given object collides with MAGMA, WATER and GAS.
     *
     * @param dt
     *        The time that has passed between the last method call and the time now.
     * @post | if (collidesWithWater || collidesWithMagma || collidesWithGas)
     *       |    if (timer[0] > 0)
     *       |        then new.timer[0] == old.timer[0] - 0.2
     *       |    then new.timer[0] == old.timer[0] + dt
     *       |    else then new.timer[0] == 0
     * @effect | if (collidesWithMagma && timer[0] > 0)
     *         |    then getMazub().setHitPoints(getMazub().getHitPoints() - 50)
     * @effect | if (collidesWithMagma || collidesWithGas)
     *         |    then new.first == true
     * @effect | if (collidesWithGas && !collidesWithMagma && timer[0] > 0)
     *         |    then getMazub().setHitPoints(getMazub().getHitPoints() - 4)
     * @post | if (collidesWithWater && !collidesWithGas && !collidesWithMagma && first)
     *       |    then new.first == false && new.timer[0] == dt
     * @post | if (collidesWithWater && !collidesWithGas && !collidesWithMagma && (timer[0] >= 0.2))
     *       |    then new.timer[0] == old.timer[0] - 0.2
     * @effect | if (collidesWithWater && !collidesWithGas && !collidesWithMagma && (timer[0] >= 0.2))
     *         |    then getMazub().setHitPoints(getMazub().getHitPoints() - 2)
     */
    public void manageGeoFeaturesCollision(double dt) {
        boolean collidesWithWater = overlapsWithGeologicalFeature(getMazub(), GeologicalFeature.WATER, 1);
        boolean collidesWithGas = overlapsWithGeologicalFeature(getMazub(), GeologicalFeature.GAS, 1);
        boolean collidesWithMagma = overlapsWithGeologicalFeature(getMazub(), GeologicalFeature.MAGMA, 1);
        if (collidesWithWater || collidesWithGas || collidesWithMagma){
            timer[0] += dt;
            if (collidesWithMagma) {
                if (timer[0] > 0) {
                    getMazub().setHitPoints(getMazub().getHitPoints() - 50);
                    timer[0] -= 0.2;
                }
                first = true;
            } else if (collidesWithGas) {
                if (timer[0] > 0) {
                    getMazub().setHitPoints(getMazub().getHitPoints() - 4);
                    timer[0] -= 0.2;
                }
                first = true;
            } else {
                if (first) {
                    timer[0] = dt;
                    first = false;
                }
                if (timer[0] >= 0.2) {
                    getMazub().setHitPoints(getMazub().getHitPoints() - 2);
                    timer[0] -= 0.2;
                }
            }
        } else timer[0] = 0;
    }

    /**
     * Manages the hitpoints when the given object collides with MAGMA, WATER and GAS.
     *
     * @param dt
     *        The time that has passed between the last method call and the time now.
     * @param slime
     *        The object of wich its hitPoints needs to be managed.
     * @post | if (collidesWithWater || collidesWithGas || collidesWithMagma)
     *       |    then new.timer[1] == this.timer[1] + dt
     *       |    else then new.timer[1] == 0
     * @effect | if (collidesWithMagma)
     *         |    then slime.setIsDead(true)
     * @effect | if (collidesWithGas && !collidesWithMagma && timer[1] >= 0.3)
     *         |    then slime.setHitPoints(slime.getHitPoints() + 2)
     * @post | if (collidesWithGas && !collidesWithMagma && timer[1] >= 0.3)
     *       |    then new.lostHp == true && new.timer[1] == old.timer[1] - 0.3
     * @effect | if (collidesWithWater && !collidesWithGas && !collidesWithMagma && timer[1] >= 0.4)
     *         |    then slime.setHitPoints(slime.getHitPoints() - 4)
     * @post | if (collidesWithWater && !collidesWithGas && !collidesWithMagma && timer[1] >= 0.4)
     *       |    then new.lostHp == true && new.timer[1] == old.timer[1] - 0.4
     * @effect | if (lostHp && slime.getSchool() != null)
     *         |    then slime.getSchool().addHitpointsToSchool(slime, -1)
     */
    public void manageSlimeGeoFeaturesCollision(double dt, Slime slime) {
        boolean collidesWithWater = overlapsWithGeologicalFeature(slime, GeologicalFeature.WATER, 1);
        boolean collidesWithGas = overlapsWithGeologicalFeature(slime, GeologicalFeature.GAS, 1);
        boolean collidesWithMagma = overlapsWithGeologicalFeature(slime, GeologicalFeature.MAGMA, 1);
        boolean lostHp = false;
        if (collidesWithWater || collidesWithGas || collidesWithMagma) {
            timer[1] += dt;
            if (collidesWithMagma) {
                slime.setIsDead(true);
            } else if (collidesWithGas && timer[1] >= 0.3) {
                slime.setHitPoints(slime.getHitPoints() + 2);
                lostHp = true;
                timer[1] -= 0.3;
            } else if (timer[1] >= 0.4) {
                slime.setHitPoints(slime.getHitPoints() - 4);
                lostHp = true;
                timer[1] -= 0.4;
            }
            if (lostHp && slime.getSchool() != null)
                slime.getSchool().addHitpointsToSchool(slime, -1);
        } else timer[1] = 0;
    }

    /**
     * Manages the hitPoints of mazub and the slimes when they collide.
     *
     * @param slime The given slime to check collsion with.
     * @effect | if (!slime.isDead() && !slime.isTerminated() && getMazub() != null && isOverlapping(getMazub(), slime) && !getMazub().isDead() && getMazub().freeze <= 0 && getMazub().isMoving())
     *         |    then getMazub().setHitPoints(getMazub().getHitPoints() - 20)
     * @effect | if (!slime.isDead() && !slime.isTerminated() && getMazub() != null && isOverlapping(getMazub(), slime) && !getMazub().isDead() && getMazub().freeze <= 0)
     *         |    then slime.setHitPoints(slime.getHitPoints() - 30)
     * @effect | if (!slime.isDead() && !slime.isTerminated() && getMazub() != null && isOverlapping(getMazub(), slime) && !getMazub().isDead() && getMazub().freeze <= 0 && (mazub.getPixelPosition()[1] + getMazub().getCurrentSprite().getHeight() - 1 >= slime.getPixelPosition()[1]) && (mazub.getPixelPosition()[1] <= slime.getPixelPosition()[1] + slime.getCurrentSprite().getHeight() - 1))
     *         |    then slime.endMoving()
     * @effect | if (!slime.isDead() && !slime.isTerminated() && getMazub() != null && isOverlapping(getMazub(), slime) && !getMazub().isDead() && getMazub().freeze <= 0 && slime.getSchool() != null)
     *         |    then slime.getSchool().addHitpointsToSchool(slime, -1)
     * @post | if (!slime.isDead() && !slime.isTerminated() && getMazub() != null && isOverlapping(getMazub(), slime) && !getMazub().isDead() && getMazub().freeze <= 0)
     *       |    then new.getMazub().freeze == 0.6
     * @effect | if (getMazub() == null || !isOverlapping(getMazub(), slime) || getMazub().isDead() || getMazub().freeze > 0)
     *         |    for each blob in SlimeInfo.getSlimes():
     *         |        if (!slime.equals(blob) && isOverlapping(slime, blob) && !blob.isTerminated() && blob.getSchool() != null && slime.getSchool() != null && slime.getSchool().getNbSlimes() > blob.getSchool().getNbSlimes() && blob.canSwitchSchool(slime.getSchool()))
     *         |           then blob.switchSchool(slime.getSchool())
     * @effect | if (getMazub() == null || !isOverlapping(getMazub(), slime) || getMazub().isDead() || getMazub().freeze > 0)
     *         |    for each blob in SlimeInfo.getSlimes():
     *         |        if (!slime.equals(blob) && isOverlapping(slime, blob) && !blob.isTerminated() && blob.getSchool() != null && slime.getSchool() != null && slime.getSchool().getNbSlimes() < blob.getSchool().getNbSlimes() && slime.canSwitchSchool(blob.getSchool()))
     *         |           then slime.switchSchool(blob.getSchool())
     * @effect | if (getMazub() == null || !isOverlapping(getMazub(), slime) || getMazub().isDead() || getMazub().freeze > 0)
     *         |    for each blob in SlimeInfo.getSlimes():
     *         |        if (!slime.equals(blob) && isOverlapping(slime, blob) && !blob.isTerminated() && blob.getSchool() != null && slime.getSchool() != null)
     *         |           then slime.switchDirection()
     */
    public void manageSlimeCollision(Slime slime) {
        if (!slime.isDead() && !slime.isTerminated()) {
            if (getMazub() != null && isOverlapping(getMazub(), slime) && !getMazub().isDead() && getMazub().freeze <= 0) {
                if (getMazub().isMoving())
                    getMazub().setHitPoints(getMazub().getHitPoints() - 20);
                slime.setHitPoints(slime.getHitPoints() - 30);
                if ((mazub.getPixelPosition()[1] + getMazub().getCurrentSprite().getHeight() - 1 >= slime.getPixelPosition()[1]) && (mazub.getPixelPosition()[1] <= slime.getPixelPosition()[1] + slime.getCurrentSprite().getHeight() - 1))
                    slime.endMoving();
                if (slime.getSchool() != null)
                    slime.getSchool().addHitpointsToSchool(slime, -1);
                getMazub().freeze = 0.6;
            } else {
                for (Slime blob : SlimeInfo.getSlimes()) {
                    if (!slime.equals(blob) && isOverlapping(slime, blob) && !blob.isTerminated() && blob.getSchool() != null && slime.getSchool() != null) {
                        if (slime.getSchool().getNbSlimes() > blob.getSchool().getNbSlimes() && blob.canSwitchSchool(slime.getSchool()))
                            blob.switchSchool(slime.getSchool());
                        else if (slime.getSchool().getNbSlimes() < blob.getSchool().getNbSlimes() && slime.canSwitchSchool(blob.getSchool()))
                            slime.switchSchool(blob.getSchool());
                        slime.switchDirection();
                    }
                }
            }
        }
    }

    /**
     * Return true when the given slime overlaps with another slime.
     *
     * @param slime The given slime.
     * @param pixelPosition The pixel position of the given slime.
     * @return | for each blob in SlimeInfo.getSlimes():
     *         |    if (!slime.equals(blob) && rectangle.intersects(rectangle_slime)) {
     *         |       if (slime.getSchool() !=null && blob.getSchool() != null) {
     *         |          if (slime.getSchool().getNbSlimes() > blob.getSchool().getNbSlimes() && blob.getVelocity()[0]!=0)
     *         |             then blob.switchSchool(slime.getSchool());
     *         |          else if (slime.getSchool().getNbSlimes() < blob.getSchool().getNbSlimes())
     *         |             then slime.switchSchool(blob.getSchool());
     *         |       }
     *         |       then result == true
     *         |    }
     *         | result == false
     */
    public boolean isOverlappingOtherSlime(Slime slime, int[] pixelPosition) {
        Rectangle rectangle_slime = new Rectangle(pixelPosition[0], pixelPosition[1], slime.getCurrentSprite().getWidth()+1, slime.getCurrentSprite().getHeight()+1);
        for (Slime blob : SlimeInfo.getSlimes()) {
            Rectangle rectangle = new Rectangle(blob.getPixelPosition()[0], blob.getPixelPosition()[1], blob.getCurrentSprite().getWidth() + 1, blob.getCurrentSprite().getHeight() + 1);
            if (!slime.equals(blob) && rectangle.intersects(rectangle_slime)) {
                if (slime.getSchool() !=null && blob.getSchool() != null) {
                    if (slime.getSchool().getNbSlimes() > blob.getSchool().getNbSlimes() && blob.getVelocity()[0]!=0)
                        blob.switchSchool(slime.getSchool());
                    else if (slime.getSchool().getNbSlimes() < blob.getSchool().getNbSlimes())
                        slime.switchSchool(blob.getSchool());
                } return true;
            }
        } return false;
    }

    /**
     * Manages the hitpoints of the iven plant & mazub when colliding with a plant.
     *
     * @param plant The given plant to check collision for.
     * @param dt The timeDifference whith which the plant is advanced
     * @post | if (!plant.isTerminated() && getMazub() != null && isOverlapping(getMazub(), plant) && plant instanceof Skullcab)
     *       |    then new.plant.timer[3] == old.plant.timer[3] + dt
     * @post | if (!plant.isTerminated() && getMazub() != null && isOverlapping(getMazub(), plant) && (!plant instanceof Skullcab || plant.timer[3] >= 0) && !plant.isDead() && getMazub().getHitPoints() != 500)
     *       |    then new.plant.timer[3] == old.plant.timer[3] + dt
     * @post | if (!plant.isTerminated() && getMazub() != null && isOverlapping(getMazub(), plant) && plant instanceof Skullcab && !plant.isDead() && getMazub().getHitPoints() != 500)
     *       |    then new.plant.timer[3] == old.plant.timer[3] - plant.DELAY_LOSING_HITPOINTS
     * @effect | if (!plant.isTerminated() && getMazub() != null && isOverlapping(getMazub(), plant) && (!(plant instanceof Skullcab) || ((Skullcab) plant).timer[3] >= 0) && !plant.isDead() && getMazub().getHitPoints() != 500))
     *         |    then getMazub().setHitPoints(getMazub().getHitPoints() + 50)
     * @effect | if (plant.getHitPoints() == 0)
     *         |    then plant.terminate()
     * @effect | if (!plant.isTerminated() && getMazub() != null && isOverlapping(getMazub(), plant) && plant.isDead())
     *         |    then getMazub().setHitPoints(getMazub().getHitPoints() - 20)
     * @effect | if (!plant.isTerminated() && getMazub() != null && isOverlapping(getMazub(), plant) && plant.isDead())
     *         |    then plant.terminate()
     * @post | if ((plant.isTerminated() || getMazub() == null || !isOverlapping(getMazub(), plant)) && plant instanceof Skullcab)
     *       |    then plant.timer[3] = 0
     */
    public void managePlantCollision(Plant plant, double dt) {
        if (!plant.isTerminated() && getMazub() != null && isOverlapping(getMazub(), plant)) {
            if (plant instanceof Skullcab) ((Skullcab) plant).timer[3] += dt;
            if ((!(plant instanceof Skullcab) || ((Skullcab) plant).timer[3] >= 0) && !plant.isDead() && getMazub().getHitPoints() != 500) {
                if (plant instanceof Skullcab) ((Skullcab) plant).timer[3] -= ((Skullcab) plant).DELAY_LOSING_HITPOINTS;
                getMazub().setHitPoints(getMazub().getHitPoints() + 50);
                plant.setHitPoints(plant.getHitPoints() - 1);
                if (plant.getHitPoints() == 0)
                    plant.terminate();
            } else if (plant.isDead()) {
                getMazub().setHitPoints(getMazub().getHitPoints() - 20);
                plant.terminate();
            }
        } else {
            if (plant instanceof Skullcab) ((Skullcab) plant).timer[3] = 0;
        }
    }

    /**
     * Return if the the gameObjects overlaps with the given geologicalFeature.
     *
     * @param gameObject
     *        The given gameobject to check.
     * @param geologicalFeature
     *        The geologicalFeature to look for.
     * @param distanceBorder
     *        The distance between the outer layer border  and the layer to check (for inner and outer layer).
     * @return | for each pixel in gameObject:
     *         |    if (getGeologicalFeature(calculateTile(gameObject.getPixelPosition()[0] + i, gameObject.getPixelPosition()[1] + j)) == geologicalFeature.getValue())
     *         |        then result == true
     *         | result == false
     */
    public boolean overlapsWithGeologicalFeature(GameObject gameObject, GeologicalFeature geologicalFeature, int distanceBorder) {
        int width = gameObject.getCurrentSprite().getWidth() - 2 * distanceBorder, height = gameObject.getCurrentSprite().getHeight() - 2 * distanceBorder;
        for (int i = distanceBorder; i <= width; i++)
            for (int j = distanceBorder+1; j <= height; j++) {
                int calcT = calculateTile(gameObject.getPixelPosition()[0] + i, gameObject.getPixelPosition()[1] + j);
                if (calcT < getGeologicalFeatures().length && getGeologicalFeature(calcT) == geologicalFeature.getValue())
                    return true;
            }
        return false;
    }

    /**
     * Return if the inner layer of those objects overlap with each other.
     *
     * @param a
     *        The first GameObject to compare.
     * @param b
     *        The second Gameobject to compare.
     * @return | result == rectangle_a.intersects(rectangle_b)
     */
    public static boolean isOverlapping(GameObject a, GameObject b){
        Rectangle rectangle_a = new Rectangle(a.getPixelPosition()[0], a.getPixelPosition()[1], a.getCurrentSprite().getWidth()+1, a.getCurrentSprite().getHeight()+1);
        Rectangle rectangle_b = new Rectangle(b.getPixelPosition()[0], b.getPixelPosition()[1], b.getCurrentSprite().getWidth()+1, b.getCurrentSprite().getHeight()+1);
        return rectangle_a.intersects(rectangle_b);
    }

    /**
     * Checks if this GameObject overlaps a tile with an impassable geologicalFeature.
     *
     * @param pixelLeftBottom
     *        The GameObjects leftBottom pixelPosition.
     * @param pixelRightTop
     *        The GameObjects rightTop pixelPosition.
     * @return | for each pixel in the given square:
     *         |    for each geologicalFeature of GeologicalFeature.impassableFeat():
     *         |       if (getWorld().getGeologicalFeature(getWorld().calculateTile(pixelLeftBottom[0] + i, pixelLeftBottom[1] + j)) == geologicalFeature)
     *         |          then result == true
     *         | result == false
     */
    public boolean containsImpassableTerrain(int[] pixelLeftBottom, int[] pixelRightTop) {
        int squareWidth = pixelRightTop[0] - pixelLeftBottom[0], squareHeight = pixelRightTop[1] - pixelLeftBottom[1];
        for (int i = 0; i <= squareWidth; i++)
            for (int j = 0; j <= squareHeight; j++) {
                int tile = calculateTile(pixelLeftBottom[0] + i, pixelLeftBottom[1] + j);
                for (GeologicalFeature geologicalFeature : GeologicalFeature.impassableFeat())
                    if (tile < getGeologicalFeatures().length && getGeologicalFeature(tile) == geologicalFeature.getValue())
                        return true;
            }
        return false;
    }

// #####################################################################################################################

    /**
     * Checks for vertical collisions for this gameObject.
     *
     * @param gameObject
     *        The gameObject that needs to be checked.
     * @param newPos
     *        The position to check for collisions.
     * @return | result == containsImpassableTerrain(pixelLeftBottom, pixelRightTop)
     *         |            || objectCollisionInPixelSquare(gameObject, pixelLeftBottom, pixelRightTop, Slime.class)
     */
    public boolean checkVerticalCollision(GameObject gameObject, double[] newPos) {
        int[] pixelLeftBottom, pixelRightTop;
        int width = gameObject.getCurrentSprite().getWidth(), height = gameObject.getCurrentSprite().getHeight();
        if (gameObject.getActualPosition()[1] < newPos[1]) {
            pixelLeftBottom = new int[]{gameObject.getPixelPosition()[0] +1, gameObject.getPixelPosition()[1] + height};
            pixelRightTop = new int[]{gameObject.getPixelPosition()[0] + width -2, gameObject.getPixelPosition()[1] + height};
        } else {
            pixelLeftBottom = new int[]{gameObject.getPixelPosition()[0] + 1, gameObject.getPixelPosition()[1]};
            pixelRightTop = new int[]{gameObject.getPixelPosition()[0] + width -2, gameObject.getPixelPosition()[1]};
        }
        return containsImpassableTerrain(pixelLeftBottom, pixelRightTop)
                || objectCollisionInPixelSquare(gameObject, pixelLeftBottom, pixelRightTop, Slime.class);

    }

    /**
     * Checks for horizontal collisions for this gameObject.
     *
     * @param gameObject
     *        The gameObject that needs to be checked.
     * @param newPos
     *        The new position to check for collisions.
     * @return | result == containsImpassableTerrain(pixelLeftBottom, pixelRightTop)
     *         |            || objectCollisionInPixelSquare(gameObject, pixelLeftBottom, pixelRightTop, Slime.class)
     */
    public boolean checkHorizontalCollision(GameObject gameObject, double[] newPos) {
        int[] pixelLeftBottom, pixelRightTop;
        int width = gameObject.getCurrentSprite().getWidth(), height = gameObject.getCurrentSprite().getHeight();
        if (gameObject.getActualPosition()[0] < newPos[0]) {
            pixelLeftBottom = new int[]{gameObject.getPixelPosition()[0] + width, gameObject.getPixelPosition()[1]+1};
            pixelRightTop = new int[]{gameObject.getPixelPosition()[0] + width, gameObject.getPixelPosition()[1] + height -2};
        } else {
            pixelLeftBottom = new int[]{gameObject.getPixelPosition()[0], gameObject.getPixelPosition()[1]+1};
            pixelRightTop = new int[]{gameObject.getPixelPosition()[0], gameObject.getPixelPosition()[1] + height - 2};
        }
        return containsImpassableTerrain(pixelLeftBottom, pixelRightTop)
                || objectCollisionInPixelSquare(gameObject, pixelLeftBottom, pixelRightTop, Slime.class);
    }

    /**
     * Return true when he collides with an impassable terrain.
     *
     * @param slime The given slime to check collision for.
     * @return | result == containsImpassableTerrain(pixelLeftBottom, pixelRightTop)
     */
    public boolean horizontalSlimeCollision(Slime slime) {
        int[] pixelLeftBottom, pixelRightTop;
        int width = slime.getCurrentSprite().getWidth(), height = slime.getCurrentSprite().getHeight();
        if (slime.getOrientation() == 1) {
            pixelLeftBottom = new int[]{slime.getPixelPosition()[0] + width, slime.getPixelPosition()[1]+1};
            pixelRightTop = new int[]{slime.getPixelPosition()[0] + width, slime.getPixelPosition()[1] + height -2};
        } else {
            pixelLeftBottom = new int[]{slime.getPixelPosition()[0], slime.getPixelPosition()[1]+1};
            pixelRightTop = new int[]{slime.getPixelPosition()[0], slime.getPixelPosition()[1] + height - 2};
        }
        return containsImpassableTerrain(pixelLeftBottom, pixelRightTop);
    }

    /**
     * Check if there is an object of the given objectType in the square defined by pixelLeftBottom and pixelRightTop.
     *
     * @param gameObject
     *        The gameObject that needs to be checked.
     * @param pixelLeftBottom
     *        The bottom left corner of this mazub.
     * @param pixelRightTop
     *        The top right corner of this mazub.
     * @param objectType
     *        The type of Object that we are searchin for in the square.
     * @return | for each pixel in the pixelSquare:
     *         |    if (detectsObjectTypeInObject(new int[] {pixelLeftBottom[0] + i, pixelLeftBottom[1] + j}, objectType))
     *         |       then result == true
     *         | result == false
     */
    public boolean objectCollisionInPixelSquare(GameObject gameObject, int[] pixelLeftBottom, int[] pixelRightTop, Class objectType) {
        int squareWidth = pixelRightTop[0] - pixelLeftBottom[0], squareHeight = pixelRightTop[1] - pixelLeftBottom[1];
        for (int i = 0; i <= squareWidth; i++)
            for (int j = 0; j <= squareHeight; j++)
                if (detectsObjectTypeInObject(gameObject, new int[] {pixelLeftBottom[0] + i, pixelLeftBottom[1] + j}, objectType))
                    return true;
        return false;
    }

    /**
     * Detects if the object contains the given objectType.
     *
     * @param gameObject
     *        The gameObject that needs to be checked.
     * @param pixelPosition
     * 		  The pixelPosition of the object.
     * @param objectType
     * 		  The object type we are checking for in the pixel
     * @return | for each object in GameObjectSet:
     *         |    if (pixel[0] >= object.getPixelPosition()[0]  && pixel[0] <= object.getPixelPosition()[0] + object.getCurrentSprite().getWidth() - 1
     *         |        && pixel[1] >= object.getPixelPosition()[1] && pixel[1] <= object.getPixelPosition()[1] + object.getCurrentSprite().getHeight() - 1
     *         |        && object != this && object.getClass() == objectType)
     *         |       then result == true
     *         | result == false
     */
    private boolean detectsObjectTypeInObject(GameObject gameObject, int[] pixelPosition, Class objectType) {
        for (GameObject object: getGameObjectSet())
            if (object != gameObject && object.getClass() == objectType
                    && pixelPosition[0] >= object.getPixelPosition()[0] && pixelPosition[1] >= object.getPixelPosition()[1]
                    && pixelPosition[0] <= object.getPixelPosition()[0] + object.getCurrentSprite().getWidth() - 1
                    && pixelPosition[1] <= object.getPixelPosition()[1] + object.getCurrentSprite().getHeight() - 1)
                return true;
        return false;
    }

    /**
     * Detects if there is an object thats not a plant that collides with the given gameObject.
     *
     * @param gameObject
     *        The given gameObject to check.
     * @return | for each object in getGameObjectSet():
     *         |     if (!(object instanceof Plant) && !(gameObject instanceof Plant) && rectangle2.intersects(rectangle1))
     *         |        then reult == true
     *         | result == false
     */
    public boolean detectsObjectInObject(GameObject gameObject) {
        Rectangle rectangle1 = new Rectangle(gameObject.getPixelPosition()[0], gameObject.getPixelPosition()[1], gameObject.getCurrentSprite().getWidth(), gameObject.getCurrentSprite().getHeight());
        for (GameObject object : getGameObjectSet()) {
            Rectangle rectangle2 = new Rectangle(object.getPixelPosition()[0], object.getPixelPosition()[1], object.getCurrentSprite().getWidth(),object.getCurrentSprite().getHeight());
            if (!(object instanceof Plant) && !(gameObject instanceof Plant) && rectangle2.intersects(rectangle1))
                return true;
        } return false;
    }

    /**
     * Return true when the given gameObject overlaps with an impassable terrain.
     *
     * @param gameObject The given game Object.
     * @param dist The distance with the border.
     * @return | for each geoFeauture in GeologicalFeature.impassableFeat():
     *         |    if (overlapsWithGeologicalFeature(gameObject, geoFeauture, dist))
     *         |        then result == true
     *         | result == false
     */
    public boolean containsImpassableTerrain(GameObject gameObject, int dist) {
        for (GeologicalFeature geoFeauture : GeologicalFeature.impassableFeat()) {
            if (overlapsWithGeologicalFeature(gameObject, geoFeauture, dist))
                return true;
        } return false;
    }

// #####################################################################################################################

    /**
     * Check whether this School has the given School as one of its Schools.
     *
     * @param school
     *        The School to check.
     */
    @Basic @Raw
    public boolean hasAsSchool(@Raw School school) {
      return schools.contains(school);
    }

    /**
     * Check whether this School can have the given School
     * as one of its Schools.
     *
     * @param  school
     *         The School to check.
     * @return | result == (school != null) && School.isValidWorld(this)
     */
    @Raw
    public boolean canHaveAsSchool(School school) {
      return (school != null) && (School.isValidWorld(this));
    }

    /**
     * Check whether this School has proper Schools attached to it.
     *
     * @return | for each school in School:
     *         |   if (hasAsSchool(school))
     *         |      then canHaveAsSchool(school) && (school.getWorld() == this)
     */
    public boolean hasProperSchools() {
        for (School school : schools) {
            if (!canHaveAsSchool(school)) return false;
            if (school.getWorld() != this) return false;
        }
        return true;
    }

    /**
     * Return the number of Schools associated with this School.
     *
     * @return | result == card({school:School | hasAsSchool({school)})
     */
    public int getNbSchools() {
      return schools.size();
    }

    /**
     * Add the given School to the set of Schools of this School.
     *
     * @param  school
     *         The School to be added.
     * @pre | (school != null) && (school.getWorld() == this)
     * @post | new.hasAsSchool(school)
     * @throws IllegalArgumentException
     *         | getNbSchools() >= 10
     */
    public void addSchool(@Raw School school) throws IllegalArgumentException {
      assert (school != null) && (school.getWorld() == this);
        if (getNbSchools() >= 10)
            throw new IllegalArgumentException();
        schools.add(school);
    }

    /**
     * Return the set that contains all schools.
     */
    public Set<School> getSchools() {
        return schools;
    }

    /**
     * Variable referencing a set collecting all the Schools
     * of this School.
     *
     * @invar | schools != null
     * @invar | for each school in schools:
     *        |   ( (school != null) && (! school.isTerminated()) )
     */
    private final Set<School> schools = new HashSet<>();

// #####################################################################################################################

    /**
     * Advances the time of this World and all it's objects with the given timeDiference.
     *
     * @param dt The given time difference.
     * @effect | for each gameObject of getGameObjectSet()
     *         |    gameObject.advanceTime(dt)
     * @effect | manageVisbleWindowPosition()
     * @throws IllegalArgumentException
     *         | Double.isNaN(dt) || Double.isInfinite(dt) || (dt > 0.2) || (dt < 0)
     */
    public void advanceTime(double dt) throws IllegalArgumentException {
        if (Double.isNaN(dt) || Double.isInfinite(dt) || (dt > 0.2) || (dt < 0))
            throw new IllegalArgumentException();
        if (getMazub() != null)
            getMazub().advanceTime(dt);
        for (GameObject object : getGameObjectSet())
            if (!(object instanceof Mazub))
                object.advanceTime(dt);
        manageVisbleWindowPosition();
    }

// #####################################################################################################################
}