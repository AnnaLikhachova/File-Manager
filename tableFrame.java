package FileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class tableFrame extends JFrame implements MouseListener, ActionListener, Runnable {

	private static final long serialVersionUID = -6413577364226023123L;
	static JFrame frame = new JFrame("FileManager");
	JPanel panel = new JPanel();
	String[] arr = file.list();
	tableModel model = new tableModel(file, arr);
	JTable table = new JTable(model);
	private JLabel label1 = new JLabel();
	private JLabel label2 = new JLabel();
	private JLabel label3 = new JLabel();
	private static TextField t = new TextField();
	static String filename = (String) JOptionPane.showInputDialog(frame, "Enter file name:\n", t.getText(),
			JOptionPane.PLAIN_MESSAGE);
	private JButton delete = new JButton("DELETE");
	private JButton back = new JButton("BACK");
	private JButton forward = new JButton("FORWARD");
	private JButton read = new JButton("READ");
	static File file = new File(filename);
	private String m = file.getAbsolutePath() + "/";

	public tableFrame() {

		frame.setSize(new Dimension(1400, 800));
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridBagLayout());
		table.setBackground(Color.blue);
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane);
		frame.add(panel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(1, 1, 1, 1), 0, 0));
		frame.add(label1, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		frame.add(label2, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		frame.add(forward, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		frame.add(back, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		frame.add(delete, new GridBagConstraints(2, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		frame.add(read, new GridBagConstraints(3, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		table.addMouseListener(this);
		frame.setVisible(true);
		new Thread(this).start();
		table.setDefaultRenderer(Object.class, new tableRenderer());

		read.addActionListener((e) -> {
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();
			Object object = table.getValueAt(row, col);
			String fileName = m + object.toString();
			System.out.println("The name of the file to read: " + fileName);
			try {
				readFile(fileName);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			table.revalidate();
			table.invalidate();
			frame.invalidate();
			frame.validate();
			frame.revalidate();
			frame.repaint();

		});

		forward.addActionListener((e) -> {
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();

			label2.setText(table.getValueAt(row, col).toString());
			label1.setText("The name of the file: ");
			Object object = table.getValueAt(row, col);
			String str = object.toString();
			m = m + "/" + str + "/";
			System.out.println(m);
			File newfile = new File(m);
			System.out.println(newfile.getAbsolutePath());
			if (new File(newfile.getAbsolutePath() + "/").isDirectory()) {
				System.out.println("Going forward: File is directory");
				String[] arr2 = newfile.list();
				tableModel model2 = new tableModel(newfile, arr2);
				table.invalidate();
				table.revalidate();
				model.fireTableStructureChanged();
				model.fireTableDataChanged();
				panel.removeAll();
				panel.add(newTable(model2, table));
				frame.add(panel, new GridBagConstraints(0, 0, 3, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
				frame.invalidate();
				frame.validate();
				frame.revalidate();
				frame.repaint();

			} else
				System.out.println("File is not a directory");

		});

		delete.addActionListener((e) -> {
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();
			Object object = table.getValueAt(row, col);
			String fileName = m + object.toString();
			System.out.println("The name of the file to delete: " + object.toString());
			File file = new File(fileName);
			if (!new File(file.getAbsolutePath() + "/").isDirectory()) {
				try {
					deleteFile(fileName);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			} else
				JOptionPane.showMessageDialog(frame, "This is a folder", null, JOptionPane.PLAIN_MESSAGE);
			table.revalidate();
			table.invalidate();
			frame.invalidate();
			frame.validate();
			frame.revalidate();
			frame.repaint();

		});

		back.addActionListener((e) -> {
			File filem = new File(m);
			System.out.println("Current " + m);
			System.out.println("Fileback " + filem);
			File fileBack = new File(
					filem.getAbsolutePath().substring(0, filem.getAbsolutePath().lastIndexOf("/")) + "/");
			m = fileBack.getAbsolutePath() + "/";
			System.out.println("File back " + fileBack);
			System.out.println("File m " + m);
			if (fileBack.isDirectory() == true) {
				System.out.println("Going back: File is directory");
				String[] arr1 = fileBack.list();
				tableModel model1 = new tableModel(fileBack, arr1);
				table.invalidate();
				table.revalidate();
				panel.removeAll();
				panel.add(newTable(model1, table));
				frame.add(panel, new GridBagConstraints(0, 0, 3, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
				frame.invalidate();
				frame.validate();
				frame.revalidate();
				frame.repaint();
			}
		});
	}

	private static void existsFile(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException(file.getName());
		}
	}

	public static void deleteFile(String nameFile) throws FileNotFoundException {
		existsFile(nameFile);
		new File(nameFile).delete();
		JOptionPane.showMessageDialog(frame, "The file " + nameFile + " was deleted", nameFile,
				JOptionPane.PLAIN_MESSAGE);

		frame.invalidate();
		frame.validate();
		frame.revalidate();
		frame.repaint();

	}

	public void readFile(String fileName) throws IOException {
		existsFile(fileName);
		Locale.setDefault(new Locale("ru"));
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),
					StandardCharsets.UTF_8));
			String strLine = null;
			String result = "";

			while ((strLine = br.readLine()) != null) {
				result += strLine + "<br>";
			}
			label3.setText("<html>" + result + "</html>");
		} catch (IOException e) {
			System.out.println("Ошибка");
		}
		br.close();
		JFrame fr = new JFrame();
		fr.add(label3);
		fr.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		fr.setSize(new Dimension(1000, 300));
		fr.setVisible(true);
	}

	public String selectFile(JTable table) {
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		Object object = table.getValueAt(row, col);
		String str = object.toString();
		return str;
	}

	public JScrollPane newTable(tableModel tableModel, JTable table) {
		JScrollPane scrollPane2 = new JScrollPane(table);
		table.setModel(tableModel);
		return scrollPane2;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		if (e.getClickCount() == 1) {
			label2.setText(table.getValueAt(row, col).toString());
			label1.setText("The name of the file: ");
		}
		if (e.getClickCount() == 2) {
			Object object = table.getValueAt(row, col);
			String str = object.toString();
			m = m + "/" + str + "/";
			System.out.println(m);
			File newfile = new File(m);
			System.out.println(newfile.getAbsolutePath());
			if (new File(newfile.getAbsolutePath() + "/").isDirectory()) {
				System.out.println("Going forward: File is directory");
				String[] arr2 = newfile.list();
				tableModel model2 = new tableModel(newfile, arr2);
				table.invalidate();
				table.revalidate();
				model.fireTableStructureChanged();
				model.fireTableDataChanged();
				panel.removeAll();
				panel.add(newTable(model2, table));
				frame.add(panel, new GridBagConstraints(0, 0, 3, 1, 1, 1, GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
				frame.invalidate();
				frame.validate();
				frame.revalidate();
				frame.repaint();

			} else
				System.out.println("File is not a directory");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void run() {
		while (true) {
			try {
				frame.invalidate();
				frame.validate();
				frame.revalidate();
				frame.repaint();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
