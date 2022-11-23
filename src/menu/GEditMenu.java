package menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frame.GPanel;
import main.GConstants.EEditMenuItem;

public class GEditMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	// Component

	// Association
	private GPanel panel;
	
	public GEditMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();
		
		for(EEditMenuItem eEditMenuItem : EEditMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eEditMenuItem.getText());
			menuItem.setActionCommand(eEditMenuItem.name());
			menuItem.addActionListener(actionHandler);
			menuItem.setAccelerator(eEditMenuItem.getKeyStroke());
			this.add(menuItem);
		}
		
	}
	public void setAssociation(GPanel panel) {
		this.panel=panel;
	}
	
	private void redo() {
		this.panel.redo();
	}
	private void undo() {
		this.panel.undo();
	}
	private void cut() {
		this.panel.cut();
	}
	private void copy() {
		this.panel.copy();
	}
	private void paste() {
		this.panel.paste();
	}
	private void delete() {
		this.panel.deleteShape();
	}
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EEditMenuItem eMenuItem = EEditMenuItem.valueOf(e.getActionCommand());
			switch(eMenuItem) {
			case eUndo: undo();
				break;
			case eRedo: redo();
				break;
			case eCut: cut();
				break;
			case eCopy: copy();
				break;
			case ePaste: paste();
				break;
			case eGroup: 
				break;
			case eUngroup: 
				break;
			case eDelete: delete();
			default: break;
			
			}
		}
	}
	
	
}
