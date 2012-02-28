package mainGame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mainGame.ProgramManager.GameState;

import Level.Keys;

public class MenuManager {
	ProgramManager pm;
	public JPanel mainMenuPanel = new JPanel();
	JButton hostGame;
	JButton connectGame;
	JButton startGame;
	JTextField ipaddressField;
	JButton mapEditor;
	public MenuManager(JFrame frame, ProgramManager pm) {
		this.pm = pm;
		mainMenuPanel.setLayout(null);
		hostGame = new JButton();
		hostGame.setSize(200, 20);
		hostGame.setLocation(30, 200);
		hostGame.setActionCommand("host");
		if(!pm.isServer){
		hostGame.setText("Host");
		}else{
			hostGame.setText("Stop Server");
		}
		hostGame.addActionListener(new ActionListener());
		mainMenuPanel.add(hostGame);
		connectGame = new JButton();
		connectGame.setSize(200, 20);
		connectGame.setLocation(30, 220);
		if(!pm.isClient){
			connectGame.setText("Connect to Server");
			}else{
				connectGame.setText("Disconnect");
			}
			connectGame.setActionCommand("join");
		
		
		connectGame.addActionListener(new ActionListener());
		mainMenuPanel.add(connectGame);
		mapEditor = new JButton();
		mapEditor.setSize(200, 20);
		mapEditor.setLocation(30, 260);
		mapEditor.setText("Map Editor");
		mapEditor.setActionCommand("mapeditor");
		mapEditor.addActionListener(new ActionListener());
		mainMenuPanel.add(mapEditor);
		ipaddressField = new JTextField();
		ipaddressField.setSize(200, 20);
		ipaddressField.setLocation(30, 280);
		ipaddressField.setText("127.0.0.1");
		mainMenuPanel.add(ipaddressField);
		frame.add(mainMenuPanel);
		frame.repaint();
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == Keys.KEY_Q) {
			System.out.println("Pressed q");
		}
		if (e.getKeyCode() == Keys.KEY_Z) {
			System.out.println("Pressed Z");
		}
		if (e.getKeyCode() == Keys.KEY_E) {
			System.out.println("Pressed e");
		}
	}

	private class ActionListener implements java.awt.event.ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().compareTo("host") == 0) {
				if (pm.server == null) {
					hostGame.setText("Stop Server");
					pm.newServer();
					pm.server.startGame();
					ProgramManager.STATE=GameState.GameScreen;
				} else {
					hostGame.setText("Host");
					pm.server.shutDown();
					pm.server = null;
					pm.isServer= false;
				}
			}
			if (e.getActionCommand().compareTo("join") == 0) {
				
				if(pm.client == null){
				pm.connectip=ipaddressField.getText();
				pm.newClient();
				
				if(pm.client.client.isConnected()){
					connectGame.setText("Disconnect");
					
					pm.isClient = true;
				}else{
					connectGame.setText("Connect to Server");
					pm.client.shutDown();
					pm.client=null;
					pm.isClient = false;
				}
				}else{
					connectGame.setText("Connect to Server");
					pm.client.shutDown();
					pm.client=null;
					pm.isClient = false;
				}
				
			}
			if (e.getActionCommand().compareTo("mapeditor") == 0) {
				pm.editorManager.startEditor();
				ProgramManager.STATE=GameState.EditorScreen;
				
			}
		}

	}
}
