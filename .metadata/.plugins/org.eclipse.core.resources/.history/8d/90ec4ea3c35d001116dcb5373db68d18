package Networking;

import java.io.IOException;
import java.util.ArrayList;

import Entity.Entity;
import Entity.Player;
import Level.Key;
import Level.Keys;
import Level.Location;
import Level.RailorComponent;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NetworkClient {
	public Client client;
	Kryo kryo;
	RailorComponent rc;
	public int clientId = -5;
	public String connectIP = "127.0.0.1";
	ArrayList<NetworkCommands>aCom  = new ArrayList<NetworkCommands>();
	NetworkCommands networkCommands;
	public boolean performedTick = false;
	public long lastServerTick = 0;
	public NetworkClient(RailorComponent rc) {
		this.rc = rc;
		startClient();

	}

	public void sendMessage(Object object) {
		client.sendTCP(object);
	}

	public void startClient() {

		client = new Client();
		kryo = client.getKryo();
		kryo.register(Key.class);
		kryo.register(Keys.class);
		kryo.register(Entity.class);
		kryo.register(Location.class);
		kryo.register(StartGamePacket.class);
		kryo.register(NetworkCommands.class);
		kryo.register(NetworkCommand.class);
		kryo.register(ArrayList.class);
		client.start();
		try {
			//client.connect(5000, "127.0.0.1", 54555, 54777);
			
			client.connect(54555, connectIP, 54555, 54777);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("failed to connect");
		}
		// SomeRequest requeste = new SomeRequest();
		// requeste.text = "Here is the request!";
		client.sendTCP(new StartGamePacket());

		client.addListener(new Listener() {
			
			public void received(Connection connection, Object object) {
				
				if (object instanceof NetworkCommands) {
					NetworkCommands ncs = (NetworkCommands) object;
					lastServerTick = ncs.getGameTick();
					aCom.add(ncs);
				}
				if (object instanceof Keys) {
					// SomeResponse response = (SomeResponse)object;
					// System.out.println(response.text);
					// Keys keys = (Keys)object;
					// rc.level.players.get(0).keys= keys;

				}
				if (object instanceof Key) {
					Key k = (Key) object;

					if (k.getIsDown()) {
						// rc.level.players.get(0).keys.keyPressed(k.getKeyCode());
					} else {
						// rc.level.players.get(0).keys.keyReleased(k.getKeyCode());
					}
				}
				if (object instanceof StartGamePacket) {

					StartGamePacket sg = (StartGamePacket) object;
					clientId = sg.getClientId();
					TurnSynchronizer.synchedRandom.setSeed(sg.getGameSeed());
					System.out.println(TurnSynchronizer.synchedSeed
							+ "CLIENT SYNCHED SEED RECEIEVED" + clientId);
					rc.startGame(clientId);
				}

			}
		});
	}
	@SuppressWarnings("unchecked")
	public NetworkCommands getCurrentTickCommands(long x){
		for(NetworkCommands c : (ArrayList<NetworkCommands>)aCom.clone()){
			//if(c.getGameTick()<=rc.level.gameTick){
				return c;
			//}
		}
		//System.out.println("no commands for this tick = " + rc.level.gameTick + " server:" + x);
		return null;
	}
	public void performGameTick(NetworkCommands ncss){
		NetworkCommands ncs = ncss;
		
		//System.out.println(ncs.getClientID());
		while (ncs.hasCommands()) {
			NetworkCommand nc = ncs.getCommand();
			Object ob = nc.getObject();
			performActions(ob);
			ncs.popCommand();
			//System.out.println("Each command");
			//rc.addText(ncs.getClientID());
			//System.out.println("action inside of shit");
		}
	}
	public boolean performTick(){
		NetworkCommands nc = getCurrentTickCommands(rc.level.gameTick);
		if(nc != null){
			performGameTick(nc);
			aCom.remove(nc);
			return true;
		}
		return false;
	}
	public void performActions(Object object) {
		if (object instanceof Location) {
			
			Location l = (Location)object;
			//System.out.println(l.getID());
			//if(clientID == 1){
				//System.out.println("inside location");
			//}
			if(l.getID()<=0){
				l.setID(l.getID()*-1);
				Player p = rc.level.getPlayerById(l.getID());
				//if(p!= null && p != rc.myPlayer){
					p.setLocation(l);
				//}
			}else{
				System.out.println("non player entity update");
				Entity et = rc.level.getEntityById(l.getID());
				if(et!=null)
				et.setLocation(l);
				else
					System.out.println("Cant find entity by id" + l.getID());
			}
			
			
		}
	}
	public void startTick(){
		while(performTick()){
			
		}
		
	}
	public void endTick(){
		sendMessage();
	}

	public void addMessage(Object o) {
		if(networkCommands == null){
			networkCommands = new NetworkCommands(rc.level.gameTick,clientId);
		}
		networkCommands.addCommand(o);
		// TODO Auto-generated method stub

	}
	public void sendMessage(){
		if(networkCommands != null)
		client.sendTCP(networkCommands);
		networkCommands = null;
		
	}

}
