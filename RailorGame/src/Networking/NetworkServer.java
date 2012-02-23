package Networking;

import Entity.Entity;
import Entity.Player;
import Level.Key;
import Level.Keys;
import Level.Location;
import Level.RailorComponent;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NetworkServer {
	Server server;
	Kryo kryo;
	RailorComponent rc;
	ArrayList<NetworkCommands> networkCommands = new ArrayList<NetworkCommands>();
	public NetworkServer(RailorComponent rc) {
		this.rc = rc;
		createServer();

	}

	public void broadcastMessage(Object object) {
		for (Connection c : server.getConnections()) {
			c.sendUDP(object);
		}
	}
	public void broadcastEntityUpdate(Entity e){
		for (Connection c : server.getConnections()) {
			Location l = e.getLocation();
			addMessage(l,c.getID());
		}
	}
	public void broadcastPlayerUpdate(int id){
		for (Connection c : server.getConnections()) {
			for(Player p : rc.level.players){
				Location l = p.getPlayerLocation();
				l.setID(p.getClientId() *-1);
				if(p.getClientId()==c.getID()){
					addMessage(l,c.getID());
				}
			}
		}
	}
	public void broadcastPlayersUpdate(boolean all){
		for (Connection c : server.getConnections()) {
			for(Player p : rc.level.players){
				Location l = p.getPlayerLocation();
				l.setID(p.getClientId() *-1);
				if(p.getClientId()==c.getID()){
					if(all)
						addMessage(l,c.getID());
					//System.out.println("I cant, thats me!");
				}else{
					addMessage(l,c.getID());
				}
				
			}
			
		}
	}
	public NetworkCommands getNetworkCommandsByClientId(int id){
		for(NetworkCommands nc : networkCommands){
			if(nc.getClientID()==id){
				return nc;
			}
		}
		NetworkCommands ncs = new NetworkCommands(rc.level.gameTick,id);
		networkCommands.add(ncs);
		return ncs;
	}
	public void addMessage(Object o,int id) {
		getNetworkCommandsByClientId(id).addCommand(o);
		// TODO Auto-generated method stub

	}
	public void sendMessage(int id){
		NetworkCommands ncs = getNetworkCommandsByClientId(id);
		if(ncs.hasCommands())
		getConnectionById(id).sendTCP(networkCommands);
		removeNetworkCommandsByClientId(id);
		
	}
	public void removeNetworkCommandsByClientId(int id){
				networkCommands.remove(getNetworkCommandsByClientId(id));
		
	}
	public Connection getConnectionById(int id){
		for (Connection c : server.getConnections()) {
			if(c.getID()==id)
			return c;
		}
		return null;
	}
	public void startGame() {
		int x = 1;
		//TurnSynchronizer.synchedSeed = TurnSynchronizer.unsynchedRandom.nextLong();
		for (Connection c : server.getConnections()) {
			c.sendTCP(new StartGamePacket(TurnSynchronizer.synchedSeed, c
					.getID()));
			//System.out.println(c.getID() + ": Client Id");
			x++;
		}
		rc.startGame(-1);

	}
	public void startTick(){
		
	}
	public void endTick(){
		//System.out.println("endtick");
		broadcastPlayersUpdate(false);
		sendNetworkCommands();
		clearAllNetworkCommands();
	}
	public void sendNetworkCommands(){
		for (Connection c : server.getConnections()) {
			NetworkCommands ncs = getNetworkCommandsByClientId(c.getID());
			if(ncs.hasCommands())
				c.sendTCP(ncs);
				removeNetworkCommandsByClientId(c.getID());
		}
	}
	public void clearAllNetworkCommands(){
		networkCommands.clear();
	}
	public void createServer() {
		server = new Server();
		kryo = server.getKryo();
		kryo.register(Key.class);
		kryo.register(Keys.class);
		kryo.register(Entity.class);
		kryo.register(Location.class);
		kryo.register(StartGamePacket.class);
		kryo.register(NetworkCommands.class);
		kryo.register(NetworkCommand.class);
		kryo.register(ArrayList.class);
		server.start();

		try {
			
			server.bind(54555, 54777);
		} catch (IOException e) {
			System.out.println("Failed to bind it bro");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		try {

			server.addListener(new Listener() {
				public void received(Connection connection, Object object) {
					// System.out.println("Received message in server");

					if (object instanceof NetworkCommands) {
						NetworkCommands ncs = (NetworkCommands) object;
						while (ncs.hasCommands()) {
							NetworkCommand nc = ncs.getCommand();
							Object ob = nc.getObject();
							performActions(ob, ncs.getClientID());
							ncs.popCommand();
							//System.out.println("action inside of shit");
						}
						//System.out.println("we have received commands captain");
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Failed");
		}
	}

	public void performActions(Object object, int clientID) {
		if (object instanceof Key) {
			Key k = (Key) object;
			Player p = rc.level.getPlayerById(clientID);
			//System.out.println("Im a key?");
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
			Location l = (Location)object;
			Player p = rc.level.getPlayerById(clientID);
			if(p!= null){
				p.setLocation(l);
			}
			
		}
		if (object instanceof StartGamePacket) {

			// System.out.println(TurnSynchronizer.synchedSeed +
			// "SERVER SYNCHED SEED SENT" + connection.getID());

		}
		if (object instanceof String) {
			System.out.println(object);
		}
	}

	public void shutDown() {
		server.close();
	}

}
