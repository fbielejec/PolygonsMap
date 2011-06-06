package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import templates.DrawPolygonMap;

@SuppressWarnings("serial")
public class DrawPolygonMapTab extends JPanel {

	// Sizing constants
	private final int leftPanelWidth = 200;
	private final int leftPanelHeight = 300;
	private Dimension dimension;

	// Colors
	private Color backgroundColor;

	// Buttons
	private JButton generateProcessing;
	private JButton saveProcessingPlot;

	// Combo boxes
	private JComboBox projectionParser;

	// Left tools pane
	private JPanel leftPanel;
	private JPanel tmpPanel;

	// Processing pane
	private DrawPolygonMap drawPolygonMap;

	// Progress bar
	private JProgressBar progressBar;

	public DrawPolygonMapTab() {

		// Setup miscallenous
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		backgroundColor = new Color(231, 237, 246);
		GridBagConstraints c = new GridBagConstraints();
		dimension = Toolkit.getDefaultToolkit().getScreenSize();

		// Setup buttons
		generateProcessing = new JButton("Plot");
		saveProcessingPlot = new JButton("Save");

		// Listeners
		generateProcessing.addActionListener(new ListenGenerateProcessing());
		saveProcessingPlot.addActionListener(new ListenSaveProcessingPlot());

		// Setup combo boxes
		String[] stringProjection = { "MERCATOR", "EQUIRECTANGULAR" };
		projectionParser = new JComboBox(stringProjection);

		// Setup progress bar
		progressBar = new JProgressBar();

		/**
		 * left tools pane
		 * */
		leftPanel = new JPanel();
		leftPanel.setBackground(backgroundColor);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension(leftPanelWidth,
				leftPanelHeight));
		// leftPanel.setMinimumSize(new Dimension(leftPanelWidth,
		// leftPanelHeight));

		tmpPanel = new JPanel();
		tmpPanel.setMaximumSize(new Dimension(leftPanelWidth + 60, 100));
		tmpPanel.setBackground(backgroundColor);
		tmpPanel.setBorder(new TitledBorder("Choose projection:"));
		tmpPanel.add(projectionParser);
		leftPanel.add(tmpPanel);

		// TODO: more info panel in work
		tmpPanel = new JPanel();
		tmpPanel.setMaximumSize(new Dimension(leftPanelWidth + 60, 100));
		tmpPanel.setLayout(new GridBagLayout());
		tmpPanel.setBackground(backgroundColor);
		tmpPanel.setBorder(new TitledBorder("Plot map:"));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		tmpPanel.add(generateProcessing, c);
		c.ipady = 7;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		tmpPanel.add(progressBar, c);
		SpinningPanel sp = new SpinningPanel(tmpPanel, "   Plotting",
				new Dimension(leftPanelWidth + 60, 20));
		sp.showBottom(true);
		leftPanel.add(sp);

		tmpPanel = new JPanel();
		tmpPanel.setMaximumSize(new Dimension(leftPanelWidth + 60, 100));
		tmpPanel.setBackground(backgroundColor);
		tmpPanel.setBorder(new TitledBorder("Save plot:"));
		tmpPanel.add(saveProcessingPlot);
		leftPanel.add(tmpPanel);

		JScrollPane leftScrollPane = new JScrollPane(leftPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		leftScrollPane.setMinimumSize(new Dimension(leftPanelWidth + 60,
				leftPanelHeight));
		add(leftScrollPane, BorderLayout.CENTER);

		/**
		 * Processing pane
		 * */
		drawPolygonMap = new DrawPolygonMap();
		drawPolygonMap.setPreferredSize(new Dimension(dimension.width,
				dimension.height));// 2048, 1025

		if (System.getProperty("java.runtime.name").toLowerCase().startsWith(
				"openjdk")) {

			JScrollPane rightScrollPane = new JScrollPane(drawPolygonMap,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			add(rightScrollPane, BorderLayout.CENTER);

		} else {

			ScrollPane rightScrollPane = new ScrollPane(
					ScrollPane.SCROLLBARS_ALWAYS);
			rightScrollPane.add(drawPolygonMap);
			add(rightScrollPane, BorderLayout.CENTER);

		}

	}// END: continuousModelTab

	private class ListenGenerateProcessing implements ActionListener {
		public void actionPerformed(ActionEvent ev) {

			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				// Executed in background thread
				public Void doInBackground() {

					try {

						generateProcessing.setEnabled(false);
						progressBar.setIndeterminate(true);

						// drawPolygonMap.setCam(drawPolygonMap.getCam());

						if (projectionParser.getSelectedIndex() == 0) {
							drawPolygonMap.setMercatorProjection();
						} else {
							drawPolygonMap.setEquirrectangularProjection();
						}

						drawPolygonMap.init();

					} catch (Exception e) {

						e.printStackTrace();

						String msg = String.format("Unexpected problem: %s", e
								.toString());

						JOptionPane.showMessageDialog(null, msg, "Error",
								JOptionPane.ERROR_MESSAGE);
					}

					return null;
				}// END: doInBackground()

				// Executed in event dispatch thread
				public void done() {

					generateProcessing.setEnabled(true);
					progressBar.setIndeterminate(false);

				}
			};

			worker.execute();
		}
	}// END: ListenGenerateProcessing

	private class ListenSaveProcessingPlot implements ActionListener {
		public void actionPerformed(ActionEvent ev) {

			try {

				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Saving as png file...");

				chooser.showSaveDialog(chooser);
				File file = chooser.getSelectedFile();
				String plotToSaveFilename = file.getAbsolutePath();

				drawPolygonMap.save(plotToSaveFilename);
				System.out.println("Saved " + plotToSaveFilename + "\n");

			} catch (Exception e) {
				System.err.println("Could not save! \n");
			}

		}// END: actionPerformed
	}// END: class

}// END class
