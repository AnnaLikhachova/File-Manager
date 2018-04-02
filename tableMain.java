package FileManager;

import javax.swing.JOptionPane;

public class tableMain {

	public static void main(String[] args) {
		try {
			new tableFrame();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "No file exists",
					 e.getMessage(), JOptionPane.PLAIN_MESSAGE);
		}
	}
}