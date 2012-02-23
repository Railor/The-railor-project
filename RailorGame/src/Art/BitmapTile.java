package Art;

import java.awt.image.BufferedImage;

public class BitmapTile extends Bitmap {
	public boolean isPassable = true;
	public BitmapTile(BufferedImage bi, int w, int h,boolean pass){
		super(bi,w,h);
		isPassable=pass;
    }
    public BufferedImage getSprite(){
    	return bi;
    }
}
