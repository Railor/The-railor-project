package MapEditor;

import java.awt.Color;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import mainGame.GameManager;
import mainGame.ProgramManager;
import Level.Keys;
import Level.Level;
import Level.Screen;
import Level.Tile;

public class EditorManager implements MouseListener,MouseMotionListener,MouseWheelListener{
	public EditorCanvas canvas;
	public static int GAME_TILE_SIZE = 64;
	BufferStrategy buffer;
	public ProgramManager pm;
	public Level level;
	public Screen screen;
	public JFrame myFrame;
	GraphicsEnvironment ge;
	GraphicsDevice gd;
	GraphicsConfiguration gc;
	BufferedImage bi;
	Graphics graphics = null;
	Graphics2D g2d = null;
	Color background = Color.BLACK;
	long totalTime = 0;
	long curTime = System.currentTimeMillis();
	long lastTime = curTime;
	public int scrollSpeed = 9;
	public Keys keys = new Keys();
	public String currentMapName = "level.txt";
	public int currentTileId = 0;
	int tileSize = GameManager.GAME_TILE_SIZE;
	int tileCountWidth = 6;
	int tileCountHeight = 8;
	public boolean mouseDown = false;
	//Create a file chooser
	JFileChooser fc;
	//In response to a button click:
	
	public void starterup() {

		pm.app.remove(pm.menuManager.mainMenuPanel);
		canvas = new EditorCanvas();
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		// canvas.setIgnoreRepaint(true);
		canvas.setSize(ProgramManager.SCREEN_WIDTH + 384,
				ProgramManager.SCREEN_HEIGHT);
		canvas.setFocusable(false);
		myFrame.add(canvas);
		myFrame.pack();
		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		bi = gc.createCompatibleImage(ProgramManager.SCREEN_WIDTH + 384,
				ProgramManager.SCREEN_HEIGHT);
		try {
			fc = new JFileChooser(new File("").getCanonicalPath() + "/maps/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startEditor() {
		starterup();
		//level = new Level("level.txt", this);
		level = new Level(50,50,this);
		screen = new Screen(this, ProgramManager.SCREEN_WIDTH,
				ProgramManager.SCREEN_HEIGHT);
	}

	public EditorManager(JFrame j, ProgramManager program) {
		this.pm = program;
		myFrame = j;

	}
	public void saveMap(){
		//fc.setSelectedFile(new File("fileToSave.txt"));
		 int returnVal = fc.showSaveDialog(pm.app);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            writeCurrentMapToFile(file.getAbsolutePath());
	            //This is where a real application would open the file.
	        } else {
	        }
	}
	public void loadMap(){
		int returnVal = fc.showOpenDialog(pm.app);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            loadMap(file.getAbsolutePath());
            //This is where a real application would open the file.
        } else {
        }
	}
	public void drawSideTiles(Graphics g) {

		for (int x = 0; x < tileCountWidth * tileCountHeight; x++) {
			// System.out.println(x);
			if (Art.Art.getTileFromTileNumber(x) != null) {
				g.drawImage(Art.Art.getTileFromTileNumber(x).getSprite(),
						ProgramManager.SCREEN_WIDTH + x % tileCountWidth
								* tileSize, x / tileCountWidth * tileSize, null);
				// g.drawImage(Art.Art.getTileFromTileNumber(x).getSprite(),
				// x%tileCountHeight * tileSize + ProgramManager.SCREEN_WIDTH,
				// x/tileCountWidth* tileSize + ProgramManager.SCREEN_HEIGHT,
				// null);
				System.out.println(x);
			} else {
				// System.out.println("BREAKYY");
				x = tileCountWidth * tileCountHeight;
				break;

				// }
			}
		}

	}

	public void drawCanvas() {
		try {

			g2d = bi.createGraphics();
			g2d.setColor(background);
			g2d.fillRect(0, 0, 1000, 479);
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			draw(g2d);
			g2d.fillRect(ProgramManager.SCREEN_WIDTH, 0, 300,
					ProgramManager.SCREEN_HEIGHT);
			drawSideTiles(g2d);

			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			graphics = buffer.getDrawGraphics();
			graphics.drawImage(bi, 0, 0, null);
		} catch (Exception e) {

		} finally {
			if (graphics != null)
				graphics.dispose();
			if (g2d != null)
				g2d.dispose();
		}

	}

