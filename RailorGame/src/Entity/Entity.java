package Entity;


import java.awt.image.BufferedImage;

import Art.Bitmap;
import Level.Location;
import Networking.TurnSynchronizer;

public class Entity {
	public int xpos = 0;
	public int ypos = 0;
	public int lastx;
	public int lasty;
	protected int fx = 25;
	protected int fy = 25;
	protected int width = 45;
	protected int height = 45;
	protected int radius = width;
	private boolean removeMe = false;
	public boolean collided = false;
	public int moveSpeed = 5;
	int id;
	public int type = 0;
	public int currentFrame = 0;
	public int frameSpeed = 6;
	public int dir = 0;
	public boolean dirType = false;
	
	Bitmap sprite;
	
	public Entity(int x, int y){
		xpos = x;
		ypos = y;
		fx = xpos;
		fy = ypos;
		sprite = Art.Art.BITMAP_ENTITY_BALL;
	}
	public BufferedImage getSprite(){
		
		return sprite.getSprite(currentFrame / frameSpeed, (dir >= sprite.maxH ? 0 : dir));
	}
	
	public void setLocation(Location l){
		//if(lastx != xpos)
			//lastx = xpos;
			//if(lasty != ypos)
		//	lasty= ypos;
		this.xpos=l.getX();
		this.ypos=l.getY();
		this.fx=l.getX();
		this.fy=l.getY();
		
	}
	public Location getLocation(){
		return new Location(id,xpos,ypos);
		
	}
	public void tick(){
		collided= false;
		currentFrame++;
		if(currentFrame/frameSpeed>=sprite.maxW )
			currentFrame=0;
		//if(lastx != xpos)
		lastx = xpos;
		//if(lasty != ypos)
		lasty= ypos;
		

	}
	public boolean getRemoveMe(){
		return removeMe;
	}
	public void collision(Entity e){
		this.setFX(this.getX());
		this.setFY(this.getY());
		collided = true;
		//System.out.println("I collided with an entity");
	}
	public int getRadius(){
		return radius;
	}
	public int getX() {
		// TODO Auto-generated method stub
		return xpos;
	}
	public int getY() {
		// TODO Auto-generated method stub
		return ypos;
	}
	public int getId(){
		return id;
	}
	public void setFX(int x){
		fx = x;
	}
	public void setFY(int y){
		fy = y;
	}
	public void setX(int x){
		 this.xpos = x;
	}
	public void setId(int x){
		id = x;
	}
	public void setY(int y){
		 this.ypos = y;
	}
	public int getFX(){
		return fx;
	}
	public int getFY(){
		return fy;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}	
	
}
