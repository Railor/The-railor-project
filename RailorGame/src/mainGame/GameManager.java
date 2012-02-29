package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

import mainGame.ProgramManager.GameState;

import Entity.Player;
import Level.Keys;
import Level.Level;
import Level.Location;
import Level.Screen;
import Mob.Mob;

public class GameManager implements MouseListener,MouseMotionListener,MouseWheelListener{
	// Create game window...
	int TICKS_PER_SECOND = 30;
    double SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    int MAX_FRAMESKIP = 5;
    int currentGameTick = 0;
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
	public int gameSpeed = 11;
	public void starterup(JFrame app){
		pm.app.remove(pm.menuManager.mainMenuPanel);
		canvas = new GameCanvas();
		//canvas.setIgnoreRepaint(true);
		canvas.setSize(ProgramManager.SCREEN_WIDTH, ProgramManager.SCREEN_HEIGHT);
		canvas.setFocusable(false);
		canvas.addMouseWheelListener(this);
		app.add(canvas);
		app.pack();
		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		bi = gc.createCompatibleImage(ProgramManager.SCREEN_WIDTH, ProgramManager.SCREEN_HEIGHT);
		//gd.setFullScreenWindow(myFrame);
	}
	public void startLevel(int playerID){
		System.out.println("START LEVEL");
		starterup(myFrame);
		System.out.println("LEVEL = nEW LEVEL");
		level = new Level(50,50,this);
		System.out.println("AFTER LEVEL");
		screen = new Screen(this, ProgramManager.SCREEN_WIDTH, ProgramManager.SCREEN_HEIGHT);
		System.out.println("Creating screen");
		ProgramManager.STATE=GameState.GameScreen;
	}
	public GameManager(JFrame j, ProgramManager program) {
		this.pm = program;
		myFrame = j;
		//starterup(j);
		
	}
	public void draw(Graphics g,double d ) {
		if (ProgramManager.STATE==GameState.GameScreen) {
			screen.drawLevelMap(level,g);
			if(myPlayer!= null){
				screen.owner=myPlayer;
				screen.drawEntities(pm.gameManager.level.getEntitiesInRange(ProgramManager.SCREEN_WIDTH + 100,screen.owner), g,d);
				
			}else{
				screen.drawEntities(level.getEntities(), g,d);
			}
			}
	}
	public void drawCanvas(double d){
		try {
			
			g2d = bi.createGraphics();
			g2d.setColor(background);
			g2d.fillRect(0, 0, 639, 479);
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			draw(g2d,d);
			g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
			g2d.setColor(Color.RED);
			if (level != null) {
				if (pm.client != null) {
					g2d.drawString(pm.isServer ? "Server"
							: ("ClientID: " + pm.client.clientId), 20,
							20);
					g2d.drawString("Entities Loaded: " + level.getEntities().size(), 20,60);
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
		
	    long next_game_tick = System.currentTimeMillis();
	    int loops;
	    double interpolation;

	    //bool game_is_running = true;
	    while( ProgramManager.STATE==GameState.GameScreen) {

	        loops = 0;
	        while( System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
	            updateGame();

	            next_game_tick += SKIP_TICKS;
	            loops++;
	        }

	        interpolation = ( System.currentTimeMillis() + SKIP_TICKS - next_game_tick ) / ( SKIP_TICKS );
	        if(ProgramManager.STATE==GameState.GameScreen)
	        displayGame( interpolation);
	       // System.out.println(interpolation);
	    }
		
	
	}
	public void updateGame(){
		 
		if (ProgramManager.STATE==GameState.GameScreen) {
			
			if(pm.isServer && pm.server != null){
				pm.server.startTick();
			}
			if(pm.client!= null){
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
			if(pm.client!= null){
				pm.client.endTick();
			}
			
		}
		if(ProgramManager.STATE!=GameState.GameScreen){
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
	}
	public void displayGame(double d){
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		totalTime += curTime - lastTime;
		if (totalTime > 1000) {
			totalTime -= 1000;
			fps = frames;
			frames = 0;
		}
		++frames;
		drawCanvas(d);
		if (!buffer.contentsLost())
			buffer.show();
		
			if (curTime - lastTime >= gameSpeed) {
				//Thread.sleep(gameSpeed);
				//Thread.sleep(0);
			} else {
				//Thread.sleep(gameSpeed - (curTime - lastTime));
				//Thread.sleep(0);
				// System.out.println(curTime - lastTime);
			}

			if (graphics != null)
				graphics.dispose();
			if (g2d != null)
				g2d.dispose();
		
	}
	public void runqwe(){
		
		displayGame(50);
		if (ProgramManager.STATE==GameState.GameScreen) {
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
		if(ProgramManager.STATE!=GameState.GameScreen){
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
			if (curTime - lastTime >= gameSpeed) {
				//Thread.sleep(gameSpeed);
				Thread.sleep(0);
			} else {
				//Thread.sleep(gameSpeed - (curTime - lastTime));
				Thread.sleep(0);
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
		if (ProgramManager.STATE==GameState.GameScreen && myPlayer!= null) {
			myPlayer.keys.keyPressed(e);
		}
	}
	public void keyReleased(KeyEvent e) {
		if (ProgramManager.STATE==GameState.GameScreen && myPlayer != null) {
			myPlayer.keys.keyReleased(e);
		}
		if (e.getKeyCode() == Keys.KEY_Q) {
			System.out.println("Pressed q");
			pm.newServer();
		}
		if (e.getKeyCode() == Keys.KEY_Z) {
			System.out.println("Pressed z");
			pm.server.startGame();
			ProgramManager.STATE=GameState.GameScreen;
		}
		if (e.getKeyCode() == Keys.KEY_E) {
			System.out.println("Pressed e");
			pm.newClient();
		}
		
		if (e.getKeyCode() == Keys.KEY_B) {
			System.out.println("Pressed b");
			if(pm.isServer){
				for (int x = 0; x < 20; x++) {
					Random rand = new Random();
					Mob p = new Mob(rand.nextInt(800),rand.nextInt(600));
					level.createEntity(p);
					//pm.server.createMob(p,1);
					//pm.server.updateMob(p,1);
				}
			gamePaused=!gamePaused;
			pm.server.pauseAll(!gamePaused);
			}
		}
		

	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
