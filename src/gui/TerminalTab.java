package gui;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

@SuppressWarnings("serial")
public class TerminalTab extends JPanel {

	private JTextArea textArea;
	private JLabel label;

	public TerminalTab() throws IOException {

		// Setup miscallenous
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Setup label
		FileSystemView fsv = FileSystemView.getFileSystemView();
		Icon icon = fsv.getSystemIcon(File.createTempFile("myfile.", ".txt"));
		label = new JLabel("myfile.txt", icon, SwingConstants.CENTER);
		add(label);

		// Setup text area
		textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane);

		// Setup Drag&Drop
		DragSource ds = DragSource.getDefaultDragSource();
		DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(
				label, DnDConstants.ACTION_MOVE, new FileDragGestureListener(
						textArea));

		// Redirect streams
		System.setOut(new PrintStream(new JTextAreaOutputStream(textArea)));
		System.setErr(new PrintStream(new JTextAreaOutputStream(textArea)));

	}

	public void setText(String text) {
		textArea.append(text);
	}

	public void clearTerminal() {
		textArea.setText("");
	}
}
