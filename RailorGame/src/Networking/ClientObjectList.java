package Networking;

import java.util.ArrayList;

public class ClientObjectList {

	ArrayList<Integer> commands = new ArrayList<Integer>();
	int clientID;

	public ClientObjectList() {

	}

	public ClientObjectList(int clientID) {
		this.clientID = clientID;
	}

	public void addCommand(int o) {
		commands.add(o);
	}
	public int getIdAt(int x){
		return commands.get(x);
	}
	public boolean removeId(int i) {
		for(int x = 0; x < commands.size();x++){
			if(commands.get(x)==i){
				commands.remove(x);
				return true;
			}
			
		}
		
		return false;
	}

	public boolean containsId(int i) {
		for( Integer x : commands){
			if(i == x){
				return true; 
			}
		}
		return false;
	}

	public boolean hasCommands() {
		return commands.size() > 0;
	}

	public int getClientID() {
		return clientID;
	}
}
