package Level;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import mainGame.GameManager;

import Entity.Entity;
import Entity.Player;
import MapEditor.EditorManager;
import Mob.Mob;

public class Level {
	public int width, height;
	public Tile[][] levelMap;
	public Tile[][] backMap;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Player> players = new ArrayList<Player>();
	GameManager gm;
	EditorManager em;
	int currentEntityId = 100;

	public Level(int w, int h, GameManager gameManager) {
		width = w;
		height = h;
		gm = gameManager;
		levelMap = new Tile[w][h];
		backMap = new Tile[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				levelMap[x][y] = new Tile(Art.Art.BITMAP_TILE_GRASS);
			}
		}
		if (gm.pm.isServer) {

		}
	}
	public Level(String mapFile, GameManager gameManager) {
		LevelProperties l = loadMap(mapFile);
		width = l.width;
		height = l.height;
		gm = gameManager;
		levelMap = l.tiles;
		backMap = l.backTiles;
	}
	public Level(int w, int h, EditorManager editorManager) {
		width = w;
		height = h;
		em = editorManager;
		levelMap = new Tile[w][h];
		backMap = new Tile[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				//System.out.println(x + "<x y>" + y);
				levelMap[x][y] = new Tile(Art.Art.BITMAP_TILE_GRASS);
				backMap[x][y] = new Tile(Art.Art.BITMAP_TILE_TRANS);
			}
		}
	}
	public Level(String mapFile, EditorManager editorManager) {
		LevelProperties l = loadMap(mapFile);
		width = l.width;
		height = l.height;
		em = editorManager;
		levelMap = l.tiles;
		backMap = l.backTiles;
		System.out.println("finished level");
		//for (int x = 0; x < width; x++) {
			//for (int y = 0; y < height; y++) {
				//levelMap[x][y] = new Tile(Art.Art.BITMAP_TILE_GRASS_WALL);
			//}
		//}
	}
	public LevelProperties loadMap(String t) {
		LevelProperties lv = new LevelProperties();
		lv.name = t;
		System.out.println("READING FILE");
		FileReader input;
		String map = "hearsay";
		String map1 = null;
		try {
			input = new FileReader(t);
			BufferedReader bufRead = new BufferedReader(input);
			if(!bufRead.ready()){
				System.out.println("COULD NOT");
			}
			map = bufRead.readLine();
			map1+=map;
			while (map != null) {
				map = bufRead.readLine();
				map1+=map;
			}
			//System.out.println("BITCH" + map1);
			bufRead.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to read file");
			e.printStackTrace();
		}
		int width = 0,height = 0;
		String delims = "[,]";
		//System.out.println(map1);
		String[] tokens = map1.split(delims);
		int breaker = 0;
		for(int x = 0; x<tokens.length;x++){
			//System.out.println(tokens[x]);
			if(tokens[x].compareTo("SIZEX")==0){
				x++;
				width = Integer.parseInt(tokens[x]);
				//System.out.println(tokens[x] + "SIZEX");
			}else if(tokens[x].compareTo("SIZEY")==0){
				x++;
				height = Integer.parseInt(tokens[x]);
				breaker = x+1;
				//break;
			}
		}
		lv.tiles = new Tile[width][height];
		lv.backTiles = new Tile[width][height];
		int ix = 0,iy = 0;
		//System.out.println("WIDTH: " + width + "| HEIGHT: " + height);
		lv.width = width;
		lv.height = height;
		boolean backs = false;
		for(int x = breaker; x<tokens.length-1;x++){
			if(tokens[x].compareTo("backtile")==0){
				ix=0;
				iy=0;
				backs=true;
				x++;
				
			}
			
			//System.out.println(ix + "IX - IY" + iy);
			if(ix < width && iy < height && !backs){
				//System.out.println((tokens[x]));
			lv.tiles[iy][ix] = new Tile(Art.Art.getTileFromTileNumber(Integer.parseInt(tokens[x])));
				//lv.tiles[ix][iy] = new Tile(Art.Art.BITMAP_TILE_GRASS_WALL);
			}
			if(backs){
				lv.backTiles[iy][ix] = new Tile(Art.Art.getTileFromTileNumber(Integer.parseInt(tokens[x])));
			}
			if(ix<(width-1)){
				ix++;
			}else{
				iy++;
				ix=0;
			}
		}
		return lv;
	}

	

	public void tick() {
		if (gm.pm.isClient) {
			// System.out.println(gm.myPlayer);
			// System.out.println(entities.size() +
			// "FIRST CLIENT ENTITY SIZE LIST");
			if (gm.myPlayer != null) {
				for (Entity c : getEntitiesNotInRange(gm.pm.updateRange,
						gm.pm.client.clientId)) {
					if (c instanceof Mob)
						entities.remove(c);
				}
			}
			if (gm.screen.owner == null) {
				for (Player k : gm.level.players) {

					if (k.clientID == gm.pm.client.clientId) {

						gm.myPlayer = k;
						gm.screen.owner = k;
					}
				}
			}

			// System.out.println(entities.size() +
			// "SECOND CLIENT ENTITY SIZE LIST");
		}
		entitiesTick();
		// System.out.println("current tick is" + gameTick + rc.isServer);
	}

	public boolean setLocationById(Location l) {
		if (l.getID() < 0) {
			int lid = l.getID() * -1;
			Player p = getPlayerById(lid);
			if (p != null) {
				p.setX(l.getX());
				p.setY(l.getY());
				p.setFX(l.getX());
				p.setFY(l.getY());
				return true;
			}
		} else {
			Entity e = getEntityById(l.getID());
			if (e != null) {
				e.setX(l.getX());
				e.setY(l.getY());
				e.setFX(l.getX());
				e.setFY(l.getY());
				return true;
			}
		}
		return false;
	}

	public Entity getEntityById(int i) {
		for (Entity e : entities) {
			if (e.getId() == i) {
				return e;
			}
		}
		return null;
	}

	public Player getPlayerById(int i) {
		for (Player p : players) {
			if (p.clientID == i)
				return p;
		}
		return null;
	}

	public Tile[][] getLevelMap() {
		return levelMap;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void createMob(Mob b) {
		entities.add(b);
	}

	public void createEntity(Entity e) {
		currentEntityId++;
		e.setId(currentEntityId);

		entities.add(e);
	}

	public void createPlayer(Player p) {
		players.add(p);
		Entity e = (Entity) p;
		// currentEntityId++;
		e.setId(p.clientID);
		entities.add(p);
	}

	public int getAngleBetweenEntities(Entity e1, Entity e2) {
		int angle = (int) (Math.atan2(e2.getY() - e1.getY(),
				e2.getX() - e1.getX()) * 180 / Math.PI);
		return angle;
	}

	public Tile getContainingBlock(int x, int y) {
		return levelMap[x / GameManager.GAME_TILE_SIZE][y
				/ GameManager.GAME_TILE_SIZE];

	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	public void entitiesTick() {

		for (int x = 0; x < entities.size(); x++) {
			if (entities.get(x).getRemoveMe()) {
				if (gm.pm.isServer) {
					gm.pm.server.removeEntity(entities.get(x));
					entities.remove(x);
					System.out.println("REMOVE ME RIGHT NOW YOU DIRTY JEW");
					if (x > 0)
						x--;
					continue;
				}
			}

			Entity e = entities.get(x);
			e.tick();
			if (e.getFX() < 0
					|| e.getFX() + e.getWidth() > width
							* GameManager.GAME_TILE_SIZE - 2) {
				e.setFX(e.getX());
			}
			if (e.getFY() < 0
					|| e.getFY() + e.getHeight() > height
							* GameManager.GAME_TILE_SIZE - 2) {
				e.setFY(e.getY());
			}
			try {
				int bWidth = 0;
				int bHeight = 0;
				if (e.getX() > 0)
					bWidth = (int) e.getX() / GameManager.GAME_TILE_SIZE;
				if (e.getY() > 0)
					bHeight = (int) e.getY() / GameManager.GAME_TILE_SIZE;
				// System.out.println("XA: "+ xa + "| YA" + ya);
				for (int xa = bWidth - 3; xa < bWidth + 3; xa++) {

					for (int ya = bHeight - 3; ya < bHeight + 3; ya++) {
						boolean collidetop = false;
						Rectangle er = new Rectangle((int) e.getFX(),
								(int) e.getY(), e.getWidth(), e.getHeight());
						Rectangle tr = new Rectangle(xa
								* GameManager.GAME_TILE_SIZE, ya
								* GameManager.GAME_TILE_SIZE,
								GameManager.GAME_TILE_SIZE,
								GameManager.GAME_TILE_SIZE);
						if (er.intersects(tr)) {
							if (collideTile(levelMap[xa][ya], e)) {
								collidetop = true;
								if (e.getX() < tr.x) {
									e.setFX(tr.x - e.getWidth());
								} else {
									e.setFX(tr.x + tr.width);
								}
							}
						}
						er = new Rectangle((int) e.getX(), (int) e.getFY(),
								e.getWidth(), e.getHeight());
						tr = new Rectangle(xa * GameManager.GAME_TILE_SIZE, ya
								* GameManager.GAME_TILE_SIZE,
								GameManager.GAME_TILE_SIZE,
								GameManager.GAME_TILE_SIZE);
						if (er.intersects(tr)) {
							if (collideTile(levelMap[xa][ya], e)) {
								if (!collidetop) {
									if (e.getY() < tr.y) {
										e.setFY(tr.y - e.getHeight());
									} else {
										e.setFY(tr.y + tr.height);
									}
								}
							}
						}

					}
				}

			} catch (ArithmeticException ae) {
			}

		}
		if (gm.pm.isServer)
			serverCollisions();
		else
			doCollisions();

	}

	public void serverCollisions() {
		if (gm.pm.isServer && gm.pm.server != null) {
			for (int x = 0; x < entities.size(); x++) {
				for (int y = entities.size() - 1; y > entities.size() - 1; y--) {
					if (entities.get(x).getId() != entities.get(y).getId()) {
						if (collideEntity(entities.get(x), entities.get(y))) {

							Entity t = entities.get(x);
							Entity et = entities.get(y);

							if (t instanceof Player == false
									&& et instanceof Player == false) {
								t.collision(et);
								et.collision(t);
							}
							/*
							 * if (t instanceof Player) { p1 = (Player) t;
							 * gm.pm.
							 * server.broadcastPlayerUpdate(p1.getClientId()); }
							 * if (et instanceof Player) { p2 = (Player) et;
							 * gm.pm
							 * .server.broadcastPlayerUpdate(p2.getClientId());
							 * }
							 */

							/*
							 * if (p1 != null || p2 != null) { //
							 * System.out.println("Collision bitches"); if (p1
							 * == null) gm.pm.server.broadcastEntityUpdate(t);
							 * if (p2 == null)
							 * gm.pm.server.broadcastEntityUpdate(et); }
							 */
						}
					}
				}
				entities.get(x).setX(entities.get(x).getFX());
				entities.get(x).setY(entities.get(x).getFY());

			}

		}

	}

	public void doCollisions() {

		for (int x = 0; x < entities.size(); x++) {

			entities.get(x).setX(entities.get(x).getFX());
			entities.get(x).setY(entities.get(x).getFY());
		}

	}

	public boolean collideTile(Tile t, Entity e) {
		if (Art.Art.getTileFromTileNumber(t.id).isPassable)
			return false;
		return true;
	}

	public ArrayList<Entity> getEntitiesNotInRange(int distance, int e) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity et : entities) {
			int dist2 = getDistance(getEntityById(e), et);
			if (dist2 > distance) {
				list.add(et);

			} else {

			}
		}
		// System.out.println("NOT FOUND LIST SIzE" + list.size());
		return list;
	}

	public ArrayList<Entity> getEntitiesNotInRange(int distance, Entity e) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity et : entities) {
			int dist2 = getDistance(e, et);
			if (dist2 > distance) {
				list.add(et);

			} else {

			}
		}

		return list;
	}

	public ArrayList<Entity> getEntitiesInRange(int distance, int e) {
		ArrayList<Entity> list = new ArrayList<Entity>();

		for (Entity et : entities) {

			int dist2 = getDistance(getEntityById(e), et);
			if (dist2 < distance) {

				list.add(et);
			}
		}

		return list;
	}

	public ArrayList<Entity> getEntitiesInRange(int distance, Entity e) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity et : entities) {
			int dist2 = getDistance(e, et);
			if (dist2 < distance) {

				list.add(et);
			}
		}

		return list;
	}

	public int getDistance(Entity e1, Entity e2) {
		if (e1.getId() != e2.getId()) {
			// double distance = Math.sqrt(Math.pow(((e2.getX()+e2.getWidth() -
			// e1.getX()+e1.getWidth())), 2)
			// - Math.pow((e2.getY()-e2.getHeight() - e1.getY()-e1.getHeight()),
			// 2));
			double distance = 0;
			double xd, yd;

			xd = Math.abs(e2.getFX() - e1.getFX());
			yd = Math.abs(e2.getFY() - e1.getFY());

			distance = Math.pow(xd, 2) + Math.pow(yd, 2);
			distance = Math.sqrt(distance);
			if (Double.toString(distance).compareTo("NaN") == 0)
				return 9000;
			return (int) distance;
		} else {
			return 0;
		}
	}

	public boolean collideEntity(Entity e1, Entity e2) {
		if (e1.getId() != e2.getId()) {
			if (getDistance(e1, e2) < (Math.max(e1.getRadius(), e2.getRadius()))) {
				// chatText.add(Integer.toString(getDistance(e1,e2)));
				return true;
			}
		}
		return false;
	}

}
