package mainGame;
/*************************************************************
         * @file: TextPanel.java 
	 * @source: adapted from Horstmann and Cornell, Core Java
	 * @history: Visualization Course, Spring 2003, Chee Yap
	 *************************************************************/

	import java.awt.*;
	import java.awt.event.*;

import javax.swing.*;

import Level.Keys;
	
	/*************************************************************
         *	TextPanel Class (with main method)
	 *************************************************************/
	
	class TestScreen extends JPanel {
	  // override the paintComponent method
	  // THE MAIN DEMO OF THIS EXAMPLE:
	
	  public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.drawString("IP Address ", 24, 113);
	  } //paintComponent

	  //=============================================
	  ///////////// main ////////////////////////////

	  public static void main(String[] args) {
	    JFrame f = new MyFrame();
	    f.setVisible(true);
	    //SimpleWindowedGame sg = new SimpleWindowedGame();
	    
	  } //main
	
	} //class TextPanel
	
	/*************************************************************
         	MyFrame Class
	 *************************************************************/

	class MyFrame extends JFrame {
		MyFrame frame = this;
		GameCanvas gc = new GameCanvas();
		JTextField ip;
	  public MyFrame() {
		// Frame Parameters
		  
		setSize(500,500); // default size is 0,0
		setLocation(10,200); // default is 0,0 (top left corner)
		
		 ip = new JTextField();
		ip.setSize(200,20);
		ip.setLocation(100,100);
		ip.setText("127.0.0.1.");
		ip.setSelectionStart(15);
		JButton joinB = new JButton();
		joinB.setSize(200,20);
		joinB.setLocation(100,120);
		joinB.setText("Join");
		joinB.addActionListener(new ButtonHandler());
		joinB.setActionCommand("join");
		JButton hostB = new JButton();
		hostB.setSize(200,50);
		hostB.setLocation(100,150);
		hostB.setText("Host");
		hostB.addActionListener(new ButtonHandler());
		hostB.setActionCommand("host");
		JButton startB = new JButton();
		startB.setSize(200,50);
		startB.setLocation(100,200);
		startB.setText("Start Game");
		startB.addActionListener(new ButtonHandler());
		startB.setActionCommand("start");
		this.add(hostB);
		this.add(startB);
		this.add(ip);
		this.add(joinB);
		// Window Listeners
		addWindowListener(new WindowAdapter() {
		  public void windowClosing(WindowEvent e) {
			System.exit(0);
		  } //windowClosing
		}); //addWindowLister
	
		// Add Panels
		Container contentPane = getContentPane();
		contentPane.add(new TestScreen());
	 
	  } //constructor MyFrame
	  public class ButtonHandler implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getActionCommand() == "host") {
					gc.canvas.newServer();
					try {
						frame.finalize();
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (e.getActionCommand() == "start") {
					gc.canvas.server.startGame();
					gc.starterup(frame);
					try {
						frame.finalize();
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (e.getActionCommand() == "join") {
					
					gc.canvas.newClient();
					gc.canvas.client.connectIP=frame.ip.getText();
					gc.canvas.client.startClient();
					if(gc.canvas.client.client.isConnected())
					gc.starterup(frame);
					try {
						frame.finalize();
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		}
	} //class MyFrame