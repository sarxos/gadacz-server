package com.sarxos.gadacz;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.theme.SubstanceSteelBlueTheme;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import com.sarxos.gadacz.server.GadaczServer;


/**
 * Control panel for Gadacz server. User can start and stop server from this
 * frame.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class GadaczServerApp extends JFrame {

	private static final long serialVersionUID = 1L;

	class ServerThread extends Thread {

		protected GadaczServer server = null;

		@Override
		public void run() {
			try {
				server = new GadaczServer();
				server.runServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public GadaczServer getServerObject() {
			return server;
		}
	};

	ServerThread sth = new ServerThread();

	JToggleButton runButton = null;
	JToggleButton stopButton = null;

	BufferedImage image = null;

	public GadaczServerApp() {

		setPreferredSize(new Dimension(240, 100));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Gadacz Server");
		setLayout(new AbsoluteLayout());
		setResizable(false);
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("com/sarxos/gadacz/resources/gadacz_small.png")).getImage());

		GadaczToolkit.setCenteredWindow(this);

		runButton = new JToggleButton();
		runButton.setFocusable(false);
		runButton.setAction(
				new AbstractAction("Run serwer") {

					private static final long serialVersionUID = -5061681038134921168L;

					public void actionPerformed(ActionEvent e) {
						if (!sth.isAlive()) {
							sth.start();
						}
					}
				}
		);

		stopButton = new JToggleButton();
		stopButton.setFocusable(false);
		stopButton.setAction(
				new AbstractAction("Stop serwer") {

					private static final long serialVersionUID = 2178722253608238022L;

					public void actionPerformed(ActionEvent e) {
						if (!sth.isInterrupted()) {
							sth.getServerObject().stopServer();
							sth.interrupt();
							System.exit(2);
						}
					}
				}
		);

		ButtonGroup buttony = new ButtonGroup();
		buttony.add(runButton);
		buttony.add(stopButton);

		ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource("com/sarxos/gadacz/resources/gadacz_big.png"));
		image = new BufferedImage(
				ii.getIconWidth(),
				ii.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D g2 = image.createGraphics();
		ii.paintIcon(null, g2, 0, 0);
		g2.dispose();

		JPanel img_panel = new JPanel() {

			private static final long serialVersionUID = -4912012695756009601L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			};
		};

		add(runButton, new AbsoluteConstraints(10, 10, 150, 20));
		add(stopButton, new AbsoluteConstraints(10, 40, 150, 20));
		add(img_panel, new AbsoluteConstraints(170, 10, 50, 50));

		pack();
		setVisible(true);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
			SubstanceLookAndFeel.setCurrentTheme(new SubstanceSteelBlueTheme());
			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(
				new Runnable() {

					public void run() {
						new GadaczServerApp();
					}
				}
		);
	}

}
