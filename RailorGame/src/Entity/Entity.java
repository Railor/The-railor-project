package Entity;


import java.awt.image.BufferedImage;


import Art.Bitmap;
import Level.Location;

public class Entity {
	public double xpos = 0.0;
	public double ypos = 0.0;
	public double lastx;
	public double lasty;
	protected double fx = 25;
	protected double fy = 25;
	protected int width = 45;
	protected int height = 45;
	protected int radius = width;
	public boolean removeMe = false;
	public boolean collided = false;
	public int MaxMoveSpeed = 500;
	public int moveSpeed = 3;
	protected int id;
	public int type = 0;
	public int currentFrame = 0;
	public int frameSpeed = 6;
	public int dir = 0;
	public boolean dirType8 = false;
	public boolean skipLastUpdate = false;
	boolean isMoving = false;
	protected Bitmap sprite;
	protected int dirAngle = 0;
	public Entity(){
		
	}
	public Entity(int x, int y){
		xpos = x;
		ypos = y;
		fx = xpos;
		fy = ypos;
		
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
		return new Location(id,(int)xpos,(int)ypos);
	}
	public void tick(){
		collided= false;
		currentFrame++;
		if(currentFrame/frameSpeed>=sprite.maxW )
			currentFrame=0;
		//if(lastx != xpos)
		if(!skipLastUpdate){
		lastx = xpos;
		//if(lasty != ypos)
		lasty= ypos;
		}else{
			skipLastUpdate=false;
		}
		

	}
	public boolean getRemoveMe(){
		return removeMe;
	}
	public void collision(Entity e){
		this.setFX(this.getX());
		this.setFY(this.getY());
		//collided = true;
		//removeMe=true;
		//System.out.println("I collided with an entity");
	}
	public int getDirAngle(){
		return dirAngle;
	}
	public void setDirAngle(int angle){
		dirAngle = angle;
	}
	public int getRadius(){
		return radius;
	}
	public double getX() {
		// TODO Auto-generated method stub
		return xpos;
	}
	public double getY() {
		// TODO Auto-generated method stub
		return ypos;
	}
	public int getId(){
		return id;
	}
	public void setFX(double x){
		fx = x;
	}
	public void setFY(double y){
		fy = y;
	}
	public void setX(double x){
		 this.xpos = x;
	}
	public void setId(int x){
		id = x;
	}
	public void setY(double y){
		 this.ypos = y;
	}
	public double getFX(){
		return fx;
	}
	public double getFY(){
		return fy;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}	
	
}
