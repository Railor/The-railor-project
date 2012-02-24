package mainGame;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import Entity.Player;
import Level.Keys;
import Level.Level;
import Level.Screen;
import Networking.NetworkClient;
import Networking.NetworkServer;

public class ProgramManager implements KeyListener, MouseListener{
	public GameFrame app = new GameFrame();
	public GameManager gameManager;
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	public boolean isMenu = true;
	public NetworkServer server;
	public NetworkClient client;
	public boolean isServer = false;
	public boolean isClient = false;
	public MenuManager menuManager;
	public ProgramManager(){
		app.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		//app.setMaximizedBounds(new Rectangle(0, 0,SCREEN_WIDTH, SCREEN_HEIGHT));
		app.setLocation(200, 200);
		app.setFocusable(true);
		app.setIgnoreRepaint(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		app.setVisible(true);
		menuManager = new MenuManager(app,this);
		gameManager = new GameManager(app,this);
		app.addKeyListener(this);
		app.addMouseListener(this);
	}
	public void run(){
		if(gameManager.gameRunning)
		gameManager.run();
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
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("keypressed e");
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void keyPressed(KeyEvent e) {
		//if(gameManager.gameRunning)
			gameManager.keyPressed(e);
			if(isMenu)
				menuManager.keyPressed(e);
	}
	public void keyReleased(KeyEvent e) {
	//	if(gameManager.gameRunning)
			gameManager.keyReleased(e);
			if(isMenu)
				menuManager.keyReleased(e);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
