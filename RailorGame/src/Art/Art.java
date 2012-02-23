package Art;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Art {
	public static Bitmap[][] bat = cut("/mob/enemy_bat_32.png", 32, 32);
	private static Bitmap[][] cut(String string, int w, int h) {
        return cut(string, w, h, 0, 0);
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
