package Level;

public class Key {
	private boolean isDown = false;
	private boolean justChanged = false;
	int keyCode;

	public Key() {

	}

	public Key(int key) {
		keyCode = key;
	}

	public void keyPressed() {
		if (isDown == false) {
			justChanged = true;
		} else {
			justChanged = false;
		}
		isDown = true;
	}

	public void keyReleased() {
		if (isDown == true) {
			justChanged = true;
		} else {
			justChanged = false;
		}
		isDown = false;
	}

	public boolean getIsDown() {
		return isDown;
	}

	public int getKeyCode() {
		return keyCode;
	}
	public boolean getJustChanged(){
		return justChanged;
	}
	public void setJustChanged(boolean p){
		justChanged = p;
	}
}
