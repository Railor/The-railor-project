package mainGame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import MapEditor.EditorManager;
import Networking.NetworkClient;
import Networking.NetworkServer;

public class ProgramManager implements KeyListener, MouseListener {
	public GameFrame app = new GameFrame();
	public GameManager gameManager;
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	public String connectip = "127.0.0.1";
	public int updateRange = 1100;
	JMenuBar menuBarEditor;
	JMenu menuEditorFile;
	JMenuItem menuItemMenuScreen;
	JMenuItem menuItemEditorSave;
	JMenuItem menuItemEditorLoad;
	JMenuItem menuItemExit;

	public enum GameState {
		MenuScreen, GameScreen, EditorScreen
	}

	public static GameState STATE = GameState.MenuScreen;
	public NetworkServer server;
	public NetworkClient client;
	public boolean isServer = false;
	public boolean isClient = false;
	public MenuManager menuManager;
	public EditorManager editorManager;

	public ProgramManager() {
		app.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		// app.setMaximizedBounds(new Rectangle(0, 0,SCREEN_WIDTH,
		// SCREEN_HEIGHT));
		app.setLocation(200, 200);
		app.setFocusable(true);
		//app.setIgnoreRepaint(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		menuManager = new MenuManager(app, this);
		gameManager = new GameManager(app, this);
		editorManager = new EditorManager(app, this);

		menuBarEditor = new JMenuBar();
		menuEditorFile = new JMenu("File");
		menuItemMenuScreen = new JMenuItem("Menu Screen");
		menuItemEditorSave = new JMenuItem("Save Game");
		menuItemEditorSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorManager.saveMap();
			}
		});
		menuItemEditorLoad = new JMenuItem("Load Game");
		menuItemEditorLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editorManager.loadMap();
			}
		});
		menuItemExit = new JMenuItem("Exit");
		menuItemMenuScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				STATE = GameState.MenuScreen;
				showMenuManagerAgain();
			}
		});
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});
		menuBarEditor.add(menuEditorFile);
		menuEditorFile.add(menuItemMenuScreen);
		menuEditorFile.add(menuItemEditorSave);
		menuEditorFile.add(menuItemEditorLoad);
		menuEditorFile.add(menuItemExit);
		app.setJMenuBar(menuBarEditor);

		app.addKeyListener(this);
		app.addMouseListener(this);
		app.setVisible(true);
	}
	public void showMenuManagerAgain(){
		if(gameManager.canvas != null)
		app.remove(gameManager.canvas);
		if(editorManager.canvas != null)
		app.remove(editorManager.canvas);
		app.setSize(ProgramManager.SCREEN_WIDTH,ProgramManager.SCREEN_HEIGHT);
		menuManager = new MenuManager(app,this);
		app.repaint();
	}
	public void run() {
		if (STATE == GameState.GameScreen)
			gameManager.run();
		else if (STATE == GameState.EditorScreen) {
			editorManager.run();
		} else {
			
			app.repaint();
		}
		
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
		if (STATE == GameState.GameScreen) {
			// gameManager.keyReleased(e);
		} else if (STATE == GameState.MenuScreen) {
			// menuManager.keyReleased(e);
		} else if (STATE == GameState.EditorScreen) {
			editorManager.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		// if(gameManager.gameRunning)
		if (STATE == GameState.GameScreen)
			gameManager.keyPressed(e);
		else if (STATE == GameState.MenuScreen)
			menuManager.keyPressed(e);
		else if (STATE == GameState.EditorScreen)
			editorManager.keyPressed(e);

	}

	public void keyReleased(KeyEvent e) {
		if (STATE == GameState.GameScreen)
			gameManager.keyReleased(e);
		else if (STATE == GameState.MenuScreen)
			menuManager.keyReleased(e);
		else if (STATE == GameState.EditorScreen)
			editorManager.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
