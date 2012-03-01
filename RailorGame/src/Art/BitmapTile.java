package Art;

import java.awt.image.BufferedImage;

public class BitmapTile extends Bitmap {
	public boolean isPassable = true;
	
	public BitmapTile(String tiles, int x, int y,boolean pass){
		
		super(Art.setupImage(tiles,x*64,y*64,64,64),64,64);
		id = y * 12 + x;
		isPassable=pass;
		
		setup();
    }
	public void setup(){
		Art.tileList[id] = this;
	}
    public BufferedImage getSprite(){
    	return bi;
    }
}
