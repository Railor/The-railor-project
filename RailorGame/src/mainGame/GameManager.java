package mainGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import mainGame.ProgramManager.GameState;

import Entity.Player;
import Level.Keys;
import Level.Level;
import Level.Location;
import Level.Screen;

public class GameManager {
	// Create game window...
	public GameCanvas canvas;
	public static int GAME_TILE_SIZE = 64;
	BufferStrategy buffer;
	public ProgramManager pm;
	public Level level;
	public Screen screen;
	//public boolean gameRunning = false;
	public boolean gamePaused = false;
	GraphicsEnvironment ge;
	GraphicsDevice gd;
	GraphicsConfiguration gc;
	BufferedImage bi;
	Graphics graphics = null;
	Graphics2D g2d = null;
	Color background = Color.BLACK;
	int fps = 0;
	int frames = 0;
	long totalTime = 0;
	long curTime = System.currentTimeMillis();
	long lastTime = curTime;
	public Player myPlayer;
	public JFrame myFrame;
	public void starterup(JFrame app){
		pm.app.remove(pm.menuManager.mainMenuPanel);
		canvas = new GameCanvas();
		//canvas.setIgnoreRepaint(true);
		canvas.setSize(ProgramManager.SCREEN_WIDTH, ProgramManager.SCREEN_HEIGHT);
		canvas.setFocusable(false);
		app.add(canvas);
		app.pack();
		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		bi = gc.createCompatibleImage(ProgramManager.SCREEN_WIDTH, ProgramManager.SCREEN_HEIGHT);
	}
	public void startLevel(int playerID){
		starterup(myFrame);
		level = new Level(25,25,this);
		screen = new Screen(this, ProgramManager.SCREEN_WIDTH, ProgramManager.SCREEN_HEIGHT);
		Player p = new Player(0, 0);
		level.createPlayer(p);
		p.clientID=1;
		p.setId(1);
		p = new Player(200, 0);
		level.createPlayer(p);
		p.clientID=2;
		p.setId(2);
		p = new Player(0, 200);
		level.createPlayer(p);
		p.clientID=3;
		p.setId(3);
		p = new Player(200, 200);
		level.createPlayer(p);
		p.clientID=4;
		p.setId(4);
		
		for (Player k : level.players) {
			
			if(k.clientID==playerID){
				myPlayer=k;
				screen.owner=p;
			}
		}
		pm.STATE=GameState.GameScreen;
	}
	public GameManager(JFrame j, ProgramManager program) {
		this.pm = program;
		myFrame = j;
		//starterup(j);
		
	}
	public void draw(Graphics g) {
		if (pm.STATE==GameState.GameScreen) {
			screen.drawLevelMap(level, g);
			if(myPlayer!= null){
				screen.owner=myPlayer;
				screen.drawEntities(level.getEntities(), g);
			}else{
				screen.drawEntities(level.getEntities(), g);
			}
			}
	}
	public void drawCanvas(){
		try {
			
			g2d = bi.createGraphics();
			g2d.setColor(background);
			g2d.fillRect(0, 0, 639, 479);
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			draw(g2d);
			g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
			g2d.setColor(Color.RED);
			if (level != null) {
				if (pm.client != null) {
					g2d.drawString(pm.isServer ? "Server"
							: ("ClientID: " + pm.client.clientId), 20,
							20);
				}
				
			}
			g2d.drawString(String.format("FPS: %s", fps), 20, 40);

			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			graphics = buffer.getDrawGraphics();
			graphics.drawImage(bi, 0, 0, null);
		}catch(Exception e){
				
			}
			
	}
	public void run(){
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		totalTime += curTime - lastTime;
		if (totalTime > 1000) {
			totalTime -= 1000;
			fps = frames;
			frames = 0;
		}
		++frames;
		drawCanvas();
		if (pm.STATE==GameState.GameScreen) {
			if(pm.isServer && pm.server != null){
				pm.server.startTick();
			}
			if(!pm.isServer && pm.client!= null){
				pm.client.startTick();
				if(pm.client.performedTick=false){
					return;
				}
			}
			if (level != null)
				level.tick();
			if (screen != null)
				screen.tick();
			if(pm.client!=null && myPlayer != null){
				pm.client.addMessage(new Location(-1,(int)myPlayer.getX(),(int)myPlayer.getY()));
			}
			if(pm.isServer){
				pm.server.endTick();
			}
			if(!pm.isServer && pm.client!= null){
				pm.client.endTick();
			}
		}
		if(pm.STATE!=GameState.GameScreen){
			if(pm.isServer && pm.server != null){
				pm.server.startTick();
			}
			if(!pm.isServer && pm.client!= null){
				pm.client.startTick();
		}
			if(pm.isServer && pm.server != null){
				pm.server.endTick();
			}
			
			if(!pm.isServer && pm.client!= null){
				pm.client.endTick();
			}
	}
		if (!buffer.contentsLost())
			buffer.show();
		try {
			if (curTime - lastTime >= 15) {
				Thread.sleep(15);
			} else {
				Thread.sleep(curTime - lastTime);
				// System.out.println(curTime - lastTime);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (graphics != null)
				graphics.dispose();
			if (g2d != null)
				g2d.dispose();
		}
	}
	public void keyPressed(KeyEvent e){
		if (pm.STATE==GameState.GameScreen && myPlayer!= null) {
			myPlayer.keys.keyPressed(e);
		}
	}
	public void keyReleased(KeyEvent e) {
		if (pm.STATE==GameState.GameScreen && myPlayer != null) {
			myPlayer.keys.keyReleased(e);
		}
		if (e.getKeyCode() == Keys.KEY_Q) {
			System.out.println("Pressed q");
			pm.newServer();
		}
		if (e.getKeyCode() == Keys.KEY_Z) {
			System.out.println("Pressed z");
			pm.server.startGame();
			pm.STATE=GameState.GameScreen;
		}
		if (e.getKeyCode() == Keys.KEY_E) {
			System.out.println("Pressed e");
			pm.newClient();
		}
		
		if (e.getKeyCode() == Keys.KEY_B) {
			System.out.println("Pressed b");
			if(pm.isServer){
			gamePaused=!gamePaused;
			pm.server.pauseAll(!gamePaused);
			}
		}
		

	}
	public class MouseHandler implements MouseListener {
		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
}
