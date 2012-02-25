package Mob;

import java.awt.image.BufferedImage;

import Art.Bitmap;
import Entity.Entity;
import Networking.TurnSynchronizer;

public class Mob extends Entity{
	public int dirAngle = 90;
	double maxX,maxY,minX = 9001,minY = 9002;
	public Mob(int x, int y) {
		super(x, y);
		sprite=Art.Art.BITMAP_ENTITY_BALL;
	}

	public void tick() {
		super.tick();
		dirAngle+=5;
			dirAngle%=360;
			//System.out.println(dirAngle);
		move();
	}
	public void move(){
		setFuturePositionFromAngle();
	}
	public void setFuturePositionFromAngle(){
		

		//fx = (int) (xpos + moveSpeed * Math.cos(Math.toRadians(dirAngle)));
		//fy = (int) (ypos + moveSpeed * Math.sin(Math.toRadians(dirAngle)));
		
		
		
		//
		double radians;
		radians = Math.toRadians(dirAngle);
		//System.out.println(moveSpeed * Math.cos(radians));
		
		fx = (xpos + moveSpeed * Math.sin(radians));
		
		fy = (ypos - moveSpeed * Math.cos(radians));
		if(Math.min(fy, minY) < minY){
			minY = Math.min(fy, minY);
			System.out.println("miny: " + minY);
		}
		if(Math.min(fx, minX) < minX){
			minX = Math.min(fx, minX);
			System.out.println("minx:" + minX );
		}
		
			
		maxY = Math.max(fy, maxY);
		
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
		return Art.Art.BITMAP_ENTITY_BALL.getSprite(currentFrame / frameSpeed,(dir >= Art.Art.BITMAP_ENTITY_BALL.maxH ? 0 : dir));
	}

}
