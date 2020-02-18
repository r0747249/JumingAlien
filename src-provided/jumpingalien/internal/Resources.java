package jumpingalien.internal;

import java.util.stream.IntStream;

import jumpingalien.internal.gui.sprites.ImageSprite;

public class Resources {

	public static final String PLANT_LEFT_FILENAME = "levels/items/plantPurple.png";
	public static final ImageSprite PLANT_SPRITE_LEFT = ImageSprite
			.createSprite(PLANT_LEFT_FILENAME);
	public static final ImageSprite PLANT_SPRITE_RIGHT = ImageSprite
			.createHFlippedSprite(PLANT_LEFT_FILENAME);

	public static final ImageSprite[] NUMBER_SPRITES = IntStream
			.rangeClosed(0, 9)
			.mapToObj(n -> String.format("levels/hud/hud_%d.png", n))
			.map(ImageSprite::createSprite)
			.toArray(size -> new ImageSprite[size]);
	public static final ImageSprite HEALTH_FULL = ImageSprite
			.createSprite("levels/hud/hud_heartFull.png");
	public static final ImageSprite HEALTH_HALF = ImageSprite
			.createSprite("levels/hud/hud_heartHalf.png");
	public static final ImageSprite HEALTH_EMPTY = ImageSprite
			.createSprite("levels/hud/hud_heartEmpty.png");

}
