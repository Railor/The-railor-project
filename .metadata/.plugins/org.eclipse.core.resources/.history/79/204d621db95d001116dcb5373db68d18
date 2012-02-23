package Level;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


import Entity.Entity;
import Entity.Player;
import Networking.NetworkClient;
import Networking.NetworkServer;
import Networking.TurnSynchronizer;

public class RailorComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = GAME_WIDTH * 3 / 4;
	public static final int TILE_SIZE = 64;
	private int TILE_FILE_SIZE = 64;
	public boolean running = true;
	public String message = "thread1";
	public Level level;
	public Screen screen;
	int count = 0;
	BufferedImage tee = null;
	BufferedImage img = null;
	Keys keys = new Keys();
	long lastUpdate = 0;
	public NetworkServer server;
	public NetworkClient client;
	public Player myPlayer;
	public ArrayList<String> chatText = new ArrayList<String>();
	public boolean started = false;
	public TurnSynchronizer turnSynchronizer = new TurnSynchronizer();
	public boolean multiplayer = false;
	public boolean isServer = false;
	public BufferedImage[] tiles = new BufferedImage[10];
	public BufferedImage[] entityGraphics = new BufferedImage[10];

	public RailorComponent() {
		super();
		this.setFocusable(true);
		this.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
		this.setMinimumSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		this.setMaximumSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		this.setVisible(true);
		this.setBackground(Color.BLACK);
		this.setIgnoreRepaint(true);
		this.addMouseListener(new MouseHandler());
		this.addKeyListener(new KeyHandler());
		setSize(GAME_WIDTH, GAME_HEIGHT);
		loadImages();
		this.setVisible(true);

	}

	public void startGame(int playerID) {
		level = new Level(20, 20, this);
		screen = new Screen(this, GAME_WIDTH, GAME_HEIGHT);
		Player p = new Player(0, 0);
		level.createPlayer(p);
		p.clientID=1;
		p = new Player(100, 0);
		level.createPlayer(p);
		p.clientID=2;
		p = new Player(0, 100);
		level.createPlayer(p);
		p.clientID=3;
		p = new Player(100, 100);
		level.createPlayer(p);
		started = true;
		p.clientID=4;
		for (Player k : level.players) {
			
			if(k.clientID==playerID){
				//System.out.println("RC START GAME: Player ClientID:" + k.clientID + "MY CLIENTID: " + playerID);
				myPlayer=k;
				screen.owner=p;
			}
		}
		 
	}

	public void newServer() {
		server = new NetworkServer(this);
		isServer = true;
		client = null;
		this.setSize(600,800);
		this.setBounds(0,0,800,800);
	}

	public void newClient() {
		client = new NetworkClient(this);
		// isServer = false;
		if (server != null) {
			// server.shutDown();
			// server=null;
		}
	}

	public void loadImages() {
		try {
			img = ImageIO.read(new File("src/Art/tiles.png"));
			tee = img.getSubimage(0, 0, TILE_FILE_SIZE, TILE_FILE_SIZE);
			tiles[0] = tee;
			tee = img.getSubimage(64, 0, TILE_FILE_SIZE, TILE_FILE_SIZE);
			tiles[1] = tee;
			img = ImageIO.read(new File("src/Art/entities.png"));
			tee = img.getSubimage(0, 0, TILE_FILE_SIZE, TILE_FILE_SIZE);
			entityGraphics[0] = tee;

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load images");
		}
	}

	public void run() {
		if (started) {
			level.gameTick++;
			if(isServer && server != null){
				server.startTick();
			}
			if(!isServer && client!= null){
				client.startTick();
				if(client.performedTick=false){
					level.gameTick--;
					return;
				}
			}
			if (level != null)
				level.tick();
			if (screen != null)
				screen.tick();
			/*
			if (isServer && server != null) {
				for (Key k : level.players.get(0).keys.keys) {
					if (k.getJustChanged()) {
						server.sendMessage(k);
						k.setJustChanged(false);
					}
				}
			}
			*/
			if(client!=null){
				//System.out.println(clientID);
				client.addMessage(new Location(-1,myPlayer.getX(),myPlayer.getY()));
				/*
				for (Key k : myPlayer.keys.keys) {
					if (k.getJustChanged()) {
						client.addMessage(k);
						k.setJustChanged(false);
					}
				}
				*/
				
			}
				// System.out.println("ID="+level.players.get(0).getId()+", X="
				// + level.players.get(0).getX() + ", Y=" +
				// level.players.get(0).getY());
				//for (Entity e : getEntitiesInRange(GAME_WIDTH,
					//	level.players.get(0))) {
					//server.sendMessage(new Location(e.getId(), e.getX(), e
						//	.getY()));

				//}
				//addText(Long.toString(level.gameTick));
				//addText("meow");
				// server.sendMessage(level.players.get(0).keys.key_left);
				// server.sendMessage(level.players.get(0).keys.key_up);
				// server.sendMessage(level.players.get(0).keys.key_right);
			
			
			
			//TurnSynchronizer.synchedRandom.setSeed(TurnSynchronizer.synchedSeed+level.gameTick);
			if(isServer){
				server.endTick();
			}
			
			if(!isServer && client!= null){
				client.endTick();
			}
			
			
			
			
		}else{
			if (!isServer) {
				if (client != null) {
					//String str = "CLIENT: Tick: " +  " Number:" + TurnSynchronizer.synchedRandom.nextInt(20);
					//client.sendMessage(str);
				}
			}else {
			//	System.out.println("SERVER: Tick: " +  " Number:" + TurnSynchronizer.synchedRandom.nextInt(20));
			}
			
		}
	}

	

	public void draw(Graphics g) {
		if (started) {
			screen.drawLevelMap(level, g);
			if(myPlayer!= null){
				screen.owner=myPlayer;
				screen.drawEntities(level.getEntities(), g);
			//}else
				//level.players.get(0).screen.drawEntities(level.getEntities(), g);
			
			}else{
				screen.drawEntities(level.getEntities(), g);
			}
			}
		//if(isServer){
			
			//for(int count = 0; count < chatText.size();count++){
			//	g.setColor(Color.BLUE);
		//		g.drawString(chatText.get(count), 20, count*15+50);
		//	}
			
		
		
	}
	public void addText(String str){
		if(chatText.size()>=30){
			chatText.remove(0);
			chatText.add(str);
		}else{
			chatText.add(str);
		}
	}
	public void addText(int stre){
		String str = (Integer.toString(stre));
		if(chatText.size()>=30){
			chatText.remove(0);
			chatText.add(str);
		}else{
			chatText.add(str);
		}
	}
	public void addText(double stre){
		String str = (Double.toString(stre));
		if(chatText.size()>=30){
			chatText.remove(0);
			chatText.add(str);
		}else{
			chatText.add(str);
		}
	}
	public void paint(Graphics g) {
	}

	public void update(Graphics g) {
		paint(g);
	}

	public class KeyHandler implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (started && myPlayer!= null) {
				myPlayer.keys.keyPressed(e);
			}
				/*
				for (Entity x : level.entities) {

					if (x instanceof Player) {

						Player temp = (Player) x;
						temp.keys.keyPressed(e);
					}
				}
			}
			*/

		}

		public void keyReleased(KeyEvent e) {
			if (started && myPlayer != null) {
				myPlayer.keys.keyReleased(e);
				/*
				for (Entity x : level.entities) {
					if (x instanceof Player) {
						Player temp = (Player) x;
						temp.keys.keyReleased(e);
					}
				}
				*/
			}
			if (e.getKeyCode() == Keys.KEY_Q) {
				System.out.println("Pressed q");
				newServer();
			}
			if (e.getKeyCode() == Keys.KEY_Z) {
				System.out.println("Pressed z");
				server.startGame();
			}
			if (e.getKeyCode() == Keys.KEY_E) {
				System.out.println("Pressed e");
				newClient();
			}
		}

		public void keyTyped(KeyEvent e) {
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
