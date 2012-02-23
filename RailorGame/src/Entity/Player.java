package Entity;

import Level.Keys;
import Level.Location;
import Level.RailorComponent.KeyHandler;
import Level.RailorComponent.MouseHandler;
import Level.Screen;

public class Player extends Entity{
	public Keys keys = new Keys();
	//public Screen screen;
	public int clientID = 0;
	public Player(int x, int y){
		super(x, y);
		type=1;
	}
	public void tick(){
		keyUpdate();
		
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
