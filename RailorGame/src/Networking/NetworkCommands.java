package Networking;

import java.util.ArrayList;

public class NetworkCommands {
	ArrayList<NetworkCommand> commands = new ArrayList<NetworkCommand>();
	Long gameTick;
	int clientID;
	public NetworkCommands(){
		
	}
	public NetworkCommands(Long g, int clientID){
		gameTick = g;
		this.clientID=clientID;
	}
	public void addCommand(Object o){
		commands.add(new NetworkCommand(o));
	}
	public boolean popCommand(){
		if(commands.size()>0){
		commands.remove(0);
		
		return true;
		}
		return false;
	}
	public NetworkCommand getCommand(){
		if(commands.size()>0)
		return commands.get(0);
		return null;
	}
	public Long getGameTick(){
		return gameTick;
	}
	public boolean hasCommands(){
	return	commands.size()>0;
	}
	public int getClientID(){
		return clientID;
	}
}
