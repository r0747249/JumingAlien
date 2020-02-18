package jumpingalien.facade;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import jumpingalien.model.*;
import jumpingalien.util.ModelException;
import jumpingalien.util.ShouldNotImplementException;
import jumpingalien.util.Sprite;

public class Facade implements IFacade {

    @Override
    public boolean isTeamSolution() { return false; }

    @Override
    public boolean isLateTeamSplit() { return false; }

    @Override
    public boolean hasImplementedWorldWindow() {
        return true;
    }

    /**
     * Flag that makes getCurrentSprite() return the default sprite for duking and moving
     */
    public static boolean isExecutingTest() { return true; }

    @Override
    public Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
        try {
            return new Mazub(new int[]{pixelLeftX, pixelBottomY}, sprites);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Position is not in the world");
        } catch (NullPointerException exc) {
            throw new ModelException("Not a valid Sprite");
        }
    }

    @Override
    public double[] getActualPosition(Mazub alien) throws ModelException {
        return alien.getActualPosition();
    }

    @Override
    public void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException {
        try {
            alien.setActualPosition(newPosition);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Not a valid actual position");
        }
    }

    @Override
    public int[] getPixelPosition(Mazub alien) throws ModelException {
        return alien.getPixelPosition();
    }

    @Override
    public int getOrientation(Mazub alien) throws ModelException {
        return alien.getOrientation();
    }

    @Override
    public double[] getVelocity(Mazub alien) throws ModelException {
        return alien.getVelocity();
    }

    @Override
    public double[] getAcceleration(Mazub alien) throws ModelException {
        return alien.getAcceleration();
    }

    @Override
    public Sprite[] getSprites(Mazub alien) throws ModelException, ShouldNotImplementException {
        return alien.getSprites().clone();
    }

    @Override
    public Sprite getCurrentSprite(Mazub alien) throws ModelException {
        return alien.getCurrentSprite();
    }

    @Override
    public boolean isMoving(Mazub alien) throws ModelException {
        return alien.isMoving();
    }

    @Override
    public void startMoveLeft(Mazub alien) throws ModelException {
        if (alien.canHaveAsIsMoving(true))
            alien.startMove(-1);
        else
            throw new ModelException("The alien is already in the given Moving state");
    }

    @Override
    public void startMoveRight(Mazub alien) throws ModelException {
        if (alien.canHaveAsIsMoving(true))
            alien.startMove(1);
        else throw new ModelException("The alien is already in the given Moving state");
    }

    @Override
    public void endMove(Mazub alien) throws ModelException {
        if (alien.canHaveAsIsMoving(false))
            alien.endMove();
        else
            throw new ModelException("The alien is already in the given Moving state");
    }

    @Override
    public boolean isJumping(Mazub alien) throws ModelException {
        return alien.isJumping();
    }

    @Override
    public void startJump(Mazub alien) throws ModelException {
        try {
            alien.startJump();
        } catch (RuntimeException exc) {
            throw new ModelException("Is already in the given Jumping state");
        }
    }

    @Override
    public void endJump(Mazub alien) throws ModelException {
        try {
            alien.endJump();
        } catch (RuntimeException exc) {
            throw new ModelException("Is already in the given Jumping state");
        }
    }

    @Override
    public boolean isDucking(Mazub alien) throws ModelException {
        return alien.isDucking();
    }

    @Override
    public void startDuck(Mazub alien) throws ModelException {
        alien.startDuck();
    }

    @Override
    public void endDuck(Mazub alien) throws ModelException {
        alien.endDuck();
    }

// #####################################################################################################################

    @Override
    public World createWorld(int tileSize, int nbTilesX, int nbTilesY, int[] targetTileCoordinate, int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures) throws ModelException {
        try {
            return new World(tileSize, new int[]{nbTilesX, nbTilesY}, targetTileCoordinate, new int[]{visibleWindowWidth, visibleWindowHeight}, geologicalFeatures);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Illegal visibleWindowDimension");
        } catch (NullPointerException exc) {
            throw new ModelException("The targetTileCoordinate is null");
        }
    }

    @Override
    public void terminateWorld(World world) throws ModelException {
        world.terminate();
    }

    @Override
    public int[] getSizeInPixels(World world) throws ModelException {
        return new int[]{world.getWidth(), world.getHeight()};
    }

    @Override
    public int getTileLength(World world) throws ModelException {
        return world.getTileSize();
    }

    @Override
    public int getGeologicalFeature(World world, int pixelX, int pixelY) throws ModelException {
        return world.getGeologicalFeature(world.calculateTile(pixelX, pixelY));
    }

    @Override
    public void setGeologicalFeature(World world, int pixelX, int pixelY, int geologicalFeature) throws ModelException {
        world.setGeologicalFeature(geologicalFeature, world.calculateTile(pixelX, pixelY));
    }

    @Override
    public int[] getVisibleWindowDimension(World world) throws ModelException {
        return world.getVisibleWindowDimension();
    }

    @Override
    public int[] getVisibleWindowPosition(World world) throws ModelException {
            return world.getVisbleWindowPosition();
    }

    @Override
    public boolean hasAsGameObject(Object object, World world) throws ModelException {
        return world.getGameObjectSet().contains(object);
    }

    @Override
    public Set<Object> getAllGameObjects(World world) throws ModelException {
        return world.getGameObjectSet().stream().map(gameObject -> (Object) gameObject).collect(Collectors.toSet());
    }

    @Override
    public Mazub getMazub(World world) throws ModelException {
        return world.getMazub();
    }

    @Override
    public void addGameObject(Object object, World world) throws ModelException {
        try {
            world.addGameObject((GameObject) object);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Can't add gameObject to world!");
        }
    }

    @Override
    public void removeGameObject(Object object, World world) throws ModelException {
        try {
            world.removeGameObject((GameObject) object);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("GameObject does not exist in the given world");
        }
    }

    @Override
    public int[] getTargetTileCoordinate(World world) throws ModelException {
        return world.getTargetTileCoordinate();
    }

    @Override
    public void setTargetTileCoordinate(World world, int[] tileCoordinate) throws ModelException {
        world.setTargetTileCoordinate(tileCoordinate);
    }

    @Override
    public void startGame(World world) throws ModelException {
        try {
            world.setActiveGame(true);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Can't start the game!");
        }
    }

    @Override
    public boolean isGameOver(World world) throws ModelException {
        return world.isGameOver();
    }

    @Override
    public boolean didPlayerWin(World world) throws ModelException {
        return world.hasReachedTargetTile();
    }

    @Override
    public void advanceWorldTime(World world, double dt) {
        try {
            world.advanceTime(dt);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("not a valid amount of time");
        }
    }

