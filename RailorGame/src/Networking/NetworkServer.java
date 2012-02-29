package Networking;

import Entity.Entity;
import Entity.EntityClass;
import Entity.Player;
import Level.Key;
import Level.Keys;
import Level.Location;
import Mob.Mob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import packets.EntityRemovePacket;
import packets.MobCreatePacket;
import packets.MobUpdatePacket;
import packets.PlayerCreatePacket;
import packets.StartGamePacket;

import mainGame.ProgramManager;
import mainGame.ProgramManager.GameState;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NetworkServer {
	Server server;
	Kryo kryo;
	ProgramManager pm;
	ArrayList<ClientObjectList> clientSeesObjects = new ArrayList<ClientObjectList>();
	ArrayList<NetworkCommands> networkCommandsIn = new ArrayList<NetworkCommands>();
	ArrayList<NetworkCommands> networkCommandsOut = new ArrayList<NetworkCommands>();

	public NetworkServer(ProgramManager programManager) {
		this.pm = programManager;
		createServer();

	}

	public void createServer() {
		server = new Server(32768, 4096);
		kryo = server.getKryo();
		kryo.register(Key.class);
		kryo.register(Keys.class);
		kryo.register(Entity.class);
		kryo.register(Location.class);
		kryo.register(StartGamePacket.class);
		kryo.register(NetworkCommands.class);
		kryo.register(NetworkCommand.class);
		kryo.register(ArrayList.class);
		kryo.register(ClientInformation.class);
		kryo.register(UpdateObjectList.class);
		kryo.register(MobUpdatePacket.class);
		kryo.register(MobCreatePacket.class);
		kryo.register(EntityClass.class);
		kryo.register(EntityRemovePacket.class);
		kryo.register(PlayerCreatePacket.class);
		// kryo.register(Integer.class);
		server.start();

		try {

			server.bind(54555, 54555);
		} catch (IOException e) {
			System.out.println("Failed to bind it bro");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		try {

			server.addListener(new Listener() {
				public void received(Connection connection, Object object) {
					// System.out.println("Received message in server");
					System.out.println("GENERAL RECEIVED");
					if (object instanceof NetworkCommands) {
						NetworkCommands ncs = (NetworkCommands) object;
						networkCommandsIn.add(ncs);

						// System.out.println("we have received commands captain");
					}
					if(object instanceof StartGamePacket){
						System.out.println("RECEIVED START GAME PACKET");
						connection.sendTCP(new StartGamePacket(TurnSynchronizer.synchedSeed, connection.getID()));
						for (Connection c : server.getConnections()) {
							if(c.getID() != connection.getID())
							connection.sendUDP(new PlayerCreatePacket(c.getID()));
						}
						System.out.println("ADD NEW PLAYER");
						addNewPlayer(connection.getID());
						
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Failed");
		}
	}
	public void addExistingPlayersToNewClient(int id){
		
	}
	public void addNewPlayer(int id){
		Player p = new Player(50,50,id);
		pm.gameManager.level.createPlayer(p);
		newPlayer(id);
		
	}
	public void newPlayer(int id){
		broadcastMessage(new PlayerCreatePacket(id));
	}
	public void broadcastMessage(Object object,int id) {
		for (Connection c : server.getConnections()) {
			if(c.getID() != id)
			c.sendUDP(object);
		}
	}
	public void broadcastMessage(Object object) {
		for (Connection c : server.getConnections()) {
			c.sendUDP(object);
		}
	}

	public void broadcastEntityUpdate(Entity e) {
		for (Connection c : server.getConnections()) {
			Location l = e.getLocation();
			addMessage(l, c.getID());
		}
	}

	public void pauseAll(boolean pause) {
		for (Connection c : server.getConnections()) {
			if (pause)
				c.sendTCP("pause");
			else
				c.sendTCP("unpause");
		}
		endTick();
	}

	public void broadcastPlayerUpdate(int id) {
		for (Connection c : server.getConnections()) {
			for (Player p : pm.gameManager.level.players) {
				Location l = p.getPlayerLocation();
				l.setID(p.getClientId());
				if (c.getID() == id) {
					addMessage(l, c.getID());
				} else {

				}
			}
		}
		broadcastPlayersUpdate(false);
	}

	public void broadcastPlayersUpdate(boolean all) {
		for (Connection c : server.getConnections()) {
			for (Player p : pm.gameManager.level.players) {
				Location l = p.getPlayerLocation();
				l.setID(p.getClientId());
				if (p.getClientId() == c.getID()) {
					if (all)
						addMessage(l, c.getID());
					// System.out.println("I cant, thats me!");
				} else {
					addMessage(l, c.getID());
				}

			}

		}
	}

	public NetworkCommands getNetworkCommandsInByClientId(int id) {
		for (NetworkCommands nc : networkCommandsIn) {
			if (nc.getClientID() == id) {
				return nc;
			}
		}
		NetworkCommands ncs = new NetworkCommands(id);
		networkCommandsIn.add(ncs);
		return ncs;
	}

	public NetworkCommands getNetworkCommandsOutByClientId(int id) {
		for (NetworkCommands nc : networkCommandsOut) {
			if (nc.getClientID() == id) {
				return nc;
			}
		}
		NetworkCommands ncs = new NetworkCommands(id);
		networkCommandsOut.add(ncs);
		return ncs;
	}

	public ClientObjectList getClientObjectList(int id) {
		for (ClientObjectList nc : clientSeesObjects) {
			if (nc.getClientID() == id) {
				//System.out.println("Found one broseph");
				return nc;
			}
		}
		//System.out.println(id + "ID IN GETCLIENTOBJECTLIST");
		ClientObjectList ncs = new ClientObjectList(id);
		clientSeesObjects.add(ncs);
		return ncs;
	}
	public void setClientObjectList(ClientObjectList i, int id) {
		//System.out.println(i.commands.size() + "INSIDE SET OBJECT");
		clientSeesObjects.remove(getClientObjectList(id));
		//System.out.println(getClientObjectList(id).commands.size() + "AFTER REMOVE INSIDE SET OBJECT");
		clientSeesObjects.add(i);
		//System.out.println(getClientObjectList(id).commands.size() + "AFTER ADD SET OBJECT");
	}

	public void addMessage(Object o, int id) {
		getNetworkCommandsOutByClientId(id).addCommand(o);
		// TODO Auto-generated method stub

	}

	public void sendAllOut(int id) {
		NetworkCommands ncs = getNetworkCommandsOutByClientId(id);
		if (ncs.hasCommands())
			getConnectionById(id).sendTCP(networkCommandsOut);
		removeNetworkCommandsOutByClientId(id);

	}

	public void removeNetworkCommandsInByClientId(int id) {
		networkCommandsIn.remove(getNetworkCommandsInByClientId(id));

	}

	public void removeNetworkCommandsOutByClientId(int id) {
		networkCommandsOut.remove(getNetworkCommandsOutByClientId(id));

	}

	public Connection getConnectionById(int id) {
		for (Connection c : server.getConnections()) {
			if (c.getID() == id)
				return c;
		}
		return null;
	}

	public void removeEntity(Entity e) {
		for (Connection c : server.getConnections()) {
			addMessage(new EntityRemovePacket(e.getId()), c.getID());
		}
	}

	public void removeEntity(int entId) {
		for (Connection c : server.getConnections()) {
			addMessage(new EntityRemovePacket(entId), c.getID());
		}
	}

	public void createMob(Mob e, int clientId) {
		//System.out.println("creaing a mob");
		addMessage(new MobCreatePacket(EntityClass.CLASS_MOB, e.getId()),
				clientId);
	}

	public void updateMob(Mob e, int clientId) {
		addMessage(
				new MobUpdatePacket(e.getId(), e.getLocation(),
						e.getDirAngle(), e.isMoving), clientId);
	}

	public void startGame() {
		TurnSynchronizer.synchedSeed = TurnSynchronizer.unsynchedRandom
				.nextLong();
		TurnSynchronizer.synchedRandom.setSeed(TurnSynchronizer.synchedSeed);
		for (Connection c : server.getConnections()) {
			c.sendTCP(new StartGamePacket(TurnSynchronizer.synchedSeed, c
					.getID()));
			// System.out.println(c.getID() + ": Client Id");
		}
		pm.gameManager.startLevel(-1);

	}

	public void startTick() {
		@SuppressWarnings("unchecked")
		ArrayList<NetworkCommands> ncss = (ArrayList<NetworkCommands>) networkCommandsIn
				.clone();
		networkCommandsIn.clear();
		while (ncss.size() > 0) {
			NetworkCommands ncs = ncss.get(0);
			while (ncs != null && ncs.hasCommands()) {
				NetworkCommand nc = ncs.getCommand();
				Object ob = nc.getObject();
				performActions(ob, ncs.getClientID());
				ncs.popCommand();
			}
			ncss.remove(ncs);
		}

	}

	public void updateEntities() {
			Random rand = new Random();
		for (Connection c : server.getConnections()) {
			ClientObjectList old = getClientObjectList(c.getID());
			for(int x = 0; x < old.commands.size();x++){
				Entity e = pm.gameManager.level.getEntityById(old.getIdAt(x));
				if (e instanceof Mob) {
					Mob m = (Mob) e;
					 if(rand.nextInt(10) > 5)
					 m.isMoving=false;
					 else
					 m.isMoving=true;
					 m.setDirAngle(rand.nextInt(360));
					updateMob(m, c.getID());
			}
			}
		}
	}

	public void endTick() {
		// System.out.println("endtick");
		if (ProgramManager.STATE == GameState.GameScreen) {
			for (Connection c : server.getConnections()) {
				ClientObjectList old = getClientObjectList(c.getID());
				//System.out.println(old.commands.size() + "OLD SIZE");
				ClientObjectList newList = new ClientObjectList(c.getID());
				
				if(pm.gameManager.level.getEntityById(c.getID()) != null)
				for (Entity e : pm.gameManager.level.getEntitiesInRange(pm.updateRange, c.getID())) {
					
						
					newList.addCommand(e.getId());
					if (old.containsId(e.getId())) {
						//System.out.println("was there last time bro");
					} else {
						
						if (e instanceof Mob) {
							createMob((Mob) e, c.getID());
						}
					}
					//old.removeId(e.getId());
				}
				
				setClientObjectList(newList, c.getID());		
				//System.out.println(newList.commands.size() + "NEW SIZE");
				//System.out.println(getClientObjectList(c.getID()).commands.size() + "NEW SIZE");;
			}
		}

		updateEntities();
		broadcastPlayersUpdate(false);

		sendNetworkCommands();

		clearAllNetworkCommandsOut();
	}

	public void sendNetworkCommands() {
		for (Connection c : server.getConnections()) {
			NetworkCommands ncs = getNetworkCommandsOutByClientId(c.getID());
			if (ncs != null && ncs.hasCommands())
				c.sendTCP(ncs);
			removeNetworkCommandsOutByClientId(c.getID());
		}
	}

	public void clearAllNetworkCommandsIn() {
		networkCommandsIn.clear();
	}

	public void clearAllNetworkCommandsOut() {
		networkCommandsOut.clear();
	}

	public void performActions(Object object, int clientID) {
		if (object instanceof Key) {
			Key k = (Key) object;
			Player p = pm.gameManager.level.getPlayerById(clientID);
			// System.out.println("Im a key?");
			if (k.getIsDown()) {
				p.keys.keyPressed(k.getKeyCode());
			} else {
				p.keys.keyReleased(k.getKeyCode());
			}
			// Keys request = (Keys)object;
			// Keys response = new Keys();
			// connection.sendTCP(response);
		}
		if (object instanceof Location) {
			Location l = (Location) object;
			Entity p = pm.gameManager.level.getEntityById(clientID);
			if (p != null) {
				p.setLocation(l);
			}

		}
		if (object instanceof Integer) {
			int k = (Integer) object;
			addMessage(pm.gameManager.level.getEntityById(k).getLocation(),
					clientID);
		}
		if (object instanceof StartGamePacket) {
		}
		if (object instanceof String) {
			System.out.println(object);
		}
	}

	public void shutDown() {
		server.close();
	}

}
