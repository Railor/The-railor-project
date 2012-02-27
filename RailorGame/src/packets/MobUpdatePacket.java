package packets;

import Level.Location;

public class MobUpdatePacket {
public Location location;
public int dirAngle;
public boolean isMoving;
public int mobId;
public MobUpdatePacket(){
	
}
public MobUpdatePacket(int mobId,Location l, int dirAngle,boolean isMoving){
	this.mobId = mobId;
	this.location = l;
	this.dirAngle = dirAngle;
	this.isMoving = isMoving;
}
}
