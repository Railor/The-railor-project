package mainGame;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import MapEditor.EditorManager;
import Networking.NetworkClient;
import Networking.NetworkServer;

public class ProgramManager implements KeyListener, MouseListener{
	public GameFrame app = new GameFrame();
	public GameManager gameManager;
	public static int SCREEN_WIDTH = 1000;
	public static int SCREEN_HEIGHT = 1000;
	public String connectip = "127.0.0.1";
	public int updateRange = 1100;
	public enum GameState {
		MenuScreen,GameScreen,EditorScreen
	}
	public static GameState STATE = GameState.MenuScreen;
	public NetworkServer server;
	public NetworkClient client;
	public boolean isServer = false;
	public boolean isClient = false;
	public MenuManager menuManager;
	public EditorManager editorManager;
	public ProgramManager(){
		app.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		//app.setMaximizedBounds(new Rectangle(0, 0,SCREEN_WIDTH, SCREEN_HEIGHT));
		app.setLocation(200, 200);
		app.setFocusable(true);
		app.setIgnoreRepaint(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		menuManager = new MenuManager(app,this);
		gameManager = new GameManager(app,this);
		editorManager = new EditorManager(app,this);
		
		app.addKeyListener(this);
		app.addMouseListener(this);
		app.setVisible(true);
	}
	public void run(){
		if(STATE == GameState.GameScreen)
			gameManager.run();
		else if(STATE == GameState.EditorScreen){
			editorManager.run();
		}
		else{
			
		}
		app.repaint();
	}
	public void startGame() {
		
	}
	
	public void newServer() {
		server = new NetworkServer(this);
		isServer = true;
	}
	public void newClient() {
		client = new NetworkClient(this);
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MP");
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MP");
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MP");
	}
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("MP");
		// TODO Auto-generated method stub
		if(STATE == GameState.GameScreen){
			//gameManager.keyReleased(e);
		}
		else if(STATE == GameState.MenuScreen){
			//	menuManager.keyReleased(e);
		}
		else if(STATE == GameState.EditorScreen){
			editorManager.mousePressed(e);
	}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void keyPressed(KeyEvent e) {
		//if(gameManager.gameRunning)
		if(STATE == GameState.GameScreen)
			gameManager.keyPressed(e);
		else if(STATE == GameState.MenuScreen)
				menuManager.keyPressed(e);
		else if(STATE == GameState.EditorScreen)
			editorManager.keyPressed(e);
			
	}
	public void keyReleased(KeyEvent e) {
		if(STATE == GameState.GameScreen)
			gameManager.keyReleased(e);
		else if(STATE == GameState.MenuScreen)
				menuManager.keyReleased(e);
		else if(STATE == GameState.EditorScreen)
			editorManager.keyReleased(e);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
