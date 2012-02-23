package Level;

public class Location {
int x,y,id;
public Location(int id,int x, int y){
	this.x = x;
	this.y = y;
	this.id = id;
}
public Location(){
	
}
public int getX(){
	return x;
}

public int getY(){
	return y;
}
public int getID(){
	return id;
}
public void setX(int x){
	this.x = x;
}
public void setY(int y){
	this.y = y;
}
public void setID(int i){
	id = i;
}
}
