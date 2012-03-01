package Entity;

import java.awt.image.BufferedImage;

import Level.Keys;
import Level.Location;

public class Player extends Entity {
	public Keys keys = new Keys();
	// public Screen screen;
	public int clientID = 0;
	public boolean updateLast = false;

	public Player(int x, int y, int id) {
		super(x, y);
		type = 1;
		dirType8 = true;
		sprite = Art.Art.BITMAP_ENTITY_PLAYER;
		moveSpeed = 7;
		this.clientID = id;
		this.id = id;
	}

	public void tick() {
		super.tick();
		// lastx = xpos;
		// lasty= ypos;
		keyUpdate();
	}

	public void setLocation(Location l) {
			lastx = xpos;
		//if(lastx != this.xpos)
			lasty = ypos;
		this.xpos = l.getX();
		this.ypos = l.getY();
		this.fx = l.getX();
		this.fy = l.getY();
		skipLastUpdate=true;

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
		return sprite.getSprite(currentFrame / frameSpeed,
				(dir >= sprite.maxH ? 0 : dir));
		}

	public void keyUpdate() {
		if (keys.isKeyDown(Keys.KEY_LEFT)) {
			fx = xpos - moveSpeed;
		}
		if (keys.isKeyDown(Keys.KEY_RIGHT)) {
			fx = xpos + moveSpeed;
		}
		if (keys.isKeyDown(Keys.KEY_UP)) {
			fy = ypos - moveSpeed;
		}
		if (keys.isKeyDown(Keys.KEY_DOWN)) {
			fy = ypos + moveSpeed;
		}
	}

	public int getClientId() {
		return clientID;
	}
	public void collision(Entity e){
		this.setFX(this.getX());
		this.setFY(this.getY());
		collided = true;
		//removeMe=true;
		//System.out.println("I collided with an entity");
	}
	public Location getPlayerLocation() {
		return new Location(clientID, (int)xpos, (int)ypos);

	}

}
