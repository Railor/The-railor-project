package Level;

import java.awt.Graphics;
import java.util.ArrayList;

import Entity.Entity;

public class Screen {
	public int w,h,tileSize;
	public int boundaryX=0,boundaryY=0;
	public int screenX = 0,screenY = 0;
	public Entity owner = null;
	RailorComponent rc;
	public Screen(RailorComponent rc, int w, int h){
		
		this.rc = rc;
		this.w = w;
		this.h = h;
		screenX =0;
		boundaryX = rc.level.width*RailorComponent.TILE_SIZE - RailorComponent.GAME_WIDTH;
		boundaryY = rc.level.height*RailorComponent.TILE_SIZE - RailorComponent.GAME_HEIGHT;
	}
	public void tick(){
		if(owner!= null){
			setScreenOnEntity(owner);
			//System.out.println("ownerx: " + owner.getX() + "| screenx: " + screenX + "| screeny: " + screenY);
		}
	}
	public void addScreenX(int x){
		if(screenX+x<boundaryX && screenX+x>0)
			screenX+=x;
	}
	public void addScreenY(int y){
		if(screenY+y<boundaryY && screenY+y>-4)
			screenY+=y;
	}
	public void setScreenOnEntity(Entity x){
		screenX = x.getX() - w/2;
		if(screenX>boundaryX){
			screenX=boundaryX;
			
		}else if(screenX<0){
			screenX=0;
		}
		screenY = x.getY()- h/2;
		if(screenY>boundaryY){
			screenY=boundaryY;
			
		}else if(screenY<0){
			screenY=0;
		}
	}
	public void drawLevelMap(Level l, Graphics g){
		Tile map[][] = l.getLevelMap();
		tileSize =RailorComponent.TILE_SIZE;
		int offsetX = screenX / RailorComponent.TILE_SIZE;
		int offsetY = screenY / RailorComponent.TILE_SIZE;
		if(offsetX < 0)
			offsetX=0;
		if(offsetY < 0)
			offsetY=0;
		for(int x = offsetX;x<l.width;x++){
			for(int y = offsetY;y<l.height;y++){
				g.drawImage(rc.tiles[map[x][y].tile],x*tileSize-screenX,y*tileSize-screenY,null);
			}
		}
	}
	public void drawEntities(ArrayList<Entity> entities, Graphics g) {
		for(Entity e : entities){
			g.drawImage(rc.entityGraphics[0],e.getX() - screenX,e.getY() - screenY,e.getWidth(),e.getHeight(),null);
		}
		
	}
	
}
