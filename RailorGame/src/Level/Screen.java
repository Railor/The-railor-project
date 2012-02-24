package Level;

import java.awt.Graphics;
import java.util.ArrayList;

import mainGame.GameManager;
import mainGame.ProgramManager;

import Entity.Entity;

public class Screen {
	public int w,h,tileSize;
	public int boundaryX=0,boundaryY=0;
	public int screenX = 0,screenY = 0;
	public Entity owner = null;
	GameManager gm;
	public Screen(GameManager gameManager, int w, int h){
		
		this.gm = gameManager;
		this.w = w;
		this.h = h;
		screenX =0;
		boundaryX = gameManager.level.width*GameManager.GAME_TILE_SIZE - ProgramManager.SCREEN_WIDTH;
		boundaryY = gameManager.level.height*GameManager.GAME_TILE_SIZE - ProgramManager.SCREEN_HEIGHT;
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
		tileSize =GameManager.GAME_TILE_SIZE;
		int offsetX = screenX / GameManager.GAME_TILE_SIZE;
		int offsetY = screenY / GameManager.GAME_TILE_SIZE;
		if(offsetX < 0)
			offsetX=0;
		if(offsetY < 0)
			offsetY=0;
		for(int x = offsetX;x<l.width;x++){
			for(int y = offsetY;y<l.height;y++){
				//g.drawImage(rc.tiles[map[x][y].tile],x*tileSize-screenX,y*tileSize-screenY,null);
				g.drawImage(map[x][y].tile,x*tileSize-screenX,y*tileSize-screenY,null);
			}
		}
	}
	public void drawEntities(ArrayList<Entity> entities, Graphics g) {
		for(Entity e : entities){
			g.drawImage(e.getSprite(),e.getX() - screenX,e.getY() - screenY,e.getWidth(),e.getHeight(),null);
		}
		
	}
	
}
