package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

public class FileDragGestureListener extends DragSourceAdapter implements
		DragGestureListener {

	private JTextArea text;
	private Cursor cursor;

	public FileDragGestureListener(JTextArea text) {
		this.text = text;
	}

	public void dragGestureRecognized(DragGestureEvent evt) {

		try {

			// generate the temp file
			File temp_dir = File.createTempFile("tempdir", ".dir", null);
			File temp = new File(temp_dir.getParent(), "myfile.txt");
			FileOutputStream out = new FileOutputStream(temp);
			out.write(text.getText().getBytes());
			out.close();

			// get the right icon
			FileSystemView fsv = FileSystemView.getFileSystemView();
			Icon icn = fsv.getSystemIcon(temp);

			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension dim = tk.getBestCursorSize(icn.getIconWidth(), icn
					.getIconHeight());
			BufferedImage buff = new BufferedImage(dim.width, dim.height,
					BufferedImage.TYPE_INT_ARGB);
			icn.paintIcon(text, buff.getGraphics(), 0, 0);

			// set up drag image
			if (DragSource.isDragImageSupported()) {

				evt.startDrag(DragSource.DefaultCopyDrop, buff,
						new Point(0, 0), new TextFileTransferable(temp), this);

			} else {

				cursor = tk.createCustomCursor(buff, new Point(0, 0),
						"billybob");
				evt.startDrag(cursor, null, new Point(0, 0),
						new TextFileTransferable(temp), this);

			}

		} catch (InvalidDnDOperationException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}// END: dragGestureRecognized

	public void dragEnter(DragSourceDragEvent evt) {
		DragSourceContext ctx = evt.getDragSourceContext();
		ctx.setCursor(cursor);
	}

	public void dragExit(DragSourceEvent evt) {
		DragSourceContext ctx = evt.getDragSourceContext();
		ctx.setCursor(DragSource.DefaultCopyNoDrop);
	}

}// END: FileDragGestureListener class