	public void run() {
		controlKeys();
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		totalTime += curTime - lastTime;
		drawCanvas();
		//if(mouseDown)
		//TileSelect();
		if (screen != null)
			screen.tick();
		if (!buffer.contentsLost())
			buffer.show();
		try {
			if (curTime - lastTime >= 11) {
				Thread.sleep(11);
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

	public void draw(Graphics g) {

		// if (pm.STATE == GameState.EditorScreen) {
		screen.drawLevelMap(level, g);
		// }
	}

	public void controlKeys() {
		if (keys.isKeyDown(Keys.KEY_UP)) {
			screen.addScreenY(-scrollSpeed);
		}
		if (keys.isKeyDown(Keys.KEY_RIGHT)) {
			screen.addScreenX(scrollSpeed);
		}
		if (keys.isKeyDown(Keys.KEY_DOWN)) {
			screen.addScreenY(scrollSpeed);
		}
		if (keys.isKeyDown(Keys.KEY_LEFT)) {
			screen.addScreenX(-scrollSpeed);
		}

	}

	public void writeCurrentMapToFile(String place) {
		FileWriter fstream;
		try {
			// System.out.println("outputeded");
			fstream = new FileWriter(place);

			BufferedWriter out = new BufferedWriter(fstream);
			Tile[][] tiles = level.getLevelMap();
			out.write(",SIZEX," + level.width + ",");
			out.write("SIZEY," + level.height + ",");
			for (int x = 0; x < level.width; x++) {
				for (int y = 0; y < level.height; y++) {
					// out.write("hey");
					out.write(tiles[x][y].id + ",");
				}
			}
			out.close();

		} catch (IOException d) {
			// System.out.println("outputeded");
			// TODO Auto-generated catch block
			d.printStackTrace();
		}
	}
	public void loadMap(String place) {
		level = new Level(place,this);
	}

	public void keyPressed(KeyEvent e) {
		keys.keyPressed(e);
		if (e.getKeyCode() == Keys.KEY_B) {
			
			//writeCurrentMapToFile();
		}

	}

	public void TileSelect(MouseEvent e) {
		int mx = e.getX(), my = e.getY();
		int ox, oy;// off screen select
		//System.out.println(mx + "<MX MY>" + my);
		
		if (mx > ProgramManager.SCREEN_WIDTH) {
			ox = (mx - ProgramManager.SCREEN_WIDTH)	/ 64;
			oy = my / 64;
			//System.out.println(ox + "eee" + oy);
			if (Art.Art.getTileFromTileNumber(ox + oy * tileCountWidth) != null) {
				currentTileId = (ox + oy * tileCountWidth);
			}
		}else{
			int offsetx = screen.screenX / GameManager.GAME_TILE_SIZE;
			//System.out.println(offsetx);
			int offsety = screen.screenY / GameManager.GAME_TILE_SIZE;
			//System.out.println(offsety);
			ox = (mx + screen.screenX)	/ GameManager.GAME_TILE_SIZE;
			oy = (my + screen.screenY) / GameManager.GAME_TILE_SIZE;

			level.levelMap[ox][oy].id=currentTileId;
		}
		
		//System.out.println(currentTileId);
	}

	public void mousePressed(MouseEvent e) {
		mouseDown=true;
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			TileSelect(e);
		}

	}

	public void keyReleased(KeyEvent e) {
		keys.keyReleased(e);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseDown=true;
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			
			
		}
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
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON1)
		mouseDown=false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("EWW");
			TileSelect(e);
	}
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("EWW");
		//System.out.println(mouseDown);
		
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if(GameManager.GAME_TILE_SIZE + e.getWheelRotation() < 64 && GameManager.GAME_TILE_SIZE + e.getWheelRotation() > 3){
			
			//	if(e.getWheelRotation() < 0)
			screen.addScreenX((GAME_TILE_SIZE) * e.getWheelRotation());
			screen.addScreenY((GAME_TILE_SIZE) * e.getWheelRotation());
			System.out.println(screen.screenX);
			
		GameManager.GAME_TILE_SIZE+=e.getWheelRotation();
		}
	
	}

}
