package Level;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Keys {
	public static final int KEY_LEFT = KeyEvent.VK_A;
	public static final int KEY_DOWN = KeyEvent.VK_S;
	public static final int KEY_RIGHT = KeyEvent.VK_D;
	public static final int KEY_UP = KeyEvent.VK_W;
	public static final int KEY_Q = KeyEvent.VK_Q;
	public static final int KEY_E = KeyEvent.VK_E;
	public static final int KEY_Z = KeyEvent.VK_Z;
	public Key key_up = new Key(KEY_UP);
	public Key key_left = new Key(KEY_LEFT);
	public Key key_down = new Key(KEY_DOWN);
	public Key key_right = new Key(KEY_RIGHT);
	public Key key_q = new Key(KEY_Q);
	ArrayList<Key> keys = new ArrayList<Key>();
	public Keys(){
		keys.add(key_up);
		keys.add(key_down);
		keys.add(key_left);
		keys.add(key_right);
	}
	public void keyPressed(KeyEvent e){
		for(Key k : keys){
			if(e.getKeyCode()==k.keyCode){
				k.keyPressed();
			}
		}
	}
	public void keyPressed(int e){
		for(Key k : keys){
			if(e==k.keyCode){
				k.keyPressed();
			}
		}
	}
	public void keyReleased(int e){
		for(Key k : keys){
			if(e==k.keyCode){
				k.keyReleased();
			}
		}
	}
	public void keyReleased(KeyEvent e){
		for(Key k : keys){
			if(e.getKeyCode()==k.keyCode){
				k.keyReleased();
			}
		}
	}
	public boolean isKeyDown(int key){
		for(Key k : keys){
			if(key==k.keyCode){
				return k.getIsDown();
			}
		}
		return false;
	}
	
}
