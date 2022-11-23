package transformer;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import shapeTools.GShapeTool;

public class GResize extends GTransformer {

	public GResize(GShapeTool selectedShape) {
		super(selectedShape);
	}

	private Point2D computedResize(double width, double height, double cx, double cy) {
		double deltaW = 0;
		double deltaH = 0;
		
		switch(this.selectedAnchor) {
			// N
			case x1y0: deltaW = 0; deltaH = -(cy-py);
				break;
			// S
			case x1y2: deltaW = 0; deltaH = cy-py;
				break;
			// W
			case x0y1: deltaW = -(cx-px); deltaH = 0;
				break;
			// E
			case x2y1: deltaW = cx-px; deltaH = 0;
				break;
			// NW
			case x0y0: deltaW = -(cx-px); deltaH = -(cy-py);
				break;
			// SW
			case x0y2: deltaW = -(cx-px); deltaH = cy-py;
				break;
			// NE
			case x2y0: deltaW = cx-px; deltaH = -(cy-py);
				break;
			// SE
			case x2y2: deltaW = cx-px; deltaH = cy-py; 
				break;
			default: break;
			}
		
		double xRatio = 1.0;
		double yRatio = 1.0;
		if(width > 0)
			xRatio = deltaW / width + xRatio;
		if(height > 0)
			yRatio = deltaH / height + yRatio;
		
		return new Point2D.Double(xRatio, yRatio);
	}

	@Override
	public void transform(Graphics2D graphics2d, double x, double y) {
		Point2D resizeRatio = this.computedResize(this.width, this.height, x, y);
		this.selectedShape.resize(graphics2d, this.originPoint, resizeRatio);
	}

}
