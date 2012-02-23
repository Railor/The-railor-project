package Level;

import java.awt.image.BufferedImage;

import Art.Bitmap;
import Art.BitmapTile;

public class Tile {
	public BufferedImage tile;
	boolean  isPassable = true;
	public Tile(BitmapTile TILE_TYPE){
		tile=TILE_TYPE.getSprite();
		isPassable=TILE_TYPE.isPassable;
	}
}
