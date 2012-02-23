package Entity;

import java.awt.image.BufferedImage;

import Networking.TurnSynchronizer;



public class TestEntity extends Entity{
	public TestEntity(int x, int y){
		super(x,y);
		sprite = Art.Art.BITMAP_ENTITY_BALL;
	}
	public void tick(){
		super.tick();
		fx+=TurnSynchronizer.synchedRandom.nextInt(15)-7;
		fy+=TurnSynchronizer.synchedRandom.nextInt(15)-7;
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

}
