package Entity;


import Level.Location;
import Networking.TurnSynchronizer;

public class Entity {
	protected int xpos = 0;
	protected int ypos = 0;
	protected int fx = 25;
	protected int fy = 25;
	protected int width = 50;
	protected int height = 50;
	protected int radius = width;
	private boolean removeMe = false;
	public boolean collided = false;
	int id;
	public int type = 0;
	public Entity(){
		
	}
	public Entity(int x, int y){
		xpos = x;
		ypos = y;
		fx = xpos;
		fy = ypos;
	}
	public void setLocation(Location l){
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
		fx+=TurnSynchronizer.synchedRandom.nextInt(15)-7;
		fy+=TurnSynchronizer.synchedRandom.nextInt(15)-7;
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
