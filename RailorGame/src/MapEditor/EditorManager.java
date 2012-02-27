package MapEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import mainGame.ProgramManager;
import Level.Keys;
import Level.Level;
import Level.Screen;

public class EditorManager {
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

	public void starterup() {
		pm.app.remove(pm.menuManager.mainMenuPanel);
		canvas = new EditorCanvas();
		//canvas.setIgnoreRepaint(true);
		canvas.setSize(ProgramManager.SCREEN_WIDTH,
				ProgramManager.SCREEN_HEIGHT);
		canvas.setFocusable(false);
		myFrame.add(canvas);
		myFrame.pack();
		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		bi = gc.createCompatibleImage(ProgramManager.SCREEN_WIDTH,
				ProgramManager.SCREEN_HEIGHT);
	}

	public void startEditor() {
		starterup();
		level = new Level(25, 25, this);
		screen = new Screen(this, ProgramManager.SCREEN_WIDTH,
				ProgramManager.SCREEN_HEIGHT);
	}

	public EditorManager(JFrame j, ProgramManager program) {
		this.pm = program;
		myFrame = j;

	}

	public void drawCanvas() {
		try {

			g2d = bi.createGraphics();
			g2d.setColor(background);
			g2d.fillRect(0, 0, 639, 479);
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			// **********************************************************************************************************************************************
			draw(g2d);
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

		//if (pm.STATE == GameState.EditorScreen) {
			screen.drawLevelMap(level, g);
		//}
	}
	public void controlKeys(){
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
	public void keyPressed(KeyEvent e) {
		keys.keyPressed(e);
		
	}

	public void keyReleased(KeyEvent e) {
		keys.keyReleased(e);

	}

}
