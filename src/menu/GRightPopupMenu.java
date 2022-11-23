package menu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import frame.GPanel.GActionHandler;
import main.GConstants.EPopupMenuItem;

public class GRightPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	public GRightPopupMenu(GActionHandler actionHandler) {
		for(EPopupMenuItem ePopupMenuItem : EPopupMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(ePopupMenuItem.getText());
			menuItem.setActionCommand(ePopupMenuItem.name());
			menuItem.addActionListener(actionHandler);
			this.add(menuItem);
		}
	}
}
