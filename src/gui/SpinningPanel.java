package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SpinningPanel extends JPanel {

	// Sizing constants
	private final int leftPanelWidth = 200;

	protected SpinWidget spinWidget;
	public Component bottomComponent;
	public String label;

	public static final int SPIN_WIDGET_HEIGHT = 10;

	public SpinningPanel(Component bottomComponent, String label) {
		this.bottomComponent = bottomComponent;
		this.label = label;
		doMyLayout();
	}

	protected void doMyLayout() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GradientPanel labelPanel = new GradientPanel(new Color(185, 195, 210),
				Color.WHITE);
		labelPanel.setMaximumSize(new Dimension(leftPanelWidth + 60, 30));

		spinWidget = new SpinWidget();
		labelPanel.add(spinWidget);

		JLabel jlabel = new JLabel(label);
		jlabel.setHorizontalTextPosition(JLabel.CENTER);
		labelPanel.add(jlabel);

		labelPanel.setBorder(BorderFactory.createLineBorder(new Color(154, 164,
				183)));
		add(labelPanel);

		add(bottomComponent);
		resetBottomVisibility();
	}

	private void resetBottomVisibility() {
		if ((bottomComponent == null) || (spinWidget == null))
			return;
		bottomComponent.setVisible(spinWidget.isOpen());
		revalidate();
	}

	public void showBottom(boolean b) {
		spinWidget.setOpen(b);
	}

	public boolean isBottomShowing() {
		return spinWidget.isOpen();
	}

	public class SpinWidget extends JPanel {

		private static final int SPIN_WIDGET_HEIGHT = 15;
		private Dimension mySize = new Dimension(SPIN_WIDGET_HEIGHT,
				SPIN_WIDGET_HEIGHT);
		private boolean open;
		private final int HALF_HEIGHT = SPIN_WIDGET_HEIGHT / 2;
		private int[] openXPoints = { 1, HALF_HEIGHT, SPIN_WIDGET_HEIGHT - 1 };

		private int[] openYPoints = { HALF_HEIGHT, SPIN_WIDGET_HEIGHT - 1,
				HALF_HEIGHT };
		private int[] closedXPoints = { 1, 1, HALF_HEIGHT };
		private int[] closedYPoints = { 1, SPIN_WIDGET_HEIGHT - 1, HALF_HEIGHT };
		private Polygon openTriangle = new Polygon(openXPoints, openYPoints, 3);
		private Polygon closedTriangle = new Polygon(closedXPoints,
				closedYPoints, 3);

		public SpinWidget() {
			setOpen(false);
			addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					handleClick();
				}
			});
		}

		public void handleClick() {
			setOpen(!isOpen());
		}

		public boolean isOpen() {
			return open;
		}

		public void setOpen(boolean o) {
			open = o;
			resetBottomVisibility();
		}

		public Dimension getMinimumSize() {
			return mySize;
		}

		public Dimension getPreferredSize() {
			return mySize;
		}

		// don't override update( ), get the default clear
		public void paint(Graphics g) {

			g.setColor(new Color(100, 104, 111));

			if (isOpen()) {
				g.fillPolygon(openTriangle);
			} else {
				g.fillPolygon(closedTriangle);
			}

		}// END: paint

	}// END: SpinWidget class

}