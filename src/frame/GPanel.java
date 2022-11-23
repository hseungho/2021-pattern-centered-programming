package frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;

import javax.swing.JPanel;

import main.GConstants.EAction;
import main.GConstants.EDrawingStyle;
import main.GConstants.EPopupMenuItem;
import menu.GRightPasteMenu;
import menu.GRightPopupMenu;
import menu.GUndoStack;
import shapeTools.GShapeTool;
import shapeTools.GShapeTool.EAnchors;
import transformer.GMover;
import transformer.GResize;
import transformer.GRotate;
import transformer.GTransformer;

public class GPanel extends JPanel implements Printable {
	///////////////////////////////////////////////////////////////////////
	// Attributes
	private static final long serialVersionUID = 1L;

	// Components
	private GMouseHandler mouseHandler;
	private GActionHandler actionHandler;
	private Vector<GShapeTool> shapes;
	
	private int myLineWidth;
	private Color myLineColor;
	private Color myFillColor;
	private boolean isSelected;
	
	private GUndoStack undoStack;
	
	private GRightPopupMenu gRightPopupMenu;
	private GRightPasteMenu gRightPasteMenu;
	
	// associations
	private GToolBar toolbar;
	
	// working objects
	private GShapeTool shapeTool;
	private GShapeTool selectedShape;
	private GShapeTool copiedShape;
	private GTransformer transformer;
	private boolean bModified;
	
	///////////////////////////////////////////////////////////////////////
	// getters and setters
	public void setSelection(GShapeTool shapeTool) { this.shapeTool = shapeTool; }
	public Vector<GShapeTool> getShapes() { return this.shapes;	}
	public void setShapes(Vector<GShapeTool> shapes) {
		this.shapes = shapes;
		this.repaint();
	}
	public boolean isModified() { return this.bModified; }
	public void setModified(boolean bModified) { this.bModified = bModified; }
	
	public void setLineWidth(int i) {
		this.myLineWidth = i;
		if(isSelected) {
			this.selectedShape.setLineWidth(this.myLineWidth);
		}
	}
	public int getLineWidth() {	return this.myLineWidth;}
	public void setLineColor(Color color) { 
		this.myLineColor = color;
		if(isSelected) {
			this.selectedShape.setLineColor(this.myLineColor);
			this.repaint();
		}
	}
	
	public Color getLineColor() { return this.myLineColor; }
	public void setFillColor(Color color) {
		this.myFillColor = color;
		if(isSelected) {
			this.selectedShape.setFillColor(this.myFillColor);
			this.repaint();
		} 
	}
	
	public Color getFillColor() { return this.myFillColor; }
	
	// Constructors
	public GPanel() {
		this.shapes = new Vector<GShapeTool>();

		this.mouseHandler = new GMouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.addMouseWheelListener(mouseHandler);
		
		this.actionHandler = new GActionHandler();
		this.gRightPopupMenu = new GRightPopupMenu(this.actionHandler);
		this.gRightPasteMenu = new GRightPasteMenu(this.actionHandler);
		
		this.undoStack = new GUndoStack();
		this.undoStack.push(this.deepCopy(this.shapes));
		
		this.bModified = false;
		
		this.isSelected=false;
	}
	public void initialize() {
		this.setBackground(Color.WHITE);
	}
	public void setAssociation(GToolBar toolbar) { this.toolbar = toolbar; }
	public void clearScreen() {
		this.shapes.clear();
		this.repaint();
	}
	
	// methods
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if(pageIndex>=1)
			return NO_SUCH_PAGE;
		graphics.translate((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY());
		paint(graphics);
		
		return PAGE_EXISTS;
	}
	
	public Vector<GShapeTool> deepCopy(Vector<GShapeTool> original){
		if(original!=null) {
			Vector<GShapeTool> clonedShapes = (Vector<GShapeTool>) original.clone();
			for(int i=0; i<original.size(); i++) {
				clonedShapes.set(i, (GShapeTool) original.get(i).clone());
			}
			return clonedShapes;
		}
		else
			return null;
	}
	
