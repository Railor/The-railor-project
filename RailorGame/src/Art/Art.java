package Art;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Art {
	public static String artLocation = "/";///WORKS FOR EDITOR
	//public static String artLocation = "/railorgame/res/"; /// WORKS FOR JAR FILE
	public static String artFileType = ".png";
	
	////////////////////////////////////////////////////////////
	//////////////////////////ENTITYS///////////////////////////
	////////////////////////////////////////////////////////////
	public static Bitmap BITMAP_ENTITY_PLAYER = new Bitmap(setupImage("herr_von_speck_sheet_32"), 26, 32);
	public static Bitmap BITMAP_ENTITY_BALL = new Bitmap(setupImage("entities"), 64, 64);
	public static Bitmap BITMAP_ENTITY_GANGAR = new Bitmap(setupImage("gangar_58_53"), 58, 53);
	////////////////////////////////////////////////////////////
	//////////////////////////TILES/////////////////////////////
	////////////////////////////////////////////////////////////
	public static BitmapTile BITMAP_TILE_GRASS = new BitmapTile(setupImage("tiles",0,0,64,64),64,64,true);
	public static BitmapTile BITMAP_TILE_GRASS_WALL = new BitmapTile(setupImage("tiles",64,0,64,64),64,64,false);
	public static BitmapTile BITMAP_TILE_DIRT = new BitmapTile(setupImage("tiles",128,0,64,64),64,64, true);
	public static BitmapTile BITMAP_TILE_DIRT_WALL = new BitmapTile(setupImage("tiles",0,64,64,64),64,64,false);
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	private static BufferedImage setupImage(String string) {
		try {
            BufferedImage bi = ImageIO.read(Art.class.getResource(artLocation + string + artFileType));
            return bi;
		
		} catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
		
		System.out.println("cant find image? WHAT IS THIS!!!!!!!!!!");
		return null;
    }
	private static BufferedImage setupImage(String string,int x, int y, int w, int h) {
		try {
			//ImageIcon flagIcon = new ImageIcon(getClass().getResource("images/flag2.gif"));
			BufferedImage bi = ImageIO.read(Art.class.getResource(artLocation + string + artFileType));
            //BufferedImage bi = ImageIO.read(new File(artLocation + string + artFileType));
			
            bi = bi.getSubimage(x,y,w,h);
            return bi;
		
		} catch (IOException e) {
            e.printStackTrace();
        }
		return null;
    }
}
