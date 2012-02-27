package packets;

import Entity.EntityClass;

public class MobCreatePacket {
public EntityClass type;
public int id;
public MobCreatePacket(){
	
}
public MobCreatePacket(EntityClass type,int id){
	this.type = type;
	this.id = id;
}
}
