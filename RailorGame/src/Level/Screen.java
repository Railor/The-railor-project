package Level;

import java.awt.Graphics;
import java.util.ArrayList;

import mainGame.GameManager;
import mainGame.ProgramManager;

import Entity.Entity;
import Entity.Player;
import MapEditor.EditorManager;

public class Screen {
	public int w, h, tileSize;
	public int boundaryX = 0, boundaryY = 0;
	public int screenX = 0, screenY = 0;
	public Entity owner = null;
	GameManager gm;
	EditorManager em;
	public Screen(GameManager gameManager, int w, int h) {

		this.gm = gameManager;
		this.w = w;
		this.h = h;
		screenX = 0;
		boundaryX = gameManager.level.width * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_WIDTH;
		boundaryY = gameManager.level.height * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_HEIGHT;
	}

	public Screen(EditorManager editorManager, int w, int h) {

		this.em = editorManager;
		this.w = w;
		this.h = h;
		screenX = 0;
		boundaryX = em.level.width * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_WIDTH;
		boundaryY = em.level.height * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_HEIGHT;
	}

	public void tick() {
		if (owner != null) {
			setScreenOnEntity(owner);
			// System.out.println("ownerx: " + owner.getX() + "| screenx: " +
			// screenX + "| screeny: " + screenY);
		} else {

		}
	}

	public void addScreenX(int x) {
		boundaryX = em.level.width * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_WIDTH;
		boundaryY = em.level.height * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_HEIGHT;
		if (screenX + x < boundaryX && screenX + x > 0)
			screenX += x;
	}

	public void addScreenY(int y) {
		boundaryX = em.level.width * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_WIDTH;
		boundaryY = em.level.height * GameManager.GAME_TILE_SIZE
				- ProgramManager.SCREEN_HEIGHT;
		if (screenY + y < boundaryY && screenY + y > -4)
			screenY += y;
	}

	public void setScreenOnEntity(Entity x) {
		screenX = (int) x.getX() - w / 2;
		if (screenX > boundaryX) {
			screenX = boundaryX;

		} else if (screenX < 0) {
			screenX = 0;
		}
		screenY = (int) x.getY() - h / 2;
		if (screenY > boundaryY) {
			screenY = boundaryY;

		} else if (screenY < 0) {
			screenY = 0;
		}
	}

	public void drawLevelMap(Level l, Graphics g) {
		Tile map[][] = l.getLevelMap();
		Tile backmap[][] = l.getLevelMap();
		tileSize = GameManager.GAME_TILE_SIZE;

		int offsetX = screenX / GameManager.GAME_TILE_SIZE;
		int offsetY = screenY / GameManager.GAME_TILE_SIZE;
		if (offsetX < 0)
			offsetX = 0;
		if (offsetY < 0)
			offsetY = 0;
		for (int x = offsetX; x < offsetX + w / GameManager.GAME_TILE_SIZE + 2
				&& x < l.width; x++) {
			for (int y = offsetY; y < offsetY + h / GameManager.GAME_TILE_SIZE
					+ 3
					&& y < l.height; y++) {
				// g.drawImage(rc.tiles[map[x][y].tile],x*tileSize-screenX,y*tileSize-screenY,null);
				//bi.setData(Art.Art.getTileFromTileNumber(map[x][y].id)
					//	.getSprite().getData());
				//g.drawImage(Art.Art.getTileFromTileNumber(map[x][y].id)
					//	.getSprite(), x * tileSize - screenX, y * tileSize
					//	- screenY, null);
				g.drawImage(Art.Art.getTileFromTileNumber(backmap[x][y].id)
						.getSprite(), x * tileSize - screenX, y * tileSize
						- screenY, null);
			}
		}
	}
		public void drawLevelMap(Level l, Graphics g,boolean t) {
			Tile map[][] = l.getLevelMap();
			Tile backmap[][] = l.backMap;
			tileSize = GameManager.GAME_TILE_SIZE;

			int offsetX = screenX / GameManager.GAME_TILE_SIZE;
			int offsetY = screenY / GameManager.GAME_TILE_SIZE;
			if (offsetX < 0)
				offsetX = 0;
			if (offsetY < 0)
				offsetY = 0;
			for (int x = offsetX; x < offsetX + w / GameManager.GAME_TILE_SIZE + 2
					&& x < l.width; x++) {
				for (int y = offsetY; y < offsetY + h / GameManager.GAME_TILE_SIZE
						+ 3
						&& y < l.height; y++) {
					// g.drawImage(rc.tiles[map[x][y].tile],x*tileSize-screenX,y*tileSize-screenY,null);
					//bi.setData(Art.Art.getTileFromTileNumber(map[x][y].id)
						//	.getSprite().getData());
					
					
						if(backmap[x][y].id != 0)
							g.drawImage(Art.Art.getTileFromTileNumber(backmap[x][y].id)
									.getSprite(), x * tileSize - screenX, y * tileSize
									- screenY, null);
								g.drawImage(Art.Art.getTileFromTileNumber(map[x][y].id)
										.getSprite(), x * tileSize - screenX, y * tileSize
										- screenY, null);
							if(x == 0 && y == 0)
							System.out.println("map id = " + map[x][y].id + "backmap id = " + backmap[x][y].id);
							/*
							 * 						g.drawImage(Art.Art.getTileFromTileNumber(map[x][y].id)
								.getSprite(), x * tileSize - screenX, y * tileSize
								- screenY, null);
						
							g.drawImage(Art.Art.getTileFromTileNumber(backmap[x][y].id)
									.getSprite(), x * tileSize - screenX, y * tileSize
									- screenY, null);
							 */
					
				}
			}
		//g.drawImage(bi, 0,0, null);
		

	}

	public void drawEntities(ArrayList<Entity> entities, Graphics g, double d) {
		int fx = 0, fy = 0;

		for (Entity e : entities) {
			fx = (int) ((e.getX() - e.lastx) * d);
			fy = (int) ((e.getFY() - e.lasty) * d);
			if (e instanceof Player) {

				// if(owner == e){
				// System.out.println("Player x: " + e.getX() + " lastX: " +
				// e.lastx);

				// if(screenX>0 && screenX < boundaryX)
				fx = 0;
				// if(screenY>0 && screenY < boundaryY)
				fy = 0;
				// }else{
				// }
			}
			g.drawImage(e.getSprite(), (int) (e.getX() + fx - screenX),
					(int) (e.getY() + fy - screenY), e.getWidth(),
					e.getHeight(), null);
		}

	}
}
