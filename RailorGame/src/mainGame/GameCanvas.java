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
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Level.RailorComponent;

public class GameCanvas {
	// Create game window...
	public RailorComponent canvas;
	public void starterup(JFrame app){
		
		// app.setResizable(false);
		// app.setMaximumSize(new
		// Dimension(RailorComponent.GAME_WIDTH,RailorComponent.GAME_HEIGHT));
		// app.setMaximizedBounds(new
		// Rectangle(0,0,RailorComponent.GAME_WIDTH,RailorComponent.GAME_HEIGHT));
		app.setMaximumSize(new Dimension(800, 800));
		app.setMaximizedBounds(new Rectangle(0, 0, 800, 800));
		app.setLocation(200, 200);
		app.setFocusable(false);
		app.setIgnoreRepaint(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create canvas for painting...
		canvas.setIgnoreRepaint(true);
		canvas.setSize(RailorComponent.GAME_WIDTH, RailorComponent.GAME_HEIGHT);
		canvas.setFocusable(true);

		// Add canvas to game window...
		
		app.add(canvas);
		app.pack();
		app.setVisible(true);
		
		
		// Create BackBuffer...
		canvas.createBufferStrategy(2);
		BufferStrategy buffer = canvas.getBufferStrategy();

		// Get graphics configuration...
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		// Create off-screen drawing surface
		BufferedImage bi = gc.createCompatibleImage(RailorComponent.GAME_WIDTH,
				RailorComponent.GAME_HEIGHT);

		// Objects needed for rendering...
		Graphics graphics = null;
		Graphics2D g2d = null;
		Color background = Color.BLACK;
		// Variables for counting frames per seconds
		int fps = 0;
		int frames = 0;
		long totalTime = 0;
		long curTime = System.currentTimeMillis();
		long lastTime = curTime;

		while (true) {
			try {
				lastTime = curTime;
				curTime = System.currentTimeMillis();
				totalTime += curTime - lastTime;
				if (totalTime > 1000) {
					totalTime -= 1000;
					fps = frames;
					frames = 0;
				}
				++frames;
				g2d = bi.createGraphics();
				g2d.setColor(background);
				g2d.fillRect(0, 0, 639, 479);
				// **********************************************************************************************************************************************
				// **********************************************************************************************************************************************
				// **********************************************************************************************************************************************
				if (canvas.isServer) {

				}
				canvas.run();
				canvas.draw(g2d);
				g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
				g2d.setColor(Color.RED);
				if (canvas.level != null) {
					if (canvas.client != null) {
						g2d.drawString(canvas.isServer ? "Server"
								: ("ClientID: " + canvas.client.clientId), 20,
								20);
					}
					if (canvas.client != null) {
						g2d.drawString(
								"GameTick: "
										+ Long.toString(canvas.level.gameTick)
										+ "Last Server Tick: "
										+ canvas.client.lastServerTick
										+ "Diff:"
										+ (canvas.client.lastServerTick - canvas.level.gameTick),
								20, 60);
						// System.out.println("GameTick: " +
						// Long.toString(canvas.level.gameTick) +
						// "Last Server Tick: " + canvas.client.lastServerTick +
						// "Diff:" + (canvas.client.lastServerTick -
						// canvas.level.gameTick));
					}
				}
				g2d.drawString(String.format("FPS: %s", fps), 20, 40);

				// **********************************************************************************************************************************************
				// **********************************************************************************************************************************************
				// **********************************************************************************************************************************************
				graphics = buffer.getDrawGraphics();
				graphics.drawImage(bi, 0, 0, null);
				if (!buffer.contentsLost())
					buffer.show();
				try {
					if (curTime - lastTime > 16) {
						Thread.sleep(16);
					} else {
						Thread.sleep(curTime - lastTime);
						// System.out.println(curTime - lastTime);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} finally {
				if (graphics != null)
					graphics.dispose();
				if (g2d != null)
					g2d.dispose();
			}
		}
	}
	public GameCanvas() {
		canvas = new RailorComponent();
		

	}
}
