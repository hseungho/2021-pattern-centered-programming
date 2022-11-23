package frame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.GConstants.EShapeTool;

public class GToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	private JButton btLineWidth;
	private JButton btLineColor;
	private JButton btShapeColor;
	private Vector<JButton> shapeButtons;

	private JSlider slider;
	private int stroke;
	
	//association
	private GPanel panel;
	
	public GToolBar() {
		// initialize components
		ActionHandler actionHandler = new ActionHandler();
		StyleActionListener styleActionHandler = new StyleActionListener();
		
		this.shapeButtons = new Vector<JButton>();
		
		for(EShapeTool ebutton: EShapeTool.values()) {
			JButton button = new JButton(ebutton.getIcon());
			button.setActionCommand(ebutton.toString());
			button.addActionListener(actionHandler);
			button.setBackground(Color.white);
			this.shapeButtons.add(button);
			this.add(button);
		}

		this.add(Box.createHorizontalGlue());
		
		this.btLineWidth = new JButton();
		this.btLineWidth.setIcon(new ImageIcon("icon/lineWidthIcon.png"));
		this.btLineWidth.setActionCommand("lineWidth");
		this.btLineWidth.addActionListener(styleActionHandler);
		this.btLineWidth.setBackground(Color.white);
		this.add(this.btLineWidth);
		
		this.btLineColor = new JButton();
		this.btLineColor.setIcon(new ImageIcon("icon/lineColorIcon.png"));
		this.btLineColor.setActionCommand("lineColor");
		this.btLineColor.addActionListener(styleActionHandler);
		this.add(this.btLineColor);
		
		this.btShapeColor = new JButton();
		this.btShapeColor.setIcon(new ImageIcon("icon/shapeColorIcon.png"));
		this.btShapeColor.setActionCommand("shapeColor");
		this.btShapeColor.addActionListener(styleActionHandler);
		this.add(this.btShapeColor);

		this.slider = new JSlider(1,6);
		this.stroke=1;
	}
	public void initialize() {
		((JButton)(this.getComponent(EShapeTool.eRectangle.ordinal()))).doClick();
		
		this.panel.setLineWidth(this.stroke);
		this.panel.setLineColor(Color.black);
		this.btLineColor.setBackground(Color.black);
		this.panel.setFillColor(new Color(47,114,225));
		this.btShapeColor.setBackground(new Color(47,114,225));
	}

	public void setAssociation(GPanel panel) {
		this.panel = panel;
	}
	
	private void createSlider() {
		this.slider.setMajorTickSpacing(1);
		this.slider.setMinorTickSpacing(1);
		this.slider.setPaintLabels(true);
		this.slider.setPaintTicks(true);
		this.slider.setPaintTrack(true);
		this.slider.setValue(this.stroke);
	}
	private int showSliderDiag() {
		JOptionPane op = new JOptionPane();
		this.createSlider();
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider sd = (JSlider) e.getSource();
				if(!sd.getValueIsAdjusting()) {
					int val = sd.getValue();
					op.setInputValue(val);
				}
			}
		});
		op.setMessage(new Object[] {"선 두께 : ", slider});
		op.setMessageType(JOptionPane.QUESTION_MESSAGE);
		op.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog = op.createDialog(this, "선 두께");
		dialog.setVisible(true);
		if(op.getInputValue() == "uninitializedValue")
			return 0;
		return (int)op.getInputValue();
	}
	
	private Color showColorDiag(String s) {
		JColorChooser chooser = new JColorChooser();
		Color selectedColor = null;
		if(s == "lineColor") 
			selectedColor = chooser.showDialog(null, "선 색", this.btLineColor.getBackground());
		else
			selectedColor = chooser.showDialog(null, "채우기 색", this.btShapeColor.getBackground());
		
		
		if(selectedColor != null) 
			return selectedColor;
		else
			return null;
	}
	
	private void setSelectedButton(JButton bt) {
		for(JButton button :this.shapeButtons) {
			if(button == bt) 
				button.setBackground(new Color(166,203,236));
			else 
				button.setBackground(Color.white);
		}
	}

	public void setStyleButton(int stroke, Color lineColor, Color fillColor) {
		this.stroke = stroke;
		this.btLineColor.setBackground(lineColor);
		this.btShapeColor.setBackground(fillColor);
	}
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EShapeTool eShapeTool = EShapeTool.valueOf(e.getActionCommand());
			panel.setSelection(eShapeTool.getShapeTool());
			setSelectedButton((JButton) e.getSource());
		}
	}

	private class StyleActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			switch(action) {
			case "lineWidth" :
				int bs = showSliderDiag();
				if(bs!=0)
					panel.setLineWidth(bs);
				break;
			case "lineColor":
				Color selectedLineColor = showColorDiag("lineColor");
				if(selectedLineColor != null) {
					panel.setLineColor(selectedLineColor);
					btLineColor.setBackground(selectedLineColor);
				}
				break;
			case "shapeColor" :
				Color selectedShapeColor = showColorDiag("shapeColor");
				if(selectedShapeColor != null) {
					panel.setFillColor(selectedShapeColor);
					btShapeColor.setBackground(selectedShapeColor);
				}
				break;
			}
		}
	}



}