// #####################################################################################################################

    @Override
    public void terminateGameObject(Object gameObject) throws ModelException {
        ((GameObject) gameObject).terminate();
    }

    @Override
    public boolean isTerminatedGameObject(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).isTerminated();
    }

    @Override
    public boolean isDeadGameObject(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).isDead() || ((GameObject) gameObject).getHitPoints() == 0;
    }

    @Override
    public double[] getActualPosition(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).getActualPosition();
    }

    @Override
    public void changeActualPosition(Object gameObject, double[] newPosition) throws ModelException {
        try {
            ((GameObject) gameObject).setActualPosition(newPosition);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Unvalid actual position");
        }
    }

    @Override
    public int[] getPixelPosition(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).getPixelPosition();
    }

    @Override
    public int getOrientation(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).getOrientation();
    }

    @Override
    public double[] getVelocity(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).getVelocity();
    }

    @Override
    public double[] getAcceleration(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).getAcceleration();
    }

    @Override
    public World getWorld(Object object) throws ModelException {
        if (object instanceof GameObject)
            return ((GameObject) object).getWorld();
        else return ((School) object).getWorld();
    }

    @Override
    public int getHitPoints(Object object) throws ModelException {
        return ((GameObject) object).getHitPoints();
    }

    @Override
    public Sprite[] getSprites(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).getSprites().clone();
    }

    @Override
    public Sprite getCurrentSprite(Object gameObject) throws ModelException {
        return ((GameObject) gameObject).getCurrentSprite();
    }

    @Override
    public void advanceTime(Object gameObject, double dt) throws ModelException {
        if (Double.isNaN(dt) || Double.isInfinite(dt))
            throw new ModelException("not a valid dt");
        ((GameObject) gameObject).advanceTime(dt);
    }
//######################################################################################################################

    @Override
    public Sneezewort createSneezewort(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
        try {
            return new Sneezewort(new int[]{pixelLeftX, pixelBottomY}, sprites);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Not valid sprite");
        }
    }

    @Override
    public Skullcab createSkullcab(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
        try {
            return new Skullcab(new int[]{pixelLeftX, pixelBottomY}, sprites);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Not valid sprite");
        }
    }

    @Override
    public Slime createSlime(long id, int pixelLeftX, int pixelBottomY, School school, Sprite... sprites) throws ModelException {
        try {
            return new Slime(id, new int[]{pixelLeftX, pixelBottomY}, school, sprites);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("not a valid Slime");
        }
    }

    @Override
    public long getIdentification(Slime slime) throws ModelException {
        return slime.getId();
    }

    @Override
    public void cleanAllSlimeIds() {
        SlimeInfo.allIds.clear();
        SlimeInfo.getSlimes().clear();
    }

    @Override
    public School createSchool(World world) throws ModelException {
        try {
            return new School(world);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("max 10 schools!");
        }
    }

    @Override
    public void terminateSchool(School school) throws ModelException {
        school.terminate();
    }

    @Override
    public Set<School> getAllSchools(World world) throws ModelException {
        return world.getSchools();
    }

    @Override
    public boolean hasAsSlime(School school, Slime slime) throws ModelException {
        return school.hasAsSlime(slime);
    }

    @Override
    public Collection<? extends Slime> getAllSlimes(School school) {
        return SlimeInfo.getSlimes().stream().filter(x -> x.getSchool() == school).collect(Collectors.toSet());
    }

    @Override
    public void addAsSlime(School school, Slime slime) throws ModelException {
       try {
           if (slime.getSchool() != null)
               throw new ModelException("cant add slime");
           slime.setSchool(school);
           school.addSlime(slime);
       } catch (IllegalArgumentException exc) {
           throw new ModelException("cant add slime");
       }
    }

    @Override
    public void removeAsSlime(School school, Slime slime) throws ModelException {
        try {
            school.removeSlime(slime);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Can't remove the given Slime");
        }
    }

    @Override
    public void switchSchool(School newSchool, Slime slime) throws ModelException {
        try {
            slime.switchSchool(newSchool);
        } catch (IllegalArgumentException exc) {
            throw new ModelException("Can't switch sochool");
        }
    }

    @Override
    public School getSchool(Slime slime) throws ModelException {
        return slime.getSchool();
    }

    @Override
    public Shark createShark(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
        return null;
    }
}
