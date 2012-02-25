package Level;

import java.awt.Rectangle;
import java.util.ArrayList;

import mainGame.GameManager;

import Entity.Entity;
import Entity.Player;
import MapEditor.EditorManager;
import Mob.Mob;
import Networking.TurnSynchronizer;
import Networking.UpdateObjectList;

public class Level {
	public int width, height;
	private Tile[][] levelMap;
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

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				levelMap[x][y] = new Tile(Art.Art.BITMAP_TILE_GRASS);
			}
		}
		for (int x = 0; x < 1; x++) {
				createEntity(new Mob(450,450));
		}
	}
	public Level(int w, int h, EditorManager editorManager) {
		width = w;
		height = h;
		em=editorManager;
		levelMap = new Tile[w][h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				levelMap[x][y] = new Tile(Art.Art.BITMAP_TILE_GRASS);
			}
		}
	}
	public void tick() {

		entitiesTick();
		// System.out.println("current tick is" + gameTick + rc.isServer);
	}

	public boolean setLocationById(Location l) {
		if(l.getID()<0){
			int lid = l.getID() * -1;
			Player p = getPlayerById(lid);
			if (p != null) {
				p.setX(l.getX());
				p.setY(l.getY());
				p.setFX(l.getX());
				p.setFY(l.getY());
				return true;
			}
		}else{
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
	public Player getPlayerById(int i){
		for (Player p : players) {
			if(p.clientID==i)
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

	public void createEntity(Entity e) {
		currentEntityId++;
		e.setId(currentEntityId);

		entities.add(e);
	}
	public void createPlayer(Player p){
		players.add(p);
		Entity e = (Entity)p;
		currentEntityId++;
		e.setId(currentEntityId);
		entities.add(p);
	}
	public int getAngleBetweenEntities(Entity e1, Entity e2){
		int nx = 500;
		int ny = 500;
		int angle = (int) (Math.atan2(e2.getY() - e1.getY(), e2.getX() - e1.getX()) * 180 / Math.PI);
		return angle;
	}
	public Tile getContainingBlock(int x, int y) {
		return levelMap[x / GameManager.GAME_TILE_SIZE][y
				/ GameManager.GAME_TILE_SIZE];

	}

	public void entitiesTick() {
		for (int x = 0; x < entities.size(); x++) {
			if (entities.get(x).getRemoveMe()) {
				entities.remove(x);
				System.out.println("REMOVE ME RIGHT NOW YOU DIRTY JEW");
				if (x > 0)
					x--;
			} else {

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
						bWidth = (int)e.getX() / GameManager.GAME_TILE_SIZE;
					if (e.getY() > 0)
						bHeight = (int)e.getY() / GameManager.GAME_TILE_SIZE;
					// System.out.println("XA: "+ xa + "| YA" + ya);
					for (int xa = bWidth - 3; xa < bWidth + 3; xa++) {

						for (int ya = bHeight - 3; ya < bHeight + 3; ya++) {
							boolean collidetop = false;
							Rectangle er = new Rectangle((int) e.getFX(),(int) e.getY(),
									e.getWidth(), e.getHeight());
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
							er = new Rectangle((int)e.getX(), (int) e.getFY(),
									e.getWidth(), e.getHeight());
							tr = new Rectangle(xa * GameManager.GAME_TILE_SIZE,
									ya * GameManager.GAME_TILE_SIZE,
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
		}
		doCollisions();
		
	}

	public void doCollisions() {
		// Entity[] entities = (Entity[])getEntities().toArray().clone();

		for (int x = 0; x < entities.size(); x++) {
			for (int y = entities.size() - 1; y > entities.size() / 2 - 1; y--) {
				if (entities.get(x).getId() != entities.get(y).getId()) {
					if (collideEntity(entities.get(x), entities.get(y))) {
						
						Entity t = entities.get(x);
						Entity et = entities.get(y);
						if(gm.pm.isServer && gm.pm.server!= null){
							
						
						Player p1 = null;
						Player p2 = null;
						if(t instanceof Player){
							p1 = (Player)t;
							gm.pm.server.broadcastPlayersUpdate(false);
						}
						if(et instanceof Player){
							p2 = (Player)et;
							gm.pm.server.broadcastPlayersUpdate(false);
						}
						entities.get(x).collision(entities.get(y));
						entities.get(y).collision(entities.get(x));
						if(p1 != null || p2 != null){
							//System.out.println("Collision bitches");
							if(p1==null)
								gm.pm.server.broadcastEntityUpdate(t);
							if(p2==null)
								gm.pm.server.broadcastEntityUpdate(et);
						}
						}else{
							if(gm.pm.isClient){
								//UpdateObjectList ob = new UpdateObjectList();
								//ob.addObject(t.getId());
								//ob.addObject(et.getId());
								//gm.pm.client.addMessage(t.getId());
								//gm.pm.client.addMessage(et.getId());
							}
							entities.get(x).collision(entities.get(y));
							entities.get(y).collision(entities.get(x));
						}
						
						
						
						
						
						
						
						
					}
					
				}
			}
			entities.get(x).setX(entities.get(x).getFX());
			entities.get(x).setY(entities.get(x).getFY());
		}

	}

	public boolean collideTile(Tile t, Entity e) {
		if (t.isPassable)
			return false;
		return true;
	}
	public ArrayList<Entity> getEntitiesInRange(int distance, Entity e) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity et : entities) {
			int dist2 = getDistance(e, et);
			if (dist2 == 0) {

			} else {
				if (dist2 < 0) {
					list.add(et);
				}
			}
		}
		return list;
	}
	
	public int getDistance(Entity e1, Entity e2) {
		if(e1.getId() != e2.getId()){
		//double distance = Math.sqrt(Math.pow(((e2.getX()+e2.getWidth() - e1.getX()+e1.getWidth())), 2)
		//		- Math.pow((e2.getY()-e2.getHeight() - e1.getY()-e1.getHeight()), 2));
		double distance=0;
		double xd,yd;

		xd = Math.abs(e2.getFX()-e1.getFX());
		yd = Math.abs(e2.getFY()-e1.getFY());
		
		distance = Math.pow(xd, 2) + Math.pow(yd,2);
		distance = Math.sqrt(distance);
		int dst = (int)distance;
		if(Double.toString(distance).compareTo("NaN")==0)
			return 9000;
		return (int) distance;
		}
		return 9000;
	}
	public boolean collideEntity(Entity e1, Entity e2){
		if(e1.getId() != e2.getId()){
		if(getDistance(e1,e2)<(Math.max(e1.getRadius(), e2.getRadius()))){
			//chatText.add(Integer.toString(getDistance(e1,e2)));
			return true;
		}
		}
		return false;
	}

}
