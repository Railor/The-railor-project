package Level;

import Art.BitmapTile;

public class Tile {
	//public BufferedImage tile;
	public int id = 0;
	//public boolean  isPassable = true;
	//public Tile(BitmapTile TILE_TYPE){
	//	tile=TILE_TYPE.getSprite();
	//	isPassable=TILE_TYPE.isPassable;
	//}
	public Tile(BitmapTile TILE_TYPE){
		id=TILE_TYPE.id;
		//tile=TILE_TYPE.getSprite();
		//isPassable=TILE_TYPE.isPassable;
	}
}
