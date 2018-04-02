package FileManager;

import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

public class tableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1856536990034119777L;
	tableFrame fr;
	File file = new File(tableFrame.filename);
	String[] arr = file.list();

	tableModel(File file, String[] arr) {
		this.file = file;
		this.arr = arr;
		arraySorted(arr);
		fireTableDataChanged();

	}

	public void arraySorted(String[] array) {
		TreeSet<String> folders = new TreeSet<String>();
		TreeSet<String> files = new TreeSet<String>();

		for (String i : array) {
			if (new File(file.getAbsolutePath() + "/").isDirectory()) {
				folders.add(i);
			} else {
				files.add(i);
			}
		}

		Iterator<String> folderIter = folders.iterator();
		for (int i = 0; i < array.length; i++) {
			if (folderIter.hasNext()) {
				array[i] = folderIter.next();
			}
		}

		Iterator<String> fileIter = files.iterator();
		for (int i = 0; i < array.length; i++) {
			if (fileIter.hasNext() && array[i] != null) {
				array[i] = fileIter.next();
			}
		}
	}

	@Override
	public int getRowCount() {
		return arr.length;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Filename";

		}
		return "";
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		return arr[rowIndex];
	}
}
