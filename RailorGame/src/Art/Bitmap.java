package Art;

import java.awt.image.BufferedImage;

public class Bitmap {
    public int w, h;
    public int maxW;
    public int maxH;
    public BufferedImage bi;
    public boolean dirType8 = true;
    public Bitmap(BufferedImage bi, int w, int h){
    	this.bi=bi;
    	this.w = w;
    	this.h=h;
    	maxW= bi.getWidth()/w;
    	maxH = bi.getHeight()/h;
    }
    public BufferedImage getSprite(int wc, int hc){
    	if(wc < maxW && hc < maxH)
    	return bi.getSubimage(w*wc, h*hc, w, h);
    	return bi;
    }
    
    
    
}
