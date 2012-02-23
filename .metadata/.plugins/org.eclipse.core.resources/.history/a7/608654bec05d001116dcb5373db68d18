package Level;

import java.awt.Rectangle;
import java.util.ArrayList;
import Entity.Entity;
import Entity.Player;
import Networking.TurnSynchronizer;

public class Level {
	public int width, height;
	private Tile[][] levelMap;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Player> players = new ArrayList<Player>();
	RailorComponent rc;
	int currentEntityId = 0;
	public long gameTick = 0;

	public Level(int w, int h, RailorComponent rca) {
		width = w;
		height = h;
		rc = rca;
		levelMap = new Tile[w][h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				levelMap[x][y] = new Tile(
						TurnSynchronizer.synchedRandom.nextInt(8));
			}
		}
		for (int x = 0; x < 20; x++) {

			createEntity(new Entity(
					TurnSynchronizer.synchedRandom.nextInt(width
							* RailorComponent.TILE_SIZE - 255),
					TurnSynchronizer.synchedRandom.nextInt(height
							* RailorComponent.TILE_SIZE - 50)));

		}

	}

	public void tick() {
		// if(rc.isServer){
		// rc.server.sendMessage(rc.isServer + ": " +
		// TurnSynchronizer.synchedRandom.nextInt(50) + "Tick:" + gameTick);
		// }
		// System.out.println(rand.nextInt(5) + "| " + rand2.nextInt(5));
		// System.out.println(rand.nextInt(5) + "| " + rand2.nextInt(5));
		// System.out.println(rand.nextInt(5) + "| " + rand2.nextInt(5));
		// System.out.println(rand.nextInt(5) + "| " + rand2.nextInt(5));
		// System.out.println(rand.nextInt(5) + "| " + rand2.nextInt(5));

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

	public Tile getContainingBlock(int x, int y) {
		return levelMap[x / RailorComponent.TILE_SIZE][y
				/ RailorComponent.TILE_SIZE];

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
								* RailorComponent.TILE_SIZE - 2) {
					e.setFX(e.getX());
				}
				if (e.getFY() < 0
						|| e.getFY() + e.getHeight() > height
								* RailorComponent.TILE_SIZE - 2) {
					e.setFY(e.getY());
				}
				try {
					int bWidth = 0;
					int bHeight = 0;
					if (e.getX() > 0)
						bWidth = e.getX() / RailorComponent.TILE_SIZE;
					if (e.getY() > 0)
						bHeight = e.getY() / RailorComponent.TILE_SIZE;
					// System.out.println("XA: "+ xa + "| YA" + ya);
					for (int xa = bWidth - 3; xa < bWidth + 3; xa++) {

						for (int ya = bHeight - 3; ya < bHeight + 3; ya++) {
							boolean collidetop = false;
							Rectangle er = new Rectangle(e.getFX(), e.getY(),
									e.getWidth(), e.getHeight());
							Rectangle tr = new Rectangle(xa
									* RailorComponent.TILE_SIZE, ya
									* RailorComponent.TILE_SIZE,
									RailorComponent.TILE_SIZE,
									RailorComponent.TILE_SIZE);
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
							er = new Rectangle(e.getX(), e.getFY(),
									e.getWidth(), e.getHeight());
							tr = new Rectangle(xa * RailorComponent.TILE_SIZE,
									ya * RailorComponent.TILE_SIZE,
									RailorComponent.TILE_SIZE,
									RailorComponent.TILE_SIZE);
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
			Entity e1 = entities.get(x);
			for (int y = entities.size() - 1; y > entities.size() / 2 - 1; y--) {
				if (entities.get(x).getId() != entities.get(y).getId()) {
					if (collideEntity(entities.get(x), entities.get(y))) {
						
						
						if(rc.isServer && rc.server!= null){
							
						Entity t = entities.get(x);
						Entity et = entities.get(y);
						Player p1 = null;
						Player p2 = null;
						if(t instanceof Player){
							p1 = (Player)t;
							rc.server.broadcastPlayerUpdate(p1.getClientId());
							
						}
						if(et instanceof Player){
							p2 = (Player)et;
							rc.server.broadcastPlayerUpdate(p2.getClientId());
						}
						entities.get(x).collision(entities.get(y));
						entities.get(y).collision(entities.get(x));
						if(p1 != null || p2 != null){
							if(p1==null)
								rc.server.broadcastEntityUpdate(t);
							if(p2==null)
								rc.server.broadcastEntityUpdate(et);
						}
						}else{
							entities.get(x).collision(entities.get(y));
							entities.get(y).collision(entities.get(x));
						}
						
						
						
						
						
						
						
						
					}
					/*
					Entity e2 = entities.get(y);
					int e1x = e1.getFX(),e2x = e2.getFX(),e1y = e1.getFY(),e2y = e2.getFY();
					
					boolean colb=false,colx=false,coly=false;
					///////////////////both axis
					if (collideEntity(entities.get(x), entities.get(y))) {
						colb=true;
						e1.setFY(e1.getY());
						if (collideEntity(entities.get(x), entities.get(y))) {
							colx=true;
						}else{
							
						}
						///////////////////only y axis collision
						e1.setFY(e1y);
						e1.setFX(e1.getX());
						if (collideEntity(entities.get(x), entities.get(y))) {
							coly=true;
						}
					}else{
						
					}
					/////////////////////Only x axis collision
					
					*/
					//if(col==3){
						
				//	}
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
