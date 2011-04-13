package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PolygonMapApp {

	// Dimension
	Dimension dimension;

	// Frame
	private JFrame frame;
	private JTabbedPane tabbedPane;

	// Menubar
	private JMenuBar mainMenu;

	// Buttons with options
	private JButton quit;

	// Tabs
	private DrawPolygonMapTab drawPolygonMapTab;

	public PolygonMapApp() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		Toolkit.getDefaultToolkit().setDynamicLayout(true);

		// Setup Main Frame
		frame = new JFrame("S.P.R.E.A.D.");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.addWindowListener(new ListenCloseWdw());

		// Setup Main Menu buttons
		quit = new JButton("Quit");

		// Add Main Menu buttons listeners
		quit.addActionListener(new ListenMenuQuit());

		// Setup Main Menu
		mainMenu = new JMenuBar();
		mainMenu.setLayout(new BorderLayout());
		JPanel buttonsHolder = new JPanel();
		buttonsHolder.setOpaque(false);
		buttonsHolder.add(quit);
		mainMenu.add(buttonsHolder, BorderLayout.EAST);

		// Setup Tabbed Pane
		tabbedPane = new JTabbedPane();

		// add DrawPolygonMap
		drawPolygonMapTab = new DrawPolygonMapTab();
		tabbedPane.add("Draw Map Polygons", drawPolygonMapTab);

		frame.setJMenuBar(mainMenu);
		frame.add(tabbedPane, BorderLayout.CENTER);
		frame.getContentPane().add(Box.createVerticalStrut(15),
				BorderLayout.SOUTH);
		frame.pack();

	}

	public class ListenMenuQuit implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			System.exit(0);
		}
	}

	public class ListenCloseWdw extends WindowAdapter {
		public void windowClosing(WindowEvent ev) {
			System.exit(0);
		}
	}

	public void launchFrame() {

		// Display Frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(dimension.width - 100,
				dimension.height - 100));
		frame.setMinimumSize(new Dimension(260, 100));
		frame.setResizable(true);
		frame.setVisible(true);
	}

	public static void main(String args[]) {

		// Start application's GUI from Event Dispatching Thread
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				PolygonMapApp gui;

				try {

					gui = new PolygonMapApp();
					gui.launchFrame();

				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		});
	}// END: main

}// END: TestlabOutbreakApp