	public void deleteShape() {
		if(isSelected) {
			this.shapes.remove(this.selectedShape);
			this.selectedShape = null;
			this.repaint();
		}
	}
	public void cut() {
		if(isSelected) {
			this.shapes.remove(this.selectedShape);
			this.repaint();
			this.copy();
		}
	}
	public void copy() {
		if(isSelected) {
			this.copiedShape = (GShapeTool) this.selectedShape.clone();
 		}
	}
	public void paste() {
		GShapeTool copy = (GShapeTool) this.copiedShape.clone();
		copy.pasteShape();
		copy.draw((Graphics2D) getGraphics());
		this.shapes.add(copy);
		this.undoStack.push(this.deepCopy(this.shapes));
		this.setSelected(copy);
		this.copiedShape = copy;
	}
	
	public void undo() {
		Vector<GShapeTool> undoShapes = this.deepCopy(this.undoStack.undo());
		if(undoShapes!=null) {
			this.shapes = undoShapes;
			this.repaint();
		}
	}
	
	public void redo() {
		Vector<GShapeTool> redoShapes = this.deepCopy(this.undoStack.redo());
		if(redoShapes!=null) {
			this.shapes = redoShapes;
			this.repaint();
		}
	}
	
	public void paint(Graphics graphics) {
		super.paint(graphics);
		for(GShapeTool shape : this.shapes) {
			shape.draw((Graphics2D)graphics);
		}
	}
	
	private void setSelected(GShapeTool selectedShape) {
		for(GShapeTool shape : this.shapes) {
			shape.setSelected(false);
		}
		this.selectedShape = selectedShape;
		this.selectedShape.setSelected(true);
		this.isSelected=true;
		this.toolbar.setStyleButton(this.selectedShape.getLineWidth(), this.selectedShape.getLineColor(), this.selectedShape.getFillColor());
		this.repaint();
	}
	
	private void setDeselected() {
		for(GShapeTool shape : this.shapes) {
			shape.setSelected(false);
		}
		this.isSelected=false;
		this.repaint();
	}

	private GShapeTool onShape(int x, int y) {
		for(GShapeTool shape : this.shapes) {
			EAction eAction = shape.containes(x, y);
			if(eAction != null) {
				this.selectedShape = shape;
				return shape;
			}
		}
		return null;
	}
	
