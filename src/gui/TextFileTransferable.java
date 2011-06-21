package gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TextFileTransferable implements Transferable {

	private File temp;

	public TextFileTransferable(File temp) throws IOException {

		this.temp = temp;

	}// END: TextFileTransferable

	public Object getTransferData(DataFlavor flavor) {

		ArrayList<File> list = new ArrayList<File>();
		list.add(temp);

		return list;
	}// END: getTransferData

	public DataFlavor[] getTransferDataFlavors() {

		DataFlavor[] df = new DataFlavor[1];
		df[0] = DataFlavor.javaFileListFlavor;

		return df;
	}// END: getTransferDataFlavors

	public boolean isDataFlavorSupported(DataFlavor flavor) {

		if (flavor == DataFlavor.javaFileListFlavor) {
			return true;
		}

		return false;
	}// END: isDataFlavorSupported

}// END: TextFileTransferable
