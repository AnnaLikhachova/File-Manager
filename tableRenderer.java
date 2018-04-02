package FileManager;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class tableRenderer implements TableCellRenderer {

	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);
		Color foreground, background;
		if (isSelected) {
			foreground = Color.YELLOW;
			background = Color.GREEN;
		} else {

			foreground = Color.WHITE;
			background = Color.BLUE;

		}

		renderer.setForeground(foreground);
		renderer.setBackground(background);
		return renderer;

	}
}
