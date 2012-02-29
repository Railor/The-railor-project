package Networking;

import java.io.IOException;
import java.util.ArrayList;

import packets.EntityRemovePacket;
import packets.MobCreatePacket;
import packets.MobUpdatePacket;
import packets.PlayerCreatePacket;
import packets.StartGamePacket;

import mainGame.ProgramManager;
import mainGame.ProgramManager.GameState;


import Entity.Entity;
import Entity.EntityClass;
import Entity.Player;
import Level.Key;
import Level.Keys;
import Level.Location;
import Mob.Mob;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkClient {
	public Client client;
	Kryo kryo;
	ProgramManager pm;
	public int clientId = -5;
	ArrayList<NetworkCommands> aCom = new ArrayList<NetworkCommands>();
	NetworkCommands networkCommands;
	public boolean performedTick = false;
	public long lastServerTick = 0;

	public NetworkClient(ProgramManager programManager) {
		this.pm = programManager;
		startClient();
	}

	public void sendMessage(Object object) {
		client.sendTCP(object);
	}
	
	public void startClient() {
		client = new Client(16384, 4096);
		kryo = client.getKryo();
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
		//kryo.register(Integer.class);
		client.start();
		try {
			// client.connect(5000, "127.0.0.1", 54555, 54777);
			
			client.connect(54555, pm.connectip, 54555, 54555);
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
				// System.out.println("asdsad");
				if(object instanceof String){
					System.out.println(object.toString());
					if(object.toString().compareTo("pause")==0){
						pm.gameManager.gamePaused=false;
					}else if(object.toString().compareTo("unpause")==0){
						pm.gameManager.gamePaused=true;
					}
				}
				if (object instanceof NetworkCommands) {
					System.out.println("Receive network commands");
					NetworkCommands ncs = (NetworkCommands) object;
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
					System.out.println(sg.getGameSeed());
					TurnSynchronizer.synchedRandom.setSeed(sg.getGameSeed());
					System.out.println(TurnSynchronizer.synchedSeed
							+ "CLIENT SYNCHED SEED RECEIEVED" + clientId);
					
					pm.gameManager.startLevel(clientId);
				}
				if(object instanceof PlayerCreatePacket){
					System.out.println("RECEIVE PLAYER CREATE PACKET");
					PlayerCreatePacket c = (PlayerCreatePacket) object;
					Player p = new Player(50,50,(c.id));
					pm.gameManager.level.createPlayer(p);
					//System.out.println("myplayer = p!");
					if(clientId == c.id){
						//System.out.println("myplayer = p!");
						pm.gameManager.myPlayer = p;
					}
					System.out.println("END RECEIVE PLAYER CREATE PACKET");
					//pm.gameManager.level.createPlayer(p);
				}

			}
		});
	}

	@SuppressWarnings("unchecked")
	public NetworkCommands getNetworkCommand() {
		for (NetworkCommands c : (ArrayList<NetworkCommands>) aCom.clone()) {
			// if(c.getGameTick()<=rc.level.gameTick){
			// System.out.println("asdsad");
			return c;
			// }
		}
		// System.out.println("no commands for this tick = " + rc.level.gameTick
		// + " server:" + x);
		return null;
	}

	public void performGameTick(NetworkCommands ncss) {
		NetworkCommands ncs = ncss;

		// System.out.println(ncs.getClientID());
		while (ncs.hasCommands()) {
			NetworkCommand nc = ncs.getCommand();
			Object ob = nc.getObject();
			performActions(ob);
			ncs.popCommand();
			// System.out.println("Each command");
			// rc.addText(ncs.getClientID());
			// System.out.println("action inside of shit");
		}
	}

	public boolean performTick() {
		NetworkCommands nc = getNetworkCommand();
		if (nc != null) {
			performGameTick(nc);
			aCom.remove(nc);
			return true;
		}
		return false;
	}

	public void performActions(Object object) {
		// System.out.println("asdsad");
		// client=null;
		if (object instanceof Location) {
			System.out.println("Location update");
			Location l = (Location) object;
				Entity et = pm.gameManager.level.getEntityById(l.getID());
				if (et != null)
					et.setLocation(l);
				else
					System.out.println("Cant find entity by id" + l.getID());
			

		}
		if(object instanceof MobCreatePacket){
			MobCreatePacket p = (MobCreatePacket)object;
			createMobByClass(p.type, p.id);
			//System.out.println(p.id);
		}
		
		if(object instanceof MobUpdatePacket){
			
			MobUpdatePacket p = (MobUpdatePacket)object;
			Mob b = (Mob)pm.gameManager.level.getEntityById(p.mobId);
			//System.out.println(p.mobId + ": Yo im updating");
			if(b != null){
				b.setLocation(p.location);
				b.setDirAngle(p.dirAngle);
				b.isMoving = p.isMoving;
			}else{
				System.out.println(p.mobId + ": MOB NOT FOUND, CLIENT SIDE UPDATE PACKET");
			}
		}
		if(object instanceof EntityRemovePacket){
			pm.gameManager.level.removeEntity(pm.gameManager.level.getEntityById(((EntityRemovePacket) object).entityId));
		}
		
		
	}
	public void createMobByClass(EntityClass c, int id){
		if(c == EntityClass.CLASS_MOB){
			pm.gameManager.level.createMob(new Mob(id));
		}
		
		
		
	}
	public void startTick() {
		//performTick();
		System.out.println("Start tick");
		if(ProgramManager.STATE==GameState.GameScreen){
			while (performTick()) {
			}
			//performTick();
		}
	

	}

	public void endTick() {
		System.out.println("end tick");
		sendMessage();
	}

	public void addMessage(Object o) {
		if (networkCommands == null) {
			networkCommands = new NetworkCommands(clientId);
		}
		networkCommands.addCommand(o);
		// TODO Auto-generated method stub

	}

	public void sendMessage() {
		if (networkCommands != null)
			client.sendTCP(networkCommands);
		networkCommands = null;

	}
	public void shutDown(){
		System.out.println("Shutdown function");
		client.close();
	}

}
