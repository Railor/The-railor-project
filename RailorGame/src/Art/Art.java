package Art;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import mainGame.Main;
import mainGame.ProgramManager;

public class Art {
	public static String artLocation = "/res/";
	public static String artFileType = ".png";
	
	////////////////////////////////////////////////////////////
	//////////////////////////ENTITYS///////////////////////////
	////////////////////////////////////////////////////////////
	public static Bitmap BITMAP_ENTITY_PLAYER = new Bitmap(setupImage("herr_von_speck_sheet_32"), 26, 32);
	public static Bitmap BITMAP_ENTITY_BALL = new Bitmap(setupImage("entities"), 64, 64);
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
    private static Bitmap[][] cut(String string, int w, int h, int bx, int by) {
    	/*
        try {
            BufferedImage bi = ImageIO.read(MojamComponent.class.getResource(string));

            int xTiles = (bi.getWidth() - bx) / w;
            int yTiles = (bi.getHeight() - by) / h;

            Bitmap[][] result = new Bitmap[xTiles][yTiles];

            for (int x = 0; x < xTiles; x++) {
                for (int y = 0; y < yTiles; y++) {
                    result[x][y] = new Bitmap(w, h);
                    bi.getRGB(bx + x * w, by + y * h, w, h, result[x][y].pixels, 0, w);
                }
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        return null;
        
    }
    
}