	private void changeCursor(int x, int y) {
		for(GShapeTool shape : this.shapes) {
			EAction eAction = shape.containes(x, y);
			if(eAction!=null) {
				switch(eAction) {
					case eMove : this.setCursor(new Cursor(Cursor.MOVE_CURSOR)); 
						return;
					case eResize :
						EAnchors eAnchors = shape.getSelectedAnchor();
						switch(eAnchors) {
						// N, S
						case x1y0: case x1y2:
							this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
							break;
							// W, E
						case x0y1: case x2y1: 
							this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
							break;
							// NW, SE
						case x0y0: case x2y2 :
							this.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
							break;
							// NE, SW
						case x2y0: case x0y2 :
							this.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
							break;
						default: break;
						}
						return;
					case eRotate :
						this.setCursor(new Cursor(Cursor.HAND_CURSOR));
						return;
					default: break;
				}
			}
		}
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void setShapeStyle() {
		this.selectedShape.setLineWidth(getLineWidth());
		this.selectedShape.setLineColor(getLineColor());
		this.selectedShape.setFillColor(getFillColor());
	}
	
	private void initDrawing(int x, int y) {
		this.selectedShape = (GShapeTool) this.shapeTool.clone();
		this.selectedShape.setInitPoint(x, y);
	}
	private void setIntermediatePoint(int x, int y) {
		this.selectedShape.setIntermediatePoint(x, y);
	}
	private void keepDrawing(int x, int y) {
		// exclusive or mode
		Graphics2D graphics2D = (Graphics2D) getGraphics();
		graphics2D.setXORMode(getBackground());
		this.setShapeStyle();
		this.selectedShape.animate(graphics2D, x, y);
	}
	private void finishDrawing(int x, int y) {
		this.setShapeStyle();
		this.selectedShape.setFinalPoint(x, y);
		this.shapes.add(this.selectedShape);
		this.bModified = true;
		this.setSelected(this.selectedShape);
		
		this.undoStack.push(this.deepCopy(this.shapes));
	}
	
	private void initTransforming(GShapeTool selectedShape, int x, int y) {
		this.selectedShape = selectedShape;
		EAction eAction = this.selectedShape.getAction();
		switch(eAction) {
		case eMove:
			this.transformer = new GMover(selectedShape);
			break;
		case eResize:
			this.transformer = new GResize(selectedShape);
			this.transformer.setOriginPoint();
			break;
		case eRotate:
			this.transformer = new GRotate(selectedShape);
			break;
		default:
			break;
		}
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(this.getBackground());
		this.transformer.initTransforming(graphics2d, x, y);
		this.selectedShape.setSelected(false);
	}
	private void keepTransforming(int x, int y) {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(this.getBackground());
		this.transformer.keepTransforming(graphics2d, x, y);
		this.repaint();
	}
	private void finishTransforming(int x, int y) {
		Graphics2D graphics2d = (Graphics2D) this.getGraphics();
		graphics2d.setXORMode(this.getBackground());
		this.transformer.finishTransforming(graphics2d, x, y);
		this.setSelected(this.selectedShape);
		this.bModified = true;

		this.undoStack.push(this.deepCopy(this.shapes));
	}
	
	//////////////////////////////////////////////////////////////////////
	// inner classes
	private class GMouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener{
		
		private boolean isDrawing;
		private boolean isTransforming;
		
		public GMouseHandler() {
			this.isDrawing = false;
			this.isTransforming = false;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(!isDrawing) {
					GShapeTool selectedShape = onShape(e.getX(), e.getY());
					if(selectedShape == null) {
						setDeselected();
						setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
						if(shapeTool.getDrawingStyle() == EDrawingStyle.e2PointDrawing) {
							initDrawing(e.getX(), e.getY());
							this.isDrawing=true;
						}
					}
					else {
						initTransforming(selectedShape, e.getX(), e.getY());
						this.isTransforming = true;
					}
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if(isDrawing) {
				if(shapeTool.getDrawingStyle() == EDrawingStyle.e2PointDrawing) {
					keepDrawing(e.getX(), e.getY());
				}
			}
			else if(this.isTransforming) {
				keepTransforming(e.getX(), e.getY());
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(isDrawing) {
				if(shapeTool.getDrawingStyle() == EDrawingStyle.e2PointDrawing) {
					finishDrawing(e.getX(), e.getY());
					this.isDrawing=false;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			else if(this.isTransforming) {
				finishTransforming(e.getX(), e.getY());
				this.isTransforming = false;
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			if(isDrawing) {
				if(shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
					keepDrawing(e.getX(), e.getY());
				}
			}
			else {
				changeCursor(e.getX(), e.getY());
			}
		}
		private void mouseLButton1Clicked(MouseEvent e) {
			if(!isDrawing) {
				GShapeTool selectedShape = onShape(e.getX(), e.getY());
				if(selectedShape == null) {
					if(shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
						initDrawing(e.getX(), e.getY());
						this.isDrawing = true;
					}
					else if(shapeTool.getDrawingStyle() == EDrawingStyle.e2PointDrawing) {
						initDrawing(e.getX(), e.getY());
						keepDrawing(e.getX()+50, e.getY()+50);
						finishDrawing(e.getX()+50, e.getY()+50);
					}
				}
				else {
					setSelected(selectedShape);
				}
			}
			else {
				if(shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
					setIntermediatePoint(e.getX(), e.getY());
				}
			}
		}
		private void mouseLButton2Clicked(MouseEvent e) {
			if(isDrawing) {
				if(shapeTool.getDrawingStyle() == EDrawingStyle.eNPointDrawing) {
					finishDrawing(e.getX(), e.getY());
					this.isDrawing = false;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
		private void mouseRButton1Clicked(MouseEvent e) {
			GShapeTool selectedShape = onShape(e.getX(), e.getY());
			if(selectedShape == null) {
				setDeselected();
				gRightPasteMenu.show(getParent(), e.getX(), e.getY()+30);
			}
			else
				gRightPopupMenu.show(getParent(), e.getX(), e.getY()+30);
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(e.getClickCount() == 1) {
					this.mouseLButton1Clicked(e);
				}
				else if(e.getClickCount() == 2) {
					this.mouseLButton2Clicked(e);
				}
			}else if(e.getButton() == MouseEvent.BUTTON3) {
				if(e.getClickCount() == 1) {
					this.mouseRButton1Clicked(e);
				}
			}
		}
	}
	
	public class GActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EPopupMenuItem ePopupMenuItem = EPopupMenuItem.valueOf(e.getActionCommand());
			switch(ePopupMenuItem) {
			case eDelete: deleteShape();
				break;
			case eCut: cut();
				break;
			case eCopy: copy();
				break;
			case ePaste: paste();
				break;
			}
		}
	}
}
