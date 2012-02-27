package Mob;

import java.awt.image.BufferedImage;

import Entity.Entity;

public class Mob extends Entity{

	public boolean isMoving = true;
	public boolean requireUpdate = false;
	public Mob(int id){
		this.setId(id);
		sprite=Art.Art.BITMAP_ENTITY_GANGAR;
	}
	public Mob(int x, int y){
		super(x, y);
		sprite=Art.Art.BITMAP_ENTITY_GANGAR;
	}
	public Mob(int x, int y, int id) {
		super(x, y);
		
		this.id = id;
		sprite=Art.Art.BITMAP_ENTITY_GANGAR;
	}

	public void tick() {
		super.tick();
		//dirAngle+=3;
			dirAngle%=360;
			//System.out.println(dirAngle);
		move();
	}
	public void tickDecisions(){
		
	}
	public void move(){
		if(isMoving)
		setFuturePositionFromAngle();
	}
	public void setFuturePositionFromAngle(){
		double radians;
		radians = Math.toRadians(dirAngle);
		fx = (xpos + moveSpeed * Math.sin(radians));
		fy = (ypos - moveSpeed * Math.cos(radians));	
		//System.out.println("minx:" + minX + "miny: " + minY);
	}

	public BufferedImage getSprite() {
		double dx = xpos - lastx, dy = ypos - lasty;
		if (dx < 0) {// IF WE ARE MOVING LEFT ON X
			if (dy < 0) {// IF WE ARE MOVING UP ON Y
				dir = 3;
			} else if (dy == 0) {// IF WE ARE NOT MOVING ON Y
				dir = 2;
			} else if (dy > 0) {// IF WE ARE MOVING DOWN ON Y
				dir = 1;

			}
		} else if (dx == 0) {// IF WE ARE NOT MOVING ON X AXIS
			if (dy < 0) {// IF WE ARE MOVING UP ON Y
				dir = 4;
			} else if (dy == 0) {// IF WE ARE NOT MOVING ON Y

			} else if (dy > 0) {// IF WE ARE MOVING DOWN ON Y
				dir = 0;
			}
		} else if (dx > 0) {// IF WE ARE MOVING RIGHT ON X
			if (dy < 0) {// IF WE ARE MOVING UP ON Y
				dir = 5;
			} else if (dy == 0) {// IF WE ARE NOT MOVING ON Y
				dir = 6;
			} else if (dy > 0) {// IF WE ARE MOVING DOWN ON Y
				dir = 7;
			}
		}
		return Art.Art.BITMAP_ENTITY_GANGAR.getSprite(currentFrame / frameSpeed,(dir >= Art.Art.BITMAP_ENTITY_GANGAR.maxH ? 0 : dir));
	}

}
