package Entity;

import java.awt.image.BufferedImage;

import Art.Bitmap;
import Level.Keys;
import Level.Location;
import Level.RailorComponent.KeyHandler;
import Level.RailorComponent.MouseHandler;
import Level.Screen;

public class Player extends Entity{
	public Keys keys = new Keys();
	//public Screen screen;
	public int clientID = 0;
	public Player(Bitmap b,int x, int y){
		super(x, y);
		type=1;
		dirType=true;
	}
	public void tick(){
		super.tick();
		keyUpdate();
		
	}
public BufferedImage getSprite(){
		int dx = xpos - lastx,dy = ypos - lasty ;
		if(dx < 0){//IF WE ARE MOVING LEFT ON X
			if(dy < 0){//IF WE ARE MOVING UP ON Y
				dir = 3;
			}else if(dy == 0){//IF WE ARE NOT MOVING ON Y
				dir = 2;
			}else if(dy > 0){//IF WE ARE MOVING DOWN ON Y
				dir = 1;
			
			}
		}else if(dx == 0){//IF WE ARE NOT MOVING ON X AXIS
			if(dy < 0){//IF WE ARE MOVING UP ON Y
				dir = 4;
			}else if(dy == 0){//IF WE ARE NOT MOVING ON Y
				
			}else if(dy > 0){//IF WE ARE MOVING DOWN ON Y
				dir = 0;
			}
		}else if(dx > 0){//IF WE ARE MOVING RIGHT ON X
			if(dy < 0){//IF WE ARE MOVING UP ON Y
				dir = 5;
			}else if(dy == 0){//IF WE ARE NOT MOVING ON Y
				dir = 6;
			}else if(dy > 0){//IF WE ARE MOVING DOWN ON Y
				dir = 7;
			}
		}
		//if(xpos > 200)
		//System.out.println(dir + "lastx: " + lastx + "xpos: " + xpos);
		return sprite.getSprite(currentFrame / frameSpeed, (dir >= sprite.maxH ? 0 : dir));
	}
	public void keyUpdate(){
		if(keys.isKeyDown(Keys.KEY_LEFT)){
			fx = xpos-5;
		}
		if(keys.isKeyDown(Keys.KEY_RIGHT)){
			fx = xpos+5;
		}
		if(keys.isKeyDown(Keys.KEY_UP)){
			fy = ypos-5;
		}
		if(keys.isKeyDown(Keys.KEY_DOWN)){
			fy = ypos+5;
		}
	}
	public int getClientId(){
		return clientID;
	}
	public Location getPlayerLocation(){
		return new Location(clientID,xpos,ypos);
		
	}
	
	
	
}
