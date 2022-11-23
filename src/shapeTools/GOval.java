package shapeTools;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import main.GConstants.EDrawingStyle;

public class GOval extends GShapeTool {
	// attributes
	private static final long serialVersionUID = 1L;
	// components
	// constructor
	public GOval() {
		super(EDrawingStyle.e2PointDrawing);
		this.shape = new Ellipse2D.Float();
	}
	public Object clone() {
		GShapeTool cloned = (GShapeTool) super.clone();
		cloned.shape = (Shape) ((Ellipse2D.Float)(this.shape)).clone();
		return cloned;
	}
	
	// methods
	@Override
	public void setInitPoint(int x, int y) {
		Ellipse2D ellipse = (Ellipse2D) this.shape;
		ellipse.setFrame(x, y, 0, 0);
	}
	@Override
	public void setFinalPoint(int x, int y) {
		
	}
	@Override
	public void movePoint(int x, int y) {
		Ellipse2D ellipse = (Ellipse2D) this.shape;
		ellipse.setFrame(ellipse.getX(), ellipse.getY(), x-ellipse.getX(), y-ellipse.getY());
	}
	@Override
	public void pasteShape() {
		Ellipse2D copyEllipse = (Ellipse2D) this.shape;
		copyEllipse.setFrame(this.shape.getBounds().x+20, this.shape.getBounds().y+20, this.shape.getBounds().getWidth(), this.shape.getBounds().getHeight());
	}
}
