package frame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import main.GConstants.CFrame;
import java.awt.Dimension;

public class GFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	// Components
	private GToolBar toolBar;
	private GPanel panel;
	private GMenuBar menuBar;
	
	public GFrame() {
		// Initialize Attributes
		this.setLocation(CFrame.point);
		this.setSize(new Dimension(625, 625));
//		this.setUndecorated(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				if(!menuBar.checkSaveOrNot()) {
					System.exit(0);
				}
			}
		});
	
		// Initialize Components
		this.menuBar = new GMenuBar();
		this.setJMenuBar(this.menuBar);
		
		BorderLayout layoutManager = new BorderLayout();
		this.getContentPane().setLayout(layoutManager);
		
		this.toolBar = new GToolBar();
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		this.panel = new GPanel();
		this.getContentPane().add(panel, BorderLayout.CENTER);
		
		// set association
		this.menuBar.setAssociation(this.panel);
		this.toolBar.setAssociation(this.panel);
		this.panel.setAssociation(this.toolBar);
		
	}

	public void initialize() {
		this.toolBar.initialize();
		this.panel.initialize();
	}
	
}
