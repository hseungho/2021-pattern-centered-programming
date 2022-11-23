package transformer;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import shapeTools.GShapeTool;
import shapeTools.GShapeTool.EAnchors;

public abstract class GTransformer {

	protected GShapeTool selectedShape;
	protected double px, py;
	protected double width, height;
	
	protected EAnchors selectedAnchor;
	protected Point2D originPoint;
	
	public GTransformer(GShapeTool selectedShape) {
		this.selectedShape = selectedShape;
	}
	
	public void initTransforming(Graphics2D graphics2d, double x, double y) {
		this.px = x;
		this.py = y;
		this.width = this.selectedShape.getWidth();
		this.height = this.selectedShape.getHeight();
	}
	public void keepTransforming(Graphics2D graphics2d, double x, double y) {
		this.transform(graphics2d, x, y);
		this.px = x;
		this.py = y;
	}
	public void finishTransforming(Graphics2D graphics2d, double x, double y) {}
	public void continueTransforming(Graphics2D graphics2d, double x, double y) {}
	
	public void setOriginPoint() {
		this.selectedAnchor = this.selectedShape.getSelectedAnchor();
		this.originPoint = this.selectedShape.getResizeOrigin(selectedAnchor);
	}
	
	public abstract void transform(Graphics2D graphics2d, double x, double y);
}
