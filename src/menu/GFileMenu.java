package menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import frame.GPanel;
import main.GConstants.EFileMenuItem;
import shapeTools.GShapeTool;

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	// Component
	private File file;;
	private JFileChooser chooser;
	
	// Association
	private GPanel panel;
	
	public GFileMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();
		
		for(EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
			menuItem.setActionCommand(eFileMenuItem.name());
			menuItem.addActionListener(actionHandler);
			menuItem.setAccelerator(eFileMenuItem.getKeyStroke());
			this.add(menuItem);
		}
		
		this.file = null;
		
	}
	public void setAssociation(GPanel panel) {
		this.panel=panel;
	}
	
	private void openFile() {
		// serialize(objectOutputStream) -> buffer(BufferedOutputStream) -> file(FileOutputStream)
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.file)));
			Vector<GShapeTool> shapes = (Vector<GShapeTool>) objectInputStream.readObject();
			this.panel.setShapes(shapes);
			objectInputStream.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private void saveFile() {
		// serialize(objectOutputStream) -> buffer(BufferedOutputStream) -> file(FileOutputStream)
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.file)));
			objectOutputStream.writeObject(this.panel.getShapes());
			objectOutputStream.close();
			this.panel.setModified(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void openChooser() {
		this.chooser = new JFileChooser();
		this.chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.chooser.setFileFilter(new FileNameExtensionFilter("HSH 형식 (*.hsh)", "hsh"));
	}
	private File createFile(String fileName) {
		if(!fileName.endsWith(".hsh")&&!fileName.endsWith(".HSH"))
			fileName += ".hsh";
		File file = new File(fileName);
		return file;
	}
	private boolean checkFileExist(File file) {
		if(file.exists()) {
			int reply = JOptionPane.showConfirmDialog(this.panel, file.getName()+"이(가) 이미 있습니다. 바꾸시겠습니까?",
					"다른 이름으로 저장 확인", JOptionPane.YES_NO_OPTION);
			if(reply == JOptionPane.YES_OPTION)
				return true;
			else
				return false;
		}
		else
			return true;
	}
	
	public boolean checkSaveOrNot() {
		boolean bCancel = true;
		if(this.panel.isModified()) {
			// save
			int reply = JOptionPane.showConfirmDialog(this.panel, "변경내용을 저장할까요?");
			if(reply == JOptionPane.OK_OPTION) {
				this.save();
				bCancel = false;
			}
			else if(reply == JOptionPane.NO_OPTION) {
				this.panel.setModified(false);
				bCancel = false;
			}
		}
		else 
			bCancel = false;
		return bCancel;
	}
	
	private void nnew() {
		if(!this.checkSaveOrNot()) {
			this.panel.clearScreen();
			this.file = null;
		}
	}
	private void open() {
		if(!this.checkSaveOrNot()) { // if not cancel
			this.openChooser();
			int returnVal = chooser.showOpenDialog(this.panel);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				this.file = chooser.getSelectedFile();
				this.openFile();
			}
			
		} // else { cancel }
	}
	private void save() {
		if(this.panel.isModified()) {
			if(this.file == null)
				this.saveAs();
			else
				this.saveFile();
		}
	}
	private void saveAs() {
		this.openChooser();
		int returnVal = this.chooser.showSaveDialog(this.panel);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			this.file = this.createFile(this.chooser.getSelectedFile().toString());
			if(this.checkFileExist(this.file))
				this.saveFile();
		}
	}
	private void print() {
		PrinterJob print = PrinterJob.getPrinterJob();
		print.setPrintable(this.panel);
		if(!print.printDialog()) {
			return ;
		}
		try {
			print.print();
		} catch (PrinterException e) {
			JOptionPane.showMessageDialog(this, "프린트 에러");
			e.printStackTrace();
		}
	}
	private void exitProgram() {
		if(!checkSaveOrNot()) {
			System.exit(0);
		}
	}
	
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EFileMenuItem eMenuItem = EFileMenuItem.valueOf(e.getActionCommand());
			switch(eMenuItem) {
			case eNew: nnew();
				break;
			case eOpen: open();
				break;
			case eSave:	save();
				break;
			case eSaveAs: saveAs();
				break;
			case ePrint: print();
				break;
			case eExit: exitProgram();
				break;
			default: break;
			
			}
		}
	}
	
	
}
