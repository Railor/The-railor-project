package Networking;

public class NetworkCommand {
	Object command;
public NetworkCommand(Object c){
	command = c;
}
public NetworkCommand(){
	
}
public Object getObject(){
	return command;
}
}
