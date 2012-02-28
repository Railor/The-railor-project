package Art;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Art {
	public static String artLocation = "/";///WORKS FOR EDITOR
	//public static String artLocation = "/railorgame/res/"; /// WORKS FOR JAR FILE
	public static String artFileType = ".png";
	public static BitmapTile[] tileList = new BitmapTile[127];
	public static int imageLoaderCounter = 0;
	////////////////////////////////////////////////////////////
	//////////////////////////ENTITYS///////////////////////////
	////////////////////////////////////////////////////////////
	//HOW TO SETUP A NEW ENTITY PICTURE
	//NAME IT SOMETHING RELEVANT, BUT IT MUST START WITH BITMAP_ENTITY_
	//setupImage("herr_von_speck_sheet_32"), 26, 32);
	//SAVE ALL IMAGES WITH THE WIDTH AND HIEGHT OF EACH OBJECT WITHIN THE SHEET AS THE IMAGE NAME
	//Herr von speck is the file name, 26 is width,32 is the height, OF EACH OBJECT inside spreadsheet, NOT ENTIRE SIZE OF PICTURE
	//and thar u go
	//to set the picture of an entity, go to the class and in all constructors, set the sprite to this object
	//sprite = Art.Art.BITMAP_ENTITY_PLAYER;
	public static Bitmap BITMAP_ENTITY_PLAYER = new Bitmap(setupImage("herr_von_speck_sheet_26_32"), 26, 32);
	public static Bitmap BITMAP_ENTITY_BALL = new Bitmap(setupImage("entities"), 64, 64);
	public static Bitmap BITMAP_ENTITY_GANGAR = new Bitmap(setupImage("gangar_58_53"), 58, 53);
	////////////////////////////////////////////////////////////
	//////////////////////////TILES/////////////////////////////
	////////////////////////////////////////////////////////////
	//HOW TO SETUP A NEW TILE
	//NAME IT SOMETHING RELEVANT, BUT IT MUST START WITH BITMAP_TILE_
	//IF IT IS A BLEND OF TWO THINGS, PUT BOTH ( SUCH AS DIRT AND GRASS BLEND TILES
	//LETS EXPLAIN THIS
	//new BitmapTile("tiles",1,2,true);
	//TILES IS THE FILE NAME, it is stored within the res folder
	//it auto adds .png, SAVE ALL IMAGES AS PNG
	//The first 2 values are position, 0,0 being the top left, 4,0 would mean 5th tile to the right in the sheet, on the first row
	//the true/false statement is if the player can walk through the tile
	public static BitmapTile BITMAP_TILE_GRASS = new BitmapTile("tiles",1,2,true);
	public static BitmapTile BITMAP_TILE_DIRT = new BitmapTile("tiles",2,0,true);
	public static BitmapTile BITMAP_TILE_FOG = new BitmapTile("tiles",4,1,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS1 = new BitmapTile("tiles",0,1,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS2 = new BitmapTile("tiles",1,1,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS3 = new BitmapTile("tiles",2,1,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS4 = new BitmapTile("tiles",0,2,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS5 = new BitmapTile("tiles",2,2,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS6 = new BitmapTile("tiles",0,3,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS7 = new BitmapTile("tiles",1,3,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS8 = new BitmapTile("tiles",2,3,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS_CORNER1 = new BitmapTile("tiles",0,0,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS_CORNER2 = new BitmapTile("tiles",1,0,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS_CORNER3 = new BitmapTile("tiles",3,3,true);
	public static BitmapTile BITMAP_TILE_DIRT_GRASS_CORNER4 = new BitmapTile("tiles",4,3,true);
	public static BitmapTile BITMAP_TILE_GRASS_WALL1 = new BitmapTile("tiles",3,0,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL2 = new BitmapTile("tiles",4,0,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL3 = new BitmapTile("tiles",5,0,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL4 = new BitmapTile("tiles",3,1,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL5 = new BitmapTile("tiles",5,1,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL6 = new BitmapTile("tiles",3,2,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL7 = new BitmapTile("tiles",4,2,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL8 = new BitmapTile("tiles",5,2,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL9 = new BitmapTile("tiles",4,1,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL10 = new BitmapTile("tiles",5,3,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL11 = new BitmapTile("tiles",0,4,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL12 = new BitmapTile("tiles",1,4,false);
	public static BitmapTile BITMAP_TILE_GRASS_WALL13 = new BitmapTile("tiles",2,4,false);
	
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	public static BitmapTile getTileFromTileNumber(int x){
		if(tileList[x] != null)
		return tileList[x];
		//System.out.println("Could not find tile, using dirt instead lol");
		return null;
		
	}
	public static BufferedImage setupImage(String string) {
		try {
			
            BufferedImage bi = ImageIO.read(Art.class.getResource(artLocation + string + artFileType));
            
            return bi;
		
		} catch (IOException e) {
			System.out.println("cant find image? WHAT IS THIS!!!!!!!!!!");
            e.printStackTrace();
        }
		
		
		return null;
    }
	public static BufferedImage setupImage(String string,int x, int y, int w, int h) {
